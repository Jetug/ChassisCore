package com.jetug.power_armor_mod.common.minecraft.item;

import com.jetug.power_armor_mod.common.minecraft.registery.ModItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {
    public static final CreativeModeTab MY_TAB = new CreativeModeTab("xxx") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.ARMOR_SPAWN_EGG.get());
        }
    };
}
