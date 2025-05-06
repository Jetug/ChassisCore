package com.jetug.chassis_core.common.network;

import com.jetug.chassis_core.ChassisCore;
import com.jetug.chassis_core.common.network.packet.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;
import java.util.function.Supplier;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    private static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(ChassisCore.MOD_ID, "main_channel"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
    private static int disc = 0;

    public static void register() {
        CHANNEL.registerMessage(disc++, S2CInventoryPacket.class, S2CInventoryPacket::write, S2CInventoryPacket::read, S2CInventoryPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(disc++, C2SInventoryPacket.class, C2SInventoryPacket::write, C2SInventoryPacket::read, C2SInventoryPacket::handle);
        CHANNEL.registerMessage(disc++, ActionPacket.class, ActionPacket::write, ActionPacket::read, ActionPacket::handle);
        CHANNEL.registerMessage(disc++, GenericPacket.class, GenericPacket::write, GenericPacket::read, GenericPacket::handle);
    }

    public static void sendToServer(Object msg) {
        CHANNEL.sendToServer(msg);
    }

    public static void sendTo(Object msg, ServerPlayer player) {
        CHANNEL.sendTo(msg, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToAllPlayers(Object message) {
        CHANNEL.send(PacketDistributor.ALL.noArg(), message);
    }

    public static void sendToTrackingEntity(Supplier<Entity> supplier, Object message) {
        CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(supplier), message);
    }

//    public static void sendToAllPlayers(Object msg) {
//        var server = ServerLifecycleHooks.getCurrentServer();
//        if (server == null) return;
//
//        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
//            sendTo(msg, player);
//        }
//    }
}
