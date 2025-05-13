package com.jetug.chassis_core;

import com.jetug.chassis_core.client.KeyBindings;
import com.jetug.chassis_core.client.events.SetupEvents;
import com.jetug.chassis_core.common.foundation.registery.ItemRegistry;
import com.jetug.chassis_core.common.network.PacketHandler;
import com.jetug.example.common.registery.ChassisArmorItems;
import com.jetug.example.common.registery.ContainerRegistry;
import com.jetug.example.common.registery.EntityTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

@Mod(ChassisCore.MOD_ID)
public class ChassisCore {
    public static final String MOD_ID = "chassis_core";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final IEventBus MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();

    public static boolean isDebugging() {
        return !FMLEnvironment.production;
    }

    public ChassisCore() {
        register();

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> MOD_EVENT_BUS.addListener(KeyBindings::register));
        EVENT_BUS.register(this);
        MOD_EVENT_BUS.addListener(this::onCommonSetup);
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        PacketHandler.register();
    }

    private void register() {
        ItemRegistry.register(MOD_EVENT_BUS);
        ContainerRegistry.register(MOD_EVENT_BUS);
        EntityTypes.register(MOD_EVENT_BUS);
        ChassisArmorItems.register(MOD_EVENT_BUS);
    }
}