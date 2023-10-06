package com.jetug.chassis_core.client.render.renderers;

import com.jetug.chassis_core.common.foundation.entity.HandEntity;
import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.cache.object.GeoBone;
import mod.azure.azurelib.constant.DataTickets;
import mod.azure.azurelib.core.animatable.GeoAnimatable;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.core.object.Color;
import mod.azure.azurelib.model.GeoModel;
import mod.azure.azurelib.model.data.EntityModelData;
import mod.azure.azurelib.renderer.GeoRenderer;
import mod.azure.azurelib.renderer.layer.GeoRenderLayer;
import mod.azure.azurelib.renderer.layer.GeoRenderLayersContainer;
import mod.azure.azurelib.util.RenderUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.util.EModelRenderCycle;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static com.jetug.chassis_core.common.util.helpers.PlayerUtils.getLocalPlayer;

public class CustomGeoRenderer<T extends GeoEntity > implements GeoRenderer<T> {
    protected MultiBufferSource rtb = null;
    public final GeoModel<T> model;
    protected final GeoRenderLayersContainer<T> renderLayers = new GeoRenderLayersContainer(this);
    protected final List<GeoLayerRenderer> layerRenderers = new ObjectArrayList<>();
    protected float scaleWidth = 1.0F;
    protected float scaleHeight = 1.0F;

    public CustomGeoRenderer(GeoModel<T> model){
        this.model = model;
    }

    public final boolean addLayer(GeoLayerRenderer layer) {
        return this.layerRenderers.add(layer);
    }

    public CustomGeoRenderer addRenderLayer(GeoRenderLayer<T> renderLayer) {
        this.renderLayers.addLayer(renderLayer);
        return this;
    }


    @Override
    public List<GeoRenderLayer<T>> getRenderLayers() {
        return this.renderLayers.getRenderLayers();
    }

//    public void setCurrentRTB(MultiBufferSource bufferSource) {
//        this.rtb = bufferSource;
//    }
//
//    public MultiBufferSource getCurrentRTB() {
//        return this.rtb;
//    }

    public GeoModel<T> getGeoModelProvider() {
        return model;
    }

    @Override
    public GeoModel<T> getGeoModel() {
        return model;
    }

    private T animatable;

    @Override
    public T getAnimatable() {
        return animatable;
    }

//    @Override
//    public ResourceLocation getTextureLocation(T animatable) {
//        return model.getTextureRec(animatable);
//    }

    @Override
    public void fireCompileRenderLayersEvent() {}

    @Override
    public boolean firePreRenderEvent(PoseStack poseStack, BakedGeoModel bakedGeoModel, MultiBufferSource multiBufferSource, float v, int i) {
        return false;
    }

    @Override
    public void firePostRenderEvent(PoseStack poseStack, BakedGeoModel bakedGeoModel, MultiBufferSource multiBufferSource, float v, int i) {}

    @Override
    public void updateAnimatedTextureFrame(T handEntity) {}

