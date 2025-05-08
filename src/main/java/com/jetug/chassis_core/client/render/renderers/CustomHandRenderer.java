package com.jetug.chassis_core.client.render.renderers;

import com.jetug.chassis_core.client.model.HandModel;
import com.jetug.chassis_core.client.render.layers.HandEquipmentLayer;
import com.jetug.chassis_core.client.render.utils.GeoUtils;
import com.jetug.chassis_core.common.foundation.entity.HandEntity;
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

import java.util.Objects;

import static com.jetug.chassis_core.common.data.constants.ChassisPart.*;
import static com.jetug.chassis_core.common.foundation.entity.ChassisBase.*;
import static com.jetug.chassis_core.common.util.helpers.PlayerUtils.getLocalPlayerChassis;

public class CustomHandRenderer extends GeoObjectRenderer<HandEntity> {
    public static final String RIGHT_HAND_BONE = "right_arm_pov";

    public CustomHandRenderer(GeoModel<HandEntity> model) {
        super(model);
        addRenderLayer(new HandEquipmentLayer<>(this));
    }

    public void render(PoseStack poseStack, @Nullable MultiBufferSource bufferSource, int packedLight) {
        super.render(poseStack, getLocalPlayerChassis().getHandEntity(), bufferSource, null, null, packedLight);
    }

    @Override
    public void renderRecursively(PoseStack poseStack, HandEntity animatable, GeoBone bone,
                                  RenderType renderType, MultiBufferSource bufferSource,
                                  VertexConsumer buffer, boolean isReRender, float partialTick,
                                  int packedLight, int packedOverlay,
                                  float red, float green, float blue, float alpha) {
        if(PlayerUtils.isLocalWearingChassis() && Objects.equals(bone.getName(), RIGHT_HAND_BONE)){
            var chassis = PlayerUtils.getLocalPlayerChassis();
            if(chassis.isEquipmentVisible(RIGHT_ARM_ARMOR)){
                var armor = getAsChassisEquipment(chassis.getEquipment(RIGHT_ARM_ARMOR));
                if(armor.getConfig() == null) return;
                var armorBone = GeoUtils.getBone(armor.getConfig().getModel(), "right_forearm_armor");
                if(armorBone == null) return;

                poseStack.pushPose();
                {
                    RenderUtils.rotateMatrixAroundBone(poseStack, bone);
                    poseStack.translate(
                            ( armorBone.getPivotX() + 0.8 ) / 16f,
                            (-armorBone.getPivotY() + 0.35) / 16f,
                            ( armorBone.getPivotZ() - 1.05) / 16f);

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
        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer,
                    isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
