package com.jetug.chassis_core.common.foundation.item;

import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import static com.jetug.chassis_core.common.util.helpers.EntityHelper.clearItemTags;
import static com.jetug.chassis_core.common.util.helpers.EntityHelper.entityFromItem;

public class ChassisItem <T extends WearableChassis> extends Item {

    private final EntityType.EntityFactory<T> factory;
    private final RegistryObject<EntityType<T>> type;

    public ChassisItem(Item.Properties properties, RegistryObject<EntityType<T>> type, EntityType.EntityFactory<T> factory) {
        super((properties.stacksTo(1)));
        this.factory = factory;
        this.type = type;
    }

    @Override
    public void onCraftedBy(ItemStack itemStack, @NotNull Level world, @NotNull Player player) {
        itemStack.setTag(new CompoundTag());
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        if (context.getClickedFace() != Direction.UP)
            return InteractionResult.FAIL;

        var savedEntity = entityFromItem(context.getItemInHand(), context.getLevel());

        if(savedEntity != null)
            summonSavedEntity(savedEntity, context);
        else summonNewEntity(context);

        return InteractionResult.SUCCESS;
    }

    private static void summonSavedEntity(Entity savedEntity, UseOnContext context) {
        var stack = context.getItemInHand();

        moveEntityToClickedBlock(savedEntity, context);

        if (context.getLevel().addFreshEntity(savedEntity)) {
            clearItemTags(stack);
            context.getPlayer().getInventory().removeItem(stack);
            //stack.shrink(1);
        }
    }

    private void summonNewEntity(UseOnContext context) {
        var stack = context.getItemInHand();
        var world = context.getLevel();
        var entity = factory.create(type.get(), world);
        //entity.inventory.setEquipment(ChassisPart.ENGINE.ordinal(), new ItemStack(ItemRegistry.ENGINE.get()));
        //entity.setPos(context.getClickLocation());
        moveEntityToClickedBlock(entity, context);

        if (world.addFreshEntity(entity)) {
            stack.shrink(1);
        }
    }

    private static void moveEntityToClickedBlock(Entity savedEntity, UseOnContext context) {
        var clickedPos = context.getClickedPos();
        savedEntity.absMoveTo(
                clickedPos.getX() + 0.5D,
                clickedPos.getY() + 1,
                clickedPos.getZ() + 0.5D,
                180 + (context.getHorizontalDirection()).toYRot(), 0.0F);
    }

}