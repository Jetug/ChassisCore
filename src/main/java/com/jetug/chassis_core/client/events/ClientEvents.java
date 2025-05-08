package com.jetug.chassis_core.client.events;

import com.jetug.chassis_core.ChassisCore;
import com.jetug.chassis_core.client.gui.hud.DebugHud;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ChassisCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {
     @SubscribeEvent
     public static void registerHud(RegisterGuiOverlaysEvent event){
         event.registerAboveAll("debug", DebugHud.DEBUG_HUD);
     }
}
