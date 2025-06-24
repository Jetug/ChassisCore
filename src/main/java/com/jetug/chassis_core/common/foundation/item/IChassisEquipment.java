package com.jetug.chassis_core.common.foundation.item;

import com.jetug.chassis_core.common.config.EquipmentConfig;
import com.jetug.chassis_core.common.network.managers.ConfigSupplier;

import javax.annotation.Nullable;

public interface IChassisEquipment {
    EquipmentConfig getConfig();

    void setConfig(ConfigSupplier<EquipmentConfig> config);
}
