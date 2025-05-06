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

//    public static void register() {
//        CHANNEL.registerMessage(disc++, S2CInventoryPacket.class, S2CInventoryPacket::write, S2CInventoryPacket::read, S2CInventoryPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
////        CHANNEL.registerMessage(disc++, C2SInventoryPacket.class, C2SInventoryPacket::write, C2SInventoryPacket::read, C2SInventoryPacket::handle);
//        CHANNEL.registerMessage(disc++, S2CActionPacket.class, S2CActionPacket::write, S2CActionPacket::read, S2CActionPacket::handle);
//        CHANNEL.registerMessage(disc++, C2SGenericPacket.class, C2SGenericPacket::write, C2SGenericPacket::read, C2SGenericPacket::handle);
//    }
//
//    public static void sendToServer(Object msg) {
//        CHANNEL.sendToServer(msg);
//    }
//
//    public static void sendTo(Object msg, ServerPlayer player) {
//        CHANNEL.sendTo(msg, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
//    }
//
//    public static void sendToAllPlayers(Object message) {
//        CHANNEL.send(PacketDistributor.ALL.noArg(), message);
//    }
//
//    public static void sendToTrackingEntity(Supplier<Entity> supplier, Object message) {
//        CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(supplier), message);
//    }

//    public static void sendToAllPlayers(Object msg) {
//        var server = ServerLifecycleHooks.getCurrentServer();
//        if (server == null) return;
//
//        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
//            sendTo(msg, player);
//        }
//    }
}
