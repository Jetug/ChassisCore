package com.jetug.power_armor_mod;

import com.jetug.power_armor_mod.common.data.constants.Global;
import com.jetug.power_armor_mod.common.foundation.registery.*;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib3.GeckoLib;

import static com.jetug.power_armor_mod.common.data.constants.Global.MOD_EVENT_BUS;
import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

@Mod(Global.MOD_ID)
public class PowerArmorMod {

    public PowerArmorMod() {
        GeckoLib.initialize();
        register();
        EVENT_BUS.register(this);
    }

    private void register() {
        ContainerRegistry.register(MOD_EVENT_BUS);
        BlockEntityRegistry.register(MOD_EVENT_BUS);
        BlockRegistry.register(MOD_EVENT_BUS);
        EntityTypeRegistry.register(MOD_EVENT_BUS);
        ItemRegistry.register(MOD_EVENT_BUS);
        ParticleRegistry.register(MOD_EVENT_BUS);
    }
}