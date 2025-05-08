package com.jetug.chassis_core.client.gui.hud;

import com.jetug.chassis_core.ChassisCore;
import com.jetug.chassis_core.client.events.InputEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class DebugHud {
    private static final int BAR_OFFSET_X = 140;
    private static final int OFFSET_Y = 15;

    public static final IGuiOverlay DEBUG_HUD = ((gui, graphics, partialTick, width, height) -> {
        var minecraft = Minecraft.getInstance();
        if(InputEvents.isHidden || !ChassisCore.isDebugging() || minecraft.player == null) return;
        var x = 10;

        renderAmmoCounter(graphics, "x", InputEvents.X, x, OFFSET_Y);
        renderAmmoCounter(graphics, "y", InputEvents.Y, x, OFFSET_Y * 2);
        renderAmmoCounter(graphics, "z", InputEvents.Z, x, OFFSET_Y * 3);
    });

    private static void renderAmmoCounter(GuiGraphics graphics, String label, float val, int x, int y) {
        var text = label + ":" + val;
        graphics.drawString(Minecraft.getInstance().font, text, x, y, 0xFFFFFFFF, true);
    }
}