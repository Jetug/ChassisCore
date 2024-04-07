package com.jetug.chassis_core.common.foundation.registery;

import com.jetug.chassis_core.ChassisCore;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ChassisCore.MOD_ID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    private static <I extends Item> I registerItem(final String name, final Supplier<? extends I> sup) {
        return ITEMS.register(name, sup).get();
    }
}
