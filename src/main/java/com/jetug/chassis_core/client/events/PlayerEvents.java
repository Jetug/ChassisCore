package com.jetug.chassis_core.client.events;

import com.jetug.chassis_core.client.render.renderers.CustomHandRenderer;
import com.mojang.math.Axis;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderArmEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.jetug.chassis_core.client.events.InputEvents.*;
import static com.jetug.chassis_core.common.util.helpers.PlayerUtils.*;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class PlayerEvents {
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
        if (isWearingChassis(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent()
    public static void onRenderHand(RenderArmEvent event) {
        if (!isLocalWearingChassis()) return;

        var poseStack = event.getPoseStack();
        poseStack.pushPose();
//        poseStack.mulPose(Vector3f.YP.rotationDegrees(180));
        poseStack.mulPose(Axis.ZP.rotationDegrees(90));
//        poseStack.mulPose(Axis.YP.rotationDegrees(90));
        poseStack.translate(-0.2 + X, 0.1 + Y, -0.6 + Z); //-0.6

        CustomHandRenderer.getHandRenderer().render(
                poseStack,
                getLocalPlayerChassis().getHandEntity(),
                event.getMultiBufferSource(),
                null,
                null,
                event.getPackedLight());

        poseStack.popPose();
        event.setCanceled(true);
    }

//    @OnlyIn(Dist.CLIENT)
//    @SubscribeEvent()
//    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
//        if (!isLocalWearingChassis()) return;
//
//    }

//    @OnlyIn(Dist.CLIENT)
//    @SubscribeEvent()
//    public static void onRenderHand(RenderArmEvent event) {
//        if (!isLocalWearingChassis()) return;
//
//        var poseStack = event.getPoseStack();
//        poseStack.translate(0.5, -0.5, -0.6);
//
//        CustomHandRenderer.getHandRenderer().render(
//                getLocalPlayerChassis(),
//                poseStack,
//                event.getMultiBufferSource(),
//                event.getPackedLight());
//
//        event.setCanceled(true);
//    }
}