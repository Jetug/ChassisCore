package com.jetug.power_armor_mod.common.minecraft.registery;

import com.jetug.power_armor_mod.common.minecraft.item.ModCreativeModeTab;
import com.jetug.power_armor_mod.common.minecraft.item.PowerArmorItem;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.jetug.power_armor_mod.common.minecraft.registery.ModEntityTypes.POWER_ARMOR;
import static com.jetug.power_armor_mod.common.util.constants.Global.MOD_ID;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<Item> ARMOR_SPAWN_EGG = ITEMS.register("armor_spawn_egg", () ->
            new ForgeSpawnEggItem(POWER_ARMOR, 0xDC8100, 0x4B4D4A,
                    new Item.Properties()
                            .tab(ModCreativeModeTab.MY_TAB)
                            .stacksTo(64) ));

    public static final RegistryObject<Item> PA_HELMET = ITEMS.register("pa_helmet", () ->
            new PowerArmorItem(BodyPart.HEAD, new Item.Properties()
                            .tab(ModCreativeModeTab.MY_TAB)
                            .durability(5)
            ));
    //tutor
    public static final RegistryObject<Item> CITRINE = ITEMS.register("citrine",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MY_TAB)));

    public static final RegistryObject<Item> RAW_CITRINE = ITEMS.register("raw_citrine",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MY_TAB)));

    public static final RegistryObject<Item> GEM_CUTTER_TOOL = ITEMS.register("gem_cutter_tool",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MY_TAB).durability(32)));



    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
