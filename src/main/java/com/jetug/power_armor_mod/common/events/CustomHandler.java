package com.jetug.power_armor_mod.common.events;

import com.jetug.power_armor_mod.common.data.constants.Global;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.EventPriority;

import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.getPlayerPowerArmor;
import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.isWearingPowerArmor;
import static java.lang.System.out;
import static net.minecraftforge.event.entity.player.PlayerInteractEvent.*;

@Mod.EventBusSubscriber(modid = Global.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CustomHandler {

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onEntityContainerChange(ContainerChangedEvent event) {
        var entity = event.getEntity();
        out.println(entity);
    }

    @SubscribeEvent
    public static void onPlayerInteract(EntityInteract event) {
        if(cancelInteraction(event.getPlayer()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onPlayerInteract(EntityInteractSpecific event) {
        if(cancelInteraction(event.getPlayer()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onPlayerInteract(RightClickBlock event) {
        if(cancelInteraction(event.getPlayer()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onPlayerHurt(LivingHurtEvent event) {
        if(event.getEntity() instanceof Player player
                && isWearingPowerArmor(player)
                && getPlayerPowerArmor(player).hasFireProtection()){
            event.setCanceled(true);
        }
    }

    private static boolean cancelInteraction(Player player){
        return isWearingPowerArmor(player) && getPlayerPowerArmor(player).isChargingAttack();
    }

//    @SubscribeEvent
//    public static void onBlockBreak(BlockEvent.BreakEvent event) {
//        var world = event.getWorld();
//        BlockPos pos = event.getPos();
//
//        Direction direction = event.getPlayer().getDirection();
//        BlockPos centerPos = pos.offset(direction.getNormal());
//
//        // Копаем блоки 3 на 3
//        for (int xOffset = -1; xOffset <= 1; xOffset++) {
//            for (int yOffset = -1; yOffset <= 1; yOffset++) {
//                BlockPos targetPos = centerPos.offset(direction.getStepX() * xOffset, yOffset, direction.getStepZ() * xOffset);
//                world.destroyBlock(targetPos, true);
//            }
//        }
//    }
//
//    private void dig(BlockPos pos, Player player){
//        var direction = player.getDirection();
//        var centerPos = pos.offset(direction.getNormal());
//
//        // Копаем блоки 3 на 3
//        for (int xOffset = -1; xOffset <= 1; xOffset++) {
//            for (int yOffset = -1; yOffset <= 1; yOffset++) {
//                BlockPos targetPos = centerPos.offset(direction.getStepX() * xOffset, yOffset, direction.getStepZ() * xOffset);
//                player.level.destroyBlock(targetPos, true);
//            }
//        }
//    }

    public static BlockPos getBlockLookingAt() {
        Minecraft minecraft = Minecraft.getInstance();

        var playerPos = minecraft.player.getPosition(1f);
        var lookVec = minecraft.player.getViewVector(1f);
        double reachDistance = 5.0;

//        BucketItem
//
//        Vector3d endVec = playerPos.add(lookVec.scale(reachDistance));
//        RayTraceResult result = minecraft.level.blocj.rayTraceBlocks(new RayTraceContext(playerPos, endVec, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, minecraft.player));
//
//        if (result.getType() == RayTraceResult.Type.BLOCK) {
//            return result.getPos();
//        }

        return null;
    }


//    @SubscribeEvent
//    public static void onPlayerInteractEntity(PlayerInteractEvent.EntityInteract event) {
//        Player player = event.getPlayer();
//        Entity entity = event.getTarget();
//
//        var t = entity;
//        // Ваш код для работы с сущностью, на которую игрок смотрит
//    }

}