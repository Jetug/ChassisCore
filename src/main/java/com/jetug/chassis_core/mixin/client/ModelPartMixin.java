package com.jetug.chassis_core.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.jetug.chassis_core.common.util.helpers.PlayerUtils.*;

@Mixin(ModelPart.class)
public abstract class ModelPartMixin {
    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V", at = @At("HEAD"), cancellable = true)
    public void render(PoseStack poseStack, VertexConsumer pVertexConsumer, int packedLight, int pPackedOverlay,
                       float pRed, float pGreen, float pBlue, float pAlpha, CallbackInfo ci) {
        var playerModel = getPlayerModel();
        if(isLocalWearingChassis()) {
            poseStack.pushPose();
            {
                if (playerModel.rightArm.equals(this)) {
//                    poseStack.translate(-12 / 10D / 16D, -63 / 10D / 16D, -105 / 10D / 16D);
//                    poseStack.translate(14 / 10D / 16D, 6 / 10D / 16D, 0 / 10D / 16D);
                    renderHand(HumanoidArm.RIGHT, poseStack, null, packedLight);
                    ci.cancel();
                } else if (playerModel.leftArm.equals(this)) {
//                    poseStack.translate(-12 / 10D / 16D, -63 / 10D / 16D, -105 / 10D / 16D);
//                    poseStack.translate(180 / 10D / 16D, 0 / 10D / 16D, 0 / 10D / 16D);
                    renderHand(HumanoidArm.LEFT, poseStack, null, packedLight);
                    ci.cancel();
                }
                else if(playerModel.rightSleeve.equals(this) || playerModel.leftSleeve.equals(this)) {
                    ci.cancel();
                }
            }
            poseStack.popPose();
        }
    }


    private PlayerModel<AbstractClientPlayer> getPlayerModel() {
        var client = Minecraft.getInstance();
        var playerEntityRenderer = (PlayerRenderer) client.getEntityRenderDispatcher().getRenderer(client.player);
        return playerEntityRenderer.getModel();
    }

}
