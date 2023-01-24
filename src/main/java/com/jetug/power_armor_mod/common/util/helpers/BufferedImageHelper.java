package com.jetug.power_armor_mod.common.util.helpers;

import com.jetug.power_armor_mod.common.util.constants.Global;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Level;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static com.jetug.power_armor_mod.common.util.helpers.TextureHelper.*;

public class BufferedImageHelper {
    @Nullable
    public static BufferedImage getPlayerHeadImage(AbstractClientPlayer clientPlayer) {
        var playerSkin = getPlayerSkinImage(clientPlayer);
        if(playerSkin == null) return null;
        cropImage(playerSkin, 32, 16);
        return playerSkin;
    }

    @Nullable
    private static BufferedImage getPlayerSkinImage(AbstractClientPlayer clientPlayer) {
        var skin = skinRequest(clientPlayer.getUUID());

        if(skin == null){
            var originalPlayerTexture = clientPlayer.getSkinTextureLocation();
            skin = resourceToBufferedImage(originalPlayerTexture);
        }

        return skin;
    }

//    public static ResourceLocation getMojangSkin(AbstractClientPlayer clientPlayer) {
//        var minecraft = Minecraft.getInstance();
//
//        var uuid = clientPlayer.getUUID();
//        var skin = skinRequest(clientPlayer.getUUID());
//    }

    public static ResourceLocation getMojangSkin2(AbstractClientPlayer clientPlayer) {
        var minecraft = Minecraft.getInstance();

        var uuid = clientPlayer.getUUID();
        //uuid = UUID.fromString("494036be-71f6-4b58-bb8d-a483a18a322f");
//        var playerInfo = minecraft.getConnection().getPlayerInfo(uuid);
//        var gameProfile = playerInfo.getProfile();
//        var map = minecraft.getSkinManager().getInsecureSkinInformation(gameProfile);

        var skin = skinRequest(clientPlayer.getUUID());



//        Map<Type, MinecraftProfileTexture> taxtures = null;
//        try {
//            var field = Minecraft.class.getDeclaredField("minecraftSessionService");
//            field.setAccessible(true);
//            var sess = (MinecraftSessionService)field.get(minecraft);
//            taxtures = sess.getTextures(gameProfile, false);
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }

//        if (map.containsKey(Type.SKIN)) {
//            final var skin = map.get(Type.SKIN);
//            try {
//
//                var url = new URL(skin.getUrl());
//                var image = ImageIO.read(url);
//                return createResource(image, clientPlayer.getUUID().toString());
//
//            } catch (Exception e) {
//                //return clientPlayer.getSkinTextureLocation();
//                throw new RuntimeException(e);
//            }
//        }
        return null;
    }


    @Nullable
    public static BufferedImage resourceToBufferedImage(ResourceLocation resourceLocation) {
        try {
            var resource = Minecraft.getInstance().getResourceManager().getResource(resourceLocation);
            var nativeImage = NativeImage.read(resource.getInputStream());
            var imageArr = nativeImage.asByteArray();
            return getImage(imageArr);
        }
        catch (IOException e) {
            Global.LOGGER.log(Level.ERROR, e);
            return null;
        }
    }

    public static NativeImage getNativeImage(BufferedImage img) {
        NativeImage nativeImage = new NativeImage(img.getWidth(), img.getHeight(), true);
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                int clr = img.getRGB(x, y);
                int alpha = (clr & 0xff000000) >> 24;
                int red =   (clr & 0x00ff0000) >> 16;
                int green = (clr & 0x0000ff00) >> 8;
                int blue =   clr & 0x000000ff;

                int rgb = alpha;
                rgb = (rgb << 8) + blue;
                rgb = (rgb << 8) + green;
                rgb = (rgb << 8) + red;

                nativeImage.setPixelRGBA(x, y, rgb);
            }
        }
        return nativeImage;
    }

    public static BufferedImage extendImage(BufferedImage image, int width, int height){
        var scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        addImage(scaledImage, image, 0, 0);
        return scaledImage;
    }

    public static void cropImage(BufferedImage img, int xPos, int yPos) {
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                if(x >= xPos || y >= yPos)
                    img.setRGB(x, y, (new Color(0.0f, 0.0f, 0.0f, 0.0f)).getRGB());
            }
        }
    }

    private static void addImage(BufferedImage buff1, BufferedImage buff2, int x, int y) {
        Graphics2D g2d = buff1.createGraphics();
        //g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
        g2d.drawImage(buff2, x, y, null);
        g2d.dispose();
    }

    public static BufferedImage getImage(byte[] imageData) {
        var bais = new ByteArrayInputStream(imageData);
        try {
            return ImageIO.read(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
