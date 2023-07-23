package com.jetug.power_armor_mod.common.events;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.eventbus.api.Event;

public class ContainerChangedEvent extends Event {

    private final Entity entity;

    public ContainerChangedEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}