    public void render(T animatable, PoseStack poseStack, MultiBufferSource bufferSource, float partialTick, int packedLight) {
//        GeoRenderer.super.defaultRender(poseStack, animatable, bufferSource, renderType, buffer, 0, partialTick, packedLight);

        // var hand = animatable.getHandEntity();
        this.animatable = animatable;

        poseStack.pushPose();
        var renderColor = getRenderColor(animatable, partialTick, packedLight);
        var red = renderColor.getRedFloat();
        var green = renderColor.getGreenFloat();
        var blue = renderColor.getBlueFloat();
        var alpha = renderColor.getAlphaFloat();
        var packedOverlay = getPackedOverlay(animatable, 0.0F);

        var model = getGeoModel().getBakedModel(getGeoModel().getModelResource(animatable));
        var renderType = getRenderType(animatable, getTextureLocation(animatable), bufferSource, partialTick);
        var buffer = bufferSource.getBuffer(renderType);

        preRender(poseStack, animatable, model, bufferSource, buffer, false, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        if (firePreRenderEvent(poseStack, model, bufferSource, partialTick, packedLight)) {
            preApplyRenderLayers(poseStack, animatable, model, renderType, bufferSource, buffer, (float)packedLight, packedLight, packedOverlay);
            actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, false, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
            applyRenderLayers(poseStack, animatable, model, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
            postRender(poseStack, animatable, model, bufferSource, buffer, false, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
            firePostRenderEvent(poseStack, model, bufferSource, partialTick, packedLight);
        }

        poseStack.popPose();
    }

    protected Matrix4f entityRenderTranslations = new Matrix4f();
    protected Matrix4f modelRenderTranslations = new Matrix4f();

    protected float getDeathMaxRotation(T animatable) {
        return 90.0F;
    }

    public void actuallyRender(PoseStack poseStack, T animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        LivingEntity var10000;
        if (animatable instanceof LivingEntity entity) {
            var10000 = entity;
        } else {
            var10000 = null;
        }

        LivingEntity livingEntity = var10000;
        float lerpBodyRot = livingEntity == null ? 0.0F : Mth.rotLerp(partialTick, livingEntity.yBodyRotO, livingEntity.yBodyRot);
        float lerpHeadRot = livingEntity == null ? 0.0F : Mth.rotLerp(partialTick, livingEntity.yHeadRotO, livingEntity.yHeadRot);
        float netHeadYaw = lerpHeadRot - lerpBodyRot;
        float limbSwingAmount;
        limbSwingAmount = 0.0F;
        float limbSwing = 0.0F;

        if (!isReRender) {
            float motionThreshold = this.getMotionAnimThreshold(animatable);
            var animationState = new AnimationState(animatable, limbSwing, limbSwingAmount, partialTick,
                    0 >= motionThreshold && limbSwingAmount != 0.0F);
            long instanceId = this.getInstanceId(animatable);
            animationState.setData(DataTickets.TICK, ((GeoAnimatable)animatable).getTick(animatable));
            animationState.setData(DataTickets.ENTITY, animatable);
            Objects.requireNonNull(animationState);
            this.model.handleAnimations(animatable, instanceId, animationState);
        }

        poseStack.translate(0.0, 0.009999999776482582, 0.0);
        this.modelRenderTranslations = new Matrix4f(poseStack.last().pose());
        GeoRenderer.super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender,
                partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.popPose();
    }

    public void preRender(PoseStack poseStack, T animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.entityRenderTranslations = new Matrix4f(poseStack.last().pose());
        this.scaleModelForRender(this.scaleWidth, this.scaleHeight, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
    }

    public Vec3 getRenderOffset(T pEntity, float pPartialTicks) {
        return Vec3.ZERO;
    }

    public void renderRecursively(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        RenderUtils.translateMatrixToBone(poseStack, bone);
        RenderUtils.translateToPivotPoint(poseStack, bone);
        RenderUtils.rotateMatrixAroundBone(poseStack, bone);
        RenderUtils.scaleMatrixForBone(poseStack, bone);
        if (bone.isTrackingMatrices()) {
            Matrix4f poseState = poseStack.last().pose().copy();
            Matrix4f localMatrix = RenderUtils.invertAndMultiplyMatrices(poseState, this.entityRenderTranslations);
            bone.setModelSpaceMatrix(RenderUtils.invertAndMultiplyMatrices(poseState, this.modelRenderTranslations));
            localMatrix.translate(new Vector3f(this.getRenderOffset(this.animatable, 1.0F)));
            bone.setLocalSpaceMatrix(localMatrix);
            Matrix4f worldState = localMatrix.copy();
            worldState.translate(new Vector3f(getLocalPlayer().position()));
            bone.setWorldSpaceMatrix(worldState);
        }

        RenderUtils.translateAwayFromPivotPoint(poseStack, bone);
        this.renderCubesOfBone(poseStack, bone, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        if (!isReRender) {
            this.applyRenderLayersForBone(poseStack, animatable, bone, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
        }

        this.renderChildBones(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.popPose();
    }

//    public void render(WearableChassis chassisEntity, PoseStack poseStack,
//                       @Nullable MultiBufferSource bufferSource,
//                       int packedLight) {
//
//        this.baseRender(chassisEntity.getHandEntity(), poseStack, bufferSource, packedLight);
//        this.renderLayers(chassisEntity, poseStack, bufferSource, packedLight);
//    }
//
//    private void renderLayers(WearableChassis chassisEntity, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, int packedLight) {
//        var partialTick = Minecraft.getInstance().getFrameTime();
//        for (var layerRenderer : this.layerRenderers) {
//            renderLayer(poseStack, bufferSource, packedLight,
//                    chassisEntity, 0, 0, partialTick, 0,
//                    0, 0, layerRenderer);
//        }
//    }

//    protected void renderLayer(PoseStack poseStack, MultiBufferSource bufferSource,
//                               int packedLight, WearableChassis animatable,
//                               float limbSwing, float limbSwingAmount,
//                               float partialTick, float rotFloat, float netHeadYaw,
//                               float headPitch, GeoLayerRenderer layerRenderer) {
//        layerRenderer.render(poseStack, bufferSource, packedLight, animatable, limbSwing,
//                limbSwingAmount, partialTick, rotFloat,
//                netHeadYaw, headPitch);
//    }
//
//    private void baseRender(HandEntity animatable, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, int packedLight) {
//        var animationEvent = new AnimationEvent<>(animatable, 0, 0,
//                Minecraft.getInstance().getFrameTime(), false,
//                Collections.singletonList(new EntityModelData()));
//        setCurrentModelRenderCycle(EModelRenderCycle.INITIAL);
//        model.setCustomAnimations(animatable, getInstanceId(animatable), animationEvent);
//
//        var renderColor = getRenderColor(animatable, 0, poseStack, bufferSource, null, packedLight);
//        var renderType = getRenderType(animatable, 0, poseStack, bufferSource, null, packedLight,
//                getTextureLocation(animatable));
//
//        render(getModel(), animatable, 0, renderType, poseStack, bufferSource, null, packedLight, OverlayTexture.NO_OVERLAY,
//                renderColor.getRed() / 255f, renderColor.getGreen() / 255f, renderColor.getBlue() / 255f,
//                renderColor.getAlpha() / 255f);
//    }

//    public GeoModel getModel(){
//        return getGeoModelProvider().getModel(getGeoModelProvider().getModelLocation(null));
//    }
}
