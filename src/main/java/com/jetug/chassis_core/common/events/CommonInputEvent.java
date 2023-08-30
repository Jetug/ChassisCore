package com.jetug.chassis_core.common.events;

import com.jetug.chassis_core.common.input.InputKey;
import com.jetug.chassis_core.common.input.KeyAction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

public class CommonInputEvent extends Event {
    private final InputKey key;
    private final KeyAction action;
    private final Player player;

    public CommonInputEvent(InputKey key, KeyAction action, Player player) {
        this.key = key;
        this.action = action;
        this.player = player;
    }

    public InputKey getKey() {
        return key;
    }

    public KeyAction getAction() {
        return action;
    }

    public Player getPlayer() {
        return player;
    }
}