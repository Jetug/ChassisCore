package com.jetug.chassis_core.client.model;

import com.jetug.chassis_core.client.render.utils.GeoUtils;
import com.jetug.chassis_core.client.animators.HandAnimator;
import com.jetug.chassis_core.common.util.helpers.PlayerUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.core.animatable.model.CoreGeoBone;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.model.GeoModel;
import mod.azure.azurelib.util.RenderUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import static com.jetug.chassis_core.client.render.renderers.CustomHandRenderer.RIGHT_HAND_BONE;
import static com.jetug.chassis_core.client.render.utils.ResourceHelper.getChassisResource;
import static com.jetug.chassis_core.common.data.constants.ChassisPart.RIGHT_ARM_ARMOR;
import static com.jetug.chassis_core.common.foundation.entity.ChassisBase.getAsChassisEquipment;

@SuppressWarnings("rawtypes")
public class HandModel extends GeoModel<HandAnimator> {
    @Override
    public ResourceLocation getModelResource(HandAnimator geoAnimatable) {
        return getChassisResource("geo/hand/", "_hand.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HandAnimator geoAnimatable) {
        return getChassisResource("textures/hand/", "_hand.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HandAnimator geoAnimatable) {
        return getChassisResource("animations/hand/", "_hand.animation.json");
    }

    @Override
    public void setCustomAnimations(HandAnimator animatable, long instanceId, AnimationState<HandAnimator> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        var chassis = PlayerUtils.getLocalPlayerChassis();
        if(chassis.isEquipmentVisible(RIGHT_ARM_ARMOR)) {
            var armor = getAsChassisEquipment(chassis.getEquipment(RIGHT_ARM_ARMOR));
            if (armor.getConfig() == null) return;
            var handAmor = GeoUtils.getBone(armor.getConfig().getModel(), "right_forearm_armor");
            var chassisHand = getAnimationProcessor().getBone(RIGHT_HAND_BONE);
            if (chassisHand != null && handAmor != null) {
                var poseStack = new PoseStack();
//                chassisHand.calculateLocalTransform(poseStack); // Apply parent hierarchy
                RenderUtils.prepMatrixForBone(poseStack, chassisHand);
//                poseStack.pushPose();
                animatable.setSecondaryBoneTransform(poseStack);
//                setPos(handAmor, getPos(chassisHand));
//                setRot(handAmor, getRot(chassisHand));
            }
        }
    }

    public Vec3 getRot(CoreGeoBone bone){
        return new Vec3(bone.getRotX(), bone.getRotY(), bone.getRotZ());
    }

    public Vec3 getPos(CoreGeoBone bone){
        return new Vec3(bone.getPosX(), bone.getPosY(), bone.getPosZ());
    }

    public void setRot(CoreGeoBone bone, Vec3 pos){
        bone.setRotX((float) pos.x);
        bone.setRotX((float) pos.y);
        bone.setRotX((float) pos.z);
    }

    public void setPos(CoreGeoBone bone, Vec3 pos){
        bone.setPosX((float) pos.x);
        bone.setPosY((float) pos.y);
        bone.setPosZ((float) pos.z);
    }
}
