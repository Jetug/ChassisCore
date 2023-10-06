package com.jetug.chassis_core.client.model;

import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animation.AnimationProcessor;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import static com.jetug.chassis_core.client.render.utils.GeoUtils.*;
import static com.jetug.chassis_core.client.render.utils.ResourceHelper.*;
import static com.jetug.chassis_core.common.data.constants.Bones.*;
import static com.jetug.chassis_core.common.data.constants.Resources.*;


@SuppressWarnings({"rawtypes", "unchecked"})
public class ChassisModel<Type extends WearableChassis> extends GeoModel<Type>
{
    public ChassisModel(){
        super();
    }

    @Override
    public ResourceLocation getModelResource(Type object) {
        if(object == null) return resourceLocation("geo/error.geo.json");
        return getChassisResource(object,"geo/chassis/", ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Type object) {
        if(object == null) return resourceLocation("textures/entity/error.png");
        return getChassisResource(object,"textures/entity/", ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(Type object) {
        if(object == null) return resourceLocation("animations/error.animation.json");
        return getChassisResource(object,"animations/", ".animation.json");
    }

    @Override
    public void setCustomAnimations(Type animatable, long instanceId, AnimationState<Type> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        setHeadAnimation(animatable, this.getAnimationProcessor(), animationState);
    }

//    @Override
//    public void setCustomAnimations(Type animatable, int instanceId, AnimationEvent animationEvent) {
//        super.setCustomAnimations(animatable, instanceId, animationEvent);
//        var head = this.getAnimationProcessor().getBone(HEAD_BONE_NAME);
//        if(head == null) return;
//        var extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
//        head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
//        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
//    }
}