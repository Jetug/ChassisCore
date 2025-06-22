package com.jetug.example.client.screen;

import com.jetug.chassis_core.ChassisCore;
import com.jetug.chassis_core.client.gui.screen.ArmorStationScreen;
import com.jetug.example.common.container.ExampleChassisStationMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ExampleChassisStationScreen extends ArmorStationScreen<ExampleChassisStationMenu> {
    public ExampleChassisStationScreen(ExampleChassisStationMenu menu, Inventory pPlayerInventory, Component pTitle) {
        super(menu, pPlayerInventory, pTitle, new ResourceLocation(ChassisCore.MOD_ID, "textures/screens/example_chassis/example_chassis_station_gui.png"));
    }
}
