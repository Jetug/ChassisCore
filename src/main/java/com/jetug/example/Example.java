package com.jetug.example;

import com.jetug.example.common.registery.ChassisArmorItems;
import com.jetug.example.common.registery.ContainerRegistry;
import com.jetug.example.common.registery.EntityTypes;
import net.minecraftforge.eventbus.api.IEventBus;

public class Example {


    public static void init(IEventBus modEventBus) {
        ContainerRegistry.register(modEventBus);
        EntityTypes.register(modEventBus);
        ChassisArmorItems.register(modEventBus);
    }
}
