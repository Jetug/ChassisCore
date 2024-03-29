package com.jetug.power_armor_mod.common.foundation.item;

import com.jetug.power_armor_mod.common.data.enums.BodyPart;
import com.jetug.power_armor_mod.common.foundation.ModCreativeModeTab;
import com.jetug.power_armor_mod.common.foundation.entity.WearableChassis;
import com.jetug.power_armor_mod.common.foundation.registery.EntityTypeRegistry;
import com.jetug.power_armor_mod.common.foundation.registery.ItemRegistry;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import static com.jetug.power_armor_mod.common.foundation.EntityHelper.*;

public class ArmorChassisItem extends Item {

    public ArmorChassisItem() {
        super((new Item.Properties()).stacksTo(1).tab(ModCreativeModeTab.MY_TAB));
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

    private static void summonNewEntity(UseOnContext context) {
        var stack = context.getItemInHand();
        var world = context.getLevel();
        var entity = new WearableChassis(EntityTypeRegistry.ARMOR_CHASSIS.get(), world);
        entity.inventory.setItem(BodyPart.ENGINE.ordinal(), new ItemStack(ItemRegistry.ENGINE.get()));
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