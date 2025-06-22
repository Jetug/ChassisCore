package com.jetug.chassis_core.client.events;

import com.jetug.chassis_core.common.foundation.registery.*;
import com.jetug.chassis_core.client.gui.screen.*;
import net.minecraft.client.gui.screens.*;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.event.lifecycle.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModScreensEvent {
    @SubscribeEvent
    public static void clientLoad(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ContainerRegistry.CHASSIS_MENU.get(), DynamicChassisScreen::new);
        });
    }
}
