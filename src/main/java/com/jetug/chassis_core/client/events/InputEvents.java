package com.jetug.chassis_core.client.events;

import com.jetug.chassis_core.ChassisCore;
import com.jetug.chassis_core.client.KeyBindings;
import com.jetug.chassis_core.client.utils.KeyUtils;
import com.jetug.chassis_core.common.input.CommonInputHandler;
import com.jetug.chassis_core.common.input.KeyAction;
import com.jetug.chassis_core.common.network.actions.InputAction;
import mod.azure.azurelib.core.math.functions.limit.Min;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import static com.jetug.chassis_core.client.ClientConfig.*;
import static com.jetug.chassis_core.client.KeyBindings.*;
import static com.jetug.chassis_core.common.network.PacketSender.doServerAction;
import static com.jetug.chassis_core.common.util.helpers.PlayerUtils.getLocalPlayer;
import static com.jetug.chassis_core.common.util.helpers.PlayerUtils.stopWearingArmor;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class InputEvents {
    public static int X = 0;
    public static int Y = 0;
    public static int Z = 0;
    public static boolean isHidden = false;

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent()
    public static void onKeyInput(InputEvent.Key event) {
        if (isNotInGame()) return;

        KeyAction action;
        if (event.getAction() == GLFW.GLFW_PRESS) {
            action = KeyAction.PRESS;
            if (event.getKey() == KeyBindings.LEAVE.getKey().getValue())
                stopWearingArmor(Minecraft.getInstance().player);
        } else if (event.getAction() == GLFW.GLFW_RELEASE)
            action = KeyAction.RELEASE;
        else
            action = KeyAction.REPEAT;

        handleInput(event.getKey(), action);
        handleDebugKeys(event);
        //CommonInputHandler.onKeyInput(InputKey.getByKey(event.getKey()), action, getLocalPlayer());
    }


    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent()
    public static void onMouseKeyInput(InputEvent.MouseButton event) {
        switch (event.getAction()) {
            case GLFW.GLFW_PRESS -> {

            }
            case GLFW.GLFW_RELEASE -> {
                if (event.getButton() != OPTIONS.keyUse.getKey().getValue() && isNotInGame()) return;
                handleInput(event.getButton(), KeyAction.RELEASE);
            }
        }
    }

    public static void onDoubleClick(InputEvent.Key event) {
        if (isNotInGame()) return;
        handleInput(event.getKey(), KeyAction.DOUBLE_CLICK);
    }

    public static void onLongClick(int key, int ticks) {
        if (isNotInGame()) return;
        handleInput(key, KeyAction.LONG_PRESS);
    }

    public static void onLongRelease(int key, int ticks) {
    }

    private static void handleInput(int key, KeyAction action) {
        var inputKey = KeyUtils.getByKey(key);
        if (getLocalPlayer() == null || inputKey == null) return;

        doServerAction(new InputAction(inputKey, action), -1);
        CommonInputHandler.onKeyInput(inputKey, action, getLocalPlayer());
    }

    public static boolean isNotInGame() {
        return Minecraft.getInstance().screen != null;
    }

    private static void handleDebugKeys(InputEvent.@NotNull Key event) {
        if (event.getAction() == GLFW.GLFW_PRESS || event.getAction() == GLFW.GLFW_REPEAT) {
            if (ChassisCore.isDebugging()) {
                int key = event.getKey();
                if (key == KEY_DEBUG_X_ADD.getKey().getValue()) {
                    X += 1;
                } else if (key == KEY_DEBUG_Y_ADD.getKey().getValue()) {
                    Y += 1;
                } else if (key == KEY_DEBUG_Z_ADD.getKey().getValue()) {
                    Z += 1;
                } else if (key == KEY_DEBUG_X_SUB.getKey().getValue()) {
                    X -= 1;
                } else if (key == KEY_DEBUG_Y_SUB.getKey().getValue()) {
                    Y -= 1;
                } else if (key == KEY_DEBUG_Z_SUB.getKey().getValue()) {
                    Z -= 1;
                } else if (key == KEY_DEBUG_SHOW.getKey().getValue()) {
                    isHidden = !isHidden;
                } else if (key == KEY_DEBUG_ZERO.getKey().getValue()) {
                    X = 0;
                    Y = 0;
                    Z = 0;
                }
            }
        }
    }
}
