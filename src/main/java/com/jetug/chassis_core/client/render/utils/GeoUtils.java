package com.jetug.chassis_core.client.render.utils;

import com.jetug.chassis_core.common.foundation.entity.*;
import mod.azure.azurelib.cache.AzureLibCache;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.cache.object.GeoBone;
import mod.azure.azurelib.constant.DataTickets;
import mod.azure.azurelib.core.animation.AnimationProcessor;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.model.data.EntityModelData;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.*;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import java.util.*;

@SuppressWarnings({"rawtypes"})
public class GeoUtils {
    @Nullable
    public static Collection<GeoBone> getEquipmentBones(String boneName, WearableChassis animatable) {
        var result = new ArrayList<GeoBone>();
        var configs = animatable.getItemConfigs();
        for (var config: configs) {
            var boneNames = config.getArmorBone(boneName);

            for (var name : boneNames) {
                var armorBone = GeoUtils.getArmorBone(config.getModelLocation(), name);
                if(armorBone != null) result.add(armorBone);
            }
        }
        return result;
    }
    public static void setHeadAnimation(LivingEntity animatable, AnimationProcessor animationProcessor, AnimationState animationState) {
        var head = animationProcessor.getBone("head");
        var partialTick = Minecraft.getInstance().getFrameTime();

        var data = (EntityModelData)animationState.getExtraData().get(DataTickets.ENTITY_MODEL_DATA);

//        float lerpBodyRot = Mth.rotLerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
//        float lerpHeadRot = Mth.rotLerp(partialTick, animatable.yHeadRotO, animatable.yHeadRot);
//        float headPitch = Mth.lerp(partialTick, animatable.xRotO, animatable.getXRot());
//        float netHeadYaw = lerpHeadRot - lerpBodyRot;

        head.setRotX(data.headPitch()  * ((float) Math.PI / 180F));
        head.setRotY(data.netHeadYaw() * ((float) Math.PI / 180F));
    }

    public static BakedGeoModel getModel(ResourceLocation location){
        return AzureLibCache.getBakedModels().get(location);
    }

    @Nullable
    public static GeoBone getFrameBone(AnimatedGeoModel provider, String name){
        return (GeoBone) provider.getAnimationProcessor().getBone(name);
    }

    @Nullable
    public static GeoBone getArmorBone(ResourceLocation resourceLocation, String name){
        var model = getModel(resourceLocation);
        return model == null ? null : model.getBone(name).orElse(null);
    }
}
