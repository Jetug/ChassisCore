package com.jetug.power_armor_mod.client.gui;

import com.jetug.power_armor_mod.common.util.constants.Global;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerRegistry {
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister
            .create(ForgeRegistries.CONTAINERS, Global.MOD_ID);

    public static final RegistryObject<MenuType<PowerArmorContainer>> DRAGON_CONTAINER
            = CONTAINERS.register("power_armor", () -> new MenuType<>(PowerArmorContainer::new));
}
