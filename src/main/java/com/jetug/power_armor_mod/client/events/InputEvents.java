package com.jetug.power_armor_mod.client.events;

import com.jetug.power_armor_mod.common.minecraft.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.util.enums.DashDirection;
import com.jetug.power_armor_mod.common.util.helpers.DoubleClickHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.Level;
import org.lwjgl.glfw.GLFW;

import static com.jetug.power_armor_mod.client.KeyBindings.LEAVE;
import static com.jetug.power_armor_mod.common.util.constants.Global.LOGGER;
import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.isWearingPowerArmor;
import static java.lang.System.out;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class InputEvents {
    static DoubleClickHelper doubleClickHelper = new DoubleClickHelper();
    
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent()
    public static void onKeyInput(InputEvent.KeyInputEvent event)
    {
        var minecraft = Minecraft.getInstance();
        var player = minecraft.player;
        var options = minecraft.options;

        if (player != null && isWearingPowerArmor(player)) {
            var entity = (PowerArmorEntity)player.getVehicle();
            assert entity != null;

            if (options.keyJump.isDown()) entity.jump();
            if (options.keyShift.isDown()) options.keyShift.setDown(false);

            if (event.getAction() == GLFW.GLFW_PRESS) {
                if (doubleClickHelper.isDoubleClick(event.getKey())) onDoubleClick(entity);
                if (LEAVE.isDown()) player.stopRiding();
            }
        }
    }

    private static void onDoubleClick(Entity entity){

        var options = Minecraft.getInstance().options;

        if (options.keyUp.isDown()) {
            ((PowerArmorEntity) entity).dash(DashDirection.FORWARD);
        }

        if (options.keyDown.isDown()) {
            ((PowerArmorEntity) entity).dash(DashDirection.BACK);
        }

        if (options.keyLeft.isDown()) {
            ((PowerArmorEntity) entity).dash(DashDirection.LEFT);
        }

        if (options.keyRight.isDown()) {
            ((PowerArmorEntity) entity).dash(DashDirection.RIGHT);
        }

        if (options.keyJump.isDown()) {
            ((PowerArmorEntity) entity).dash(DashDirection.UP);
        }
    }

    private static void onDash(Entity entity){
        Options options = Minecraft.getInstance().options;

        if (options.keyDown.isDown()){
            ((PowerArmorEntity)entity).dash(DashDirection.BACK);
        }
        else if (options.keyRight.isDown()){
            ((PowerArmorEntity)entity).dash(DashDirection.RIGHT);
        }
        else if (options.keyLeft.isDown()){
            ((PowerArmorEntity)entity).dash(DashDirection.LEFT);
        }
        else {
            ((PowerArmorEntity) entity).dash(DashDirection.FORWARD);
        }
        out.println("Dash!");
    }

    
}
