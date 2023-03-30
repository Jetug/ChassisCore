package com.jetug.power_armor_mod.client.model.item;

import com.jetug.power_armor_mod.common.foundation.item.HandItem;
import com.jetug.power_armor_mod.common.util.constants.Global;
import com.jetug.power_armor_mod.common.util.constants.Resources;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.jetug.power_armor_mod.common.util.constants.Resources.*;

@SuppressWarnings("rawtypes")
public class HandModel extends AnimatedGeoModel {
    public static final ResourceLocation HAND_MODEL_LOCATION = new ResourceLocation(Global.MOD_ID, "geo/hand.geo.json");

    @Override
    public ResourceLocation getModelLocation(Object object) {
        return HAND_MODEL_LOCATION;
    }

    @Override
    public ResourceLocation getTextureLocation(Object object) {
        return POWER_ARMOR_TEXTURE_LOCATION;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Object animatable) {
        return POWER_ARMOR_ANIMATION_LOCATION;
    }
}