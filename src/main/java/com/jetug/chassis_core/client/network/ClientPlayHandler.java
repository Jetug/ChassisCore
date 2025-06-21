package com.jetug.chassis_core.client.network;

import com.jetug.chassis_core.common.network.managers.NetworkChassisManager;
import com.jetug.chassis_core.common.network.packet.S2CMessageUpdateChassisConfig;

public class ClientPlayHandler {
    public static void handleUpdateAmmo(S2CMessageUpdateChassisConfig message) {
        NetworkChassisManager.updateRegisteredAmmo(message);
    }
}
