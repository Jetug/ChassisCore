package com.jetug.chassis_core.client.render.layers;

import com.ibm.icu.impl.Pair;
import com.jetug.chassis_core.ChassisCore;
import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import com.jetug.chassis_core.common.util.helpers.texture.PlayerSkins;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.renderer.GeoRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.Level;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import static com.jetug.chassis_core.common.util.helpers.BufferedImageHelper.*;
import static com.jetug.chassis_core.common.util.helpers.TextureHelper.*;

public class ScaledPlayerSkinLayer<T extends WearableChassis> extends LayerBase<T> {
    private static final HashMap<Pair<UUID, Pair<Integer, Integer>>, ResourceLocation> playerSkins = new HashMap<>();
    private final PlayerSkinStorage storage = PlayerSkinStorage.INSTANCE;
    private Pair<Integer, Integer> size = null;

    public ScaledPlayerSkinLayer(GeoRenderer<T> entityRenderer) {
        super(entityRenderer);
    }

    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType,
                       MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (size == null) this.size = getTextureSize(getRenderer().getTextureLocation(animatable));
        if(animatable.hasPlayerPassenger()) {
            var texture = getPlayerSkin(animatable.getPlayerPassenger(), animatable);

            if(!animatable.isInvisible() && texture != null) {
                renderLayer(poseStack, animatable, bakedModel, bufferSource, partialTick, packedLight, texture);
            }
        }
    }

    @Nullable
    private ResourceLocation getPlayerSkin(Player player, T animatable) {
        return storage.getSkin(player, size);
    }
}