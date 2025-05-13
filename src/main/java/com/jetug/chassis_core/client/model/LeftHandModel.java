package com.jetug.chassis_core.client.model;

import com.jetug.chassis_core.client.animators.HandAnimator;
import com.jetug.chassis_core.client.render.utils.GeoUtils;
import com.jetug.chassis_core.common.util.helpers.PlayerUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.model.GeoModel;
import mod.azure.azurelib.util.RenderUtils;
import net.minecraft.resources.ResourceLocation;

import static com.jetug.chassis_core.client.render.renderers.CustomHandRenderer.LEFT_HAND_BONE;
import static com.jetug.chassis_core.client.render.renderers.CustomHandRenderer.RIGHT_HAND_BONE;
import static com.jetug.chassis_core.client.render.utils.ResourceHelper.getChassisResource;
import static com.jetug.chassis_core.common.data.constants.ChassisPart.LEFT_ARM_ARMOR;
import static com.jetug.chassis_core.common.data.constants.ChassisPart.RIGHT_ARM_ARMOR;
import static com.jetug.chassis_core.common.foundation.entity.ChassisBase.getAsChassisEquipment;

public class LeftHandModel extends GeoModel<HandAnimator> {
    @Override
    public ResourceLocation getModelResource(HandAnimator geoAnimatable) {
        return getChassisResource("geo/hand/", "_left_hand.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HandAnimator geoAnimatable) {
        return getChassisResource("textures/hand/", "_left_hand.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HandAnimator geoAnimatable) {
        return getChassisResource("animations/hand/", "_left_hand.animation.json");
    }
}
