package com.jetug.chassis_core.common.foundation.registery;

import com.jetug.chassis_core.ChassisCore;
import com.jetug.chassis_core.common.foundation.container.menu.ChassisMenu;
import com.jetug.example.common.container.ExampleChassisMenu;
import com.jetug.example.common.container.ExampleChassisStationMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ContainerRegistry {
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister
            .create(ForgeRegistries.MENU_TYPES, ChassisCore.MOD_ID);

    public static final RegistryObject<MenuType<ChassisMenu>> CHASSIS_MENU
            = registerMenuType("chassis_menu", ChassisMenu::new);

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return CONTAINERS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        CONTAINERS.register(eventBus);
    }
}
