package com.jetug.chassis_core.client.render.layers;

import com.jetug.chassis_core.common.util.helpers.PlayerUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;

import static com.jetug.chassis_core.common.util.helpers.PlayerUtils.renderHand;

public class CustomHandLayer extends RenderLayer<Player, PlayerModel<Player>> {
    private final CustomHandModel handModel; // Your GeckoLib model

    public CustomHandLayer(RenderLayerParent<Player, PlayerModel<Player>> renderer) {
        super(renderer);
        this.handModel = new CustomHandModel();
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Player player,
                       float limbSwing, float limbSwingAmount, float partialTick,
                       float ageInTicks, float netHeadYaw, float headPitch) {

        // Get the vanilla right hand ModelPart
        ModelPart rightHand = this.getParentModel().rightHand;

        // Apply the hand's transformations
        poseStack.pushPose();
        rightHand.translateAndRotate(poseStack);
        poseStack.scale(0.0625f, 0.0625f, 0.0625f); // Scale to block units

        // Render the GeckoLib hand model
        this.handModel.render(
                poseStack,
                buffer.getBuffer(RenderType.entityCutout(CustomHandModel.TEXTURE)),
                packedLight,
                OverlayTexture.NO_OVERLAY,
                1.0f, 1.0f, 1.0f, 1.0f
        );

        poseStack.popPose();
    }
}