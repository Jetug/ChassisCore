package com.jetug.chassis_core.client.render.renderers;

import com.jetug.chassis_core.client.render.layers.HandEquipmentLayer;
import com.jetug.chassis_core.client.render.utils.GeoUtils;
import com.jetug.chassis_core.client.animators.HandAnimator;
import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import com.jetug.chassis_core.common.util.helpers.PlayerUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.azure.azurelib.cache.object.GeoBone;
import mod.azure.azurelib.cache.object.GeoCube;
import mod.azure.azurelib.model.GeoModel;
import mod.azure.azurelib.renderer.GeoObjectRenderer;
import mod.azure.azurelib.util.RenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.Objects;

import static com.jetug.chassis_core.client.events.InputEvents.*;
import static com.jetug.chassis_core.common.data.constants.ChassisPart.*;
import static com.jetug.chassis_core.common.foundation.entity.ChassisBase.*;
import static com.jetug.chassis_core.common.util.helpers.PlayerUtils.getLocalPlayerChassis;

public class CustomHandRenderer extends GeoObjectRenderer<HandAnimator> {
    public static final String RIGHT_HAND_BONE = "right_arm_pov";

    public CustomHandRenderer(GeoModel<HandAnimator> model) {
        super(model);
        addRenderLayer(new HandEquipmentLayer<>(this));
    }

    public void render(PoseStack poseStack, @Nullable MultiBufferSource bufferSource, int packedLight) {
        super.render(poseStack, getLocalPlayerChassis().getHandEntity(), bufferSource, null, null, packedLight);
    }

    @Override
    public void renderRecursively(PoseStack poseStack, HandAnimator animatable, GeoBone bone,
                                  RenderType renderType, MultiBufferSource bufferSource,
                                  VertexConsumer buffer, boolean isReRender, float partialTick,
                                  int packedLight, int packedOverlay,
                                  float red, float green, float blue, float alpha) {
        if(PlayerUtils.isLocalWearingChassis() && Objects.equals(bone.getName(), RIGHT_HAND_BONE)){
            var chassis = PlayerUtils.getLocalPlayerChassis();
            if(chassis.isEquipmentVisible(RIGHT_ARM_ARMOR)) {
                renderHand(poseStack, animatable, buffer, packedLight, packedOverlay, red, green, blue, alpha, chassis);
            }
        }
        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer,
                    isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    protected void renderHand(PoseStack poseStack, HandAnimator animatable, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, WearableChassis chassis) {
        var armor = getAsChassisEquipment(chassis.getEquipment(RIGHT_ARM_ARMOR));
        if (armor.getConfig() == null) return;
        var armorModel = armor.getConfig().getModel();
        var armorBone = GeoUtils.getBone(armorModel, "right_forearm_armor");
        if (armorBone == null) return;

        poseStack.pushPose();
        {
            var modelPose = animatable.getSecondaryBoneTransform();
            poseStack.mulPoseMatrix(modelPose.last().pose());
            poseStack.translate(X / 10D / 16D, Y / 10D / 16D,  Z / 10D / 16D);
            poseStack.translate(-82 / 10D / 16D, -220 / 10D / 16D,  30 / 10D / 16D);

            for (var cube : armorBone.getCubes()) {
                poseStack.pushPose();
                {
                    var newCube = new GeoCube(cube.quads(), new Vec3(0, 0, 0),
                            cube.rotation(), cube.size(), cube.inflate(), cube.mirror());
                    renderCube(poseStack, newCube, buffer, packedLight, packedOverlay, red, green, blue, alpha);
                }
                poseStack.popPose();
            }
        }
        poseStack.popPose();
    }

}
