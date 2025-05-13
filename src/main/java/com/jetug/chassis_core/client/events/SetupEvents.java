package com.jetug.chassis_core.client.events;

import com.jetug.chassis_core.ChassisCore;
import com.jetug.chassis_core.client.ClientConfig;
import com.jetug.chassis_core.client.KeyBindings;
import com.jetug.chassis_core.client.input.DoubleClickController;
import com.jetug.chassis_core.client.input.LongClickController;
import com.jetug.chassis_core.client.render.layers.CustomHandLayer;
import com.jetug.chassis_core.client.render.renderers.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


@Mod.EventBusSubscriber(modid = ChassisCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class SetupEvents {
    @OnlyIn(Dist.CLIENT)
    public static final DoubleClickController DOUBLE_CLICK_CONTROLLER = new DoubleClickController();
    @OnlyIn(Dist.CLIENT)
    private static final LongClickController LONG_CLICK_CONTROLLER = new LongClickController();

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        ClientConfig.modResourceManager.loadConfigs();
        registerClickListeners();

//        EntityRenderers.register(EntityType.PLAYER, (context) -> {
//            var renderer = new PlayerRenderer(context, false);
//            renderer.addLayer(new CustomHandLayer(renderer));
//            return renderer;
//        });
    }

    public static void registerHandlers() {
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(SetupEvents::registerPlayerLayer);
    }

    private static void registerPlayerLayer() {
        var renderManager = Minecraft.getInstance().getEntityRenderDispatcher();
        var skinMap = renderManager.getSkinMap();
        for (var renderer : skinMap.values()) {
            if (renderer instanceof LivingEntityRenderer livingEntityRenderer) {
                livingEntityRenderer.addLayer(new CustomHandLayer(livingEntityRenderer));
            }
        }
        renderManager.renderers.forEach((e, r) -> {
            if (r instanceof LivingEntityRenderer livingEntityRenderer) {
                livingEntityRenderer.addLayer(new CustomHandLayer(livingEntityRenderer));
            }
        });
    }

    private static void registerClickListeners() {
        DOUBLE_CLICK_CONTROLLER.addListener(InputEvents::onDoubleClick);
        LONG_CLICK_CONTROLLER.setRepeatListener(InputEvents::onLongClick);
        LONG_CLICK_CONTROLLER.setReleaseListener(InputEvents::onLongRelease);
    }
}