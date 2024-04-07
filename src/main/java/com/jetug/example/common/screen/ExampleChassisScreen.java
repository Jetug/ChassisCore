package com.jetug.example.common.screen;

import com.jetug.chassis_core.ChassisCore;
import com.jetug.chassis_core.client.gui.screen.ChassisScreen;
import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import com.jetug.example.common.container.ExampleChassisMenu;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ExampleChassisScreen extends ChassisScreen<ExampleChassisMenu> {
    public static final ResourceLocation POWER_ARMOR_GUI =
            new ResourceLocation(ChassisCore.MOD_ID, "textures/screens/example_chassis/example_chassis_inventory.png");
    public ExampleChassisScreen(ExampleChassisMenu container, Inventory inventory, Component name) {
        super(container, inventory, name, POWER_ARMOR_GUI);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderEntity(WearableChassis powerArmor) {
        float scale = 1.0F / Math.max(1.0E-4F, powerArmor.getScale());
        InventoryScreen.renderEntityInInventory(this.leftPos + 32, this.topPos + 73, (int)(scale * 23.0F),
                (float)(this.leftPos + 51) - this.mousePosX, (float)(this.topPos + 75 - 50) - this.mousePosY, powerArmor);
    }
}