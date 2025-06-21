package com.jetug.chassis_core.common.network;

import com.jetug.chassis_core.ChassisCore;
import com.jetug.chassis_core.common.network.packet.*;
import com.mrcrayfish.framework.api.FrameworkAPI;
import com.mrcrayfish.framework.api.network.FrameworkNetwork;
import com.mrcrayfish.framework.api.network.MessageDirection;
import net.minecraft.resources.ResourceLocation;

public class PacketHandler {
    private static FrameworkNetwork PLAY_CHANNEL;

    public static FrameworkNetwork getPlayChannel() {
        return PLAY_CHANNEL;
    }

    public static void register() {
        PLAY_CHANNEL = FrameworkAPI.createNetworkBuilder(new ResourceLocation(ChassisCore.MOD_ID, "play"), 1)
                .registerPlayMessage(S2CActionPacket.class, MessageDirection.PLAY_SERVER_BOUND)
                .registerPlayMessage(C2SGenericPacket.class, MessageDirection.PLAY_SERVER_BOUND)
                .registerPlayMessage(S2CInventoryPacket.class, MessageDirection.PLAY_CLIENT_BOUND)
                .build();
    }
}
