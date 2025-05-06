package com.jetug.chassis_core.mixin.client;

import com.jetug.chassis_core.client.render.renderers.CustomHandRenderer;
import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.Boat;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.UUID;

import static com.jetug.chassis_core.common.util.helpers.PlayerUtils.getLocalPlayerChassis;
import static com.jetug.chassis_core.common.util.helpers.PlayerUtils.isLocalWearingChassis;

@Mixin(ModelPart.class)
public abstract class ModelPartMixin {
    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V", at = @At("HEAD"), cancellable = true)
    public void render(PoseStack poseStack, VertexConsumer pVertexConsumer, int packedLight, int pPackedOverlay,
                       float pRed, float pGreen, float pBlue, float pAlpha, CallbackInfo ci) {
        var playerModel = getPlayerModel();
        if (playerModel.rightArm.equals(this) && isLocalWearingChassis()) {
            poseStack.pushPose();
            {
                poseStack.mulPose(Axis.ZP.rotationDegrees(90));
                poseStack.translate(-0.15, -0.1, -0.5);

                CustomHandRenderer.getHandRenderer().render(
                        poseStack,
                        getLocalPlayerChassis().getHandEntity(),
                        null,
                        null,
                        null,
                        packedLight);
            }
            poseStack.popPose();
            ci.cancel();
        }
    }

    private PlayerModel<AbstractClientPlayer> getPlayerModel() {
        var client = Minecraft.getInstance();
        var playerEntityRenderer = (PlayerRenderer) client.getEntityRenderDispatcher().getRenderer(client.player);
        return playerEntityRenderer.getModel();
    }

}
