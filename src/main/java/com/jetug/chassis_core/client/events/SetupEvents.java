package com.jetug.chassis_core.client.events;

import com.jetug.chassis_core.ChassisCore;
import com.jetug.chassis_core.client.ClientConfig;
import com.jetug.chassis_core.client.input.LongClickController;
import com.jetug.chassis_core.client.input.DoubleClickController;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;

import static com.jetug.chassis_core.client.KeyBindings.*;
import static com.jetug.chassis_core.client.render.renderers.CustomHandRenderer.*;

@Mod.EventBusSubscriber(modid = ChassisCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class SetupEvents {
    @OnlyIn(Dist.CLIENT)
    public static final DoubleClickController DOUBLE_CLICK_CONTROLLER = new DoubleClickController();
    @OnlyIn(Dist.CLIENT)
    private static final LongClickController LONG_CLICK_CONTROLLER = new LongClickController();

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        for (KeyMapping key: getKeys()) ClientRegistry.registerKeyBinding(key);
        ClientConfig.modResourceManager.loadConfigs();
        registerClickListeners();
        registerHandRenderer();
    }

    private static void registerClickListeners() {
        DOUBLE_CLICK_CONTROLLER.addListener(InputEvents::onDoubleClick);
        LONG_CLICK_CONTROLLER.setRepeatListener(InputEvents::onLongClick);
        LONG_CLICK_CONTROLLER.setReleaseListener(InputEvents::onLongRelease);
    }
}