package com.jetug.chassis_core.common.data.constants;

import com.jetug.chassis_core.ChassisCore;
import net.minecraft.resources.ResourceLocation;

public class Resources {
    public static final ResourceLocation PLAYER_INVENTORY_TABS = resourceLocation("textures/gui/player_inventory_switch_tabs.png");
    public static final ResourceLocation PLAYER_INVENTORY_BOTTOM_TABS = resourceLocation("textures/gui/player_inventory_tabs_bottom.png");

    public static ResourceLocation resourceLocation(String location) {
        return new ResourceLocation(ChassisCore.MOD_ID, location);
    }
}
