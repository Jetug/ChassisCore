package com.jetug.chassis_core.client.animators;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.NotNull;

public class HandAnimator implements GeoEntity {
    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);
    private PoseStack secondaryBoneTransform = new PoseStack();
    public LocalPlayer player;

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controllerName", 0, predicate()));
    }

    @NotNull
    protected AnimationController.AnimationStateHandler<HandAnimator> predicate() {
        return event -> PlayState.STOP;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public void setSecondaryBoneTransform(PoseStack poseStack) {
        this.secondaryBoneTransform = poseStack;
    }

    public PoseStack getSecondaryBoneTransform() {
        return this.secondaryBoneTransform;
    }
}