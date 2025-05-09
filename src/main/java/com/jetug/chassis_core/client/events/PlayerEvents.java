package com.jetug.chassis_core.client.events;

import com.mojang.math.Axis;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.*;

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
        if (isLocalWearingChassis() && getLocalPlayerChassis().renderHand()) {
            var poseStack = event.getPoseStack();
            poseStack.pushPose();
            {
                var isRight = event.getArm() == HumanoidArm.RIGHT;
                var side = isRight ? 1 : -1;
                poseStack.mulPose(Axis.XP.rotationDegrees(90));
                if(isRight) {
                    poseStack.translate(18 / 10D / 16D, 91 / 10D / 16D, -155 / 10D / 16D);
                }
                else {
                    poseStack.translate(140 / 10D / 16D, 106 / 10D / 16D,  -155 / 10D / 16D);
                }

                poseStack.mulPose(Axis.ZP.rotationDegrees(180 * side));

                renderHand(event.getArm(), poseStack, event.getMultiBufferSource(), event.getPackedLight());
            }
            poseStack.popPose();
            event.setCanceled(true);
        }
    }
}