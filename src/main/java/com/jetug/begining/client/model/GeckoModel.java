package com.jetug.begining.client.model;

import com.jetug.begining.ExampleMod;
import com.jetug.begining.common.entity.entity_type.GeckoEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class GeckoModel<T extends GeckoEntity> extends AnimatedGeoModel<GeckoEntity> {

    @Override
    public ResourceLocation getModelLocation(GeckoEntity object)
    {
        return new ResourceLocation(ExampleMod.MOD_ID, "geo/gecko_model.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(GeckoEntity object)
    {
        return new ResourceLocation(ExampleMod.MOD_ID, "textures/entities/gecko_model.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(GeckoEntity object)
    {
        return new ResourceLocation(ExampleMod.MOD_ID, "animations/gecko_model.animation.json");
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void setLivingAnimations(GeckoEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
    }
}
