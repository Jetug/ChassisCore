package com.jetug.chassis_core.common.network.managers;

import com.jetug.chassis_core.common.config.ChassisConfig;
import com.jetug.chassis_core.common.foundation.entity.Chassis;
import net.minecraft.world.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

public class Configs {
    public static final Map<EntityType<Chassis>, ConfigSupplier<ChassisConfig>> CHASSIS_CONFIGS = new HashMap<>();
}
