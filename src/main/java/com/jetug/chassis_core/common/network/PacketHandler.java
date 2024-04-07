package com.jetug.chassis_core.common.network;

import com.jetug.chassis_core.ChassisCore;
import com.jetug.chassis_core.common.network.packet.ActionPacket;
import com.jetug.chassis_core.common.network.packet.C2SChassisPacket;
import com.jetug.chassis_core.common.network.packet.GenericPacket;
import com.jetug.chassis_core.common.network.packet.S2CCassisPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.List;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = Integer.toString(1);
    private static final SimpleChannel HANDLER = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(ChassisCore.MOD_ID, "network"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
    private static int disc = 0;

    public static void register() {
        HANDLER.registerMessage(disc++, ActionPacket.class, ActionPacket::write, ActionPacket::read, ActionPacket::handle);
        HANDLER.registerMessage(disc++, S2CCassisPacket.class, S2CCassisPacket::write, S2CCassisPacket::read, S2CCassisPacket::handle);
        HANDLER.registerMessage(disc++, GenericPacket.class, GenericPacket::write, GenericPacket::read, GenericPacket::handle);
        HANDLER.registerMessage(disc++, C2SChassisPacket.class, C2SChassisPacket::write, C2SChassisPacket::read, C2SChassisPacket::handle);
//		HANDLER.registerMessage(disc++, InventoryPacket.class  , InventoryPacket::write	 , InventoryPacket::read , InventoryPacket::handle	);
    }

    public static void sendToServer(Object msg) {
        HANDLER.sendToServer(msg);
    }

    public static void sendTo(Object msg, ServerPlayer player) {
        if (!(player instanceof FakePlayer)) {
            HANDLER.sendTo(msg, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        }
    }

    public static void sendToAllPlayers(Object msg) {
        var server = ServerLifecycleHooks.getCurrentServer();
        List<ServerPlayer> list = server.getPlayerList().getPlayers();
        for (ServerPlayer e : list) {
            sendTo(msg, e);
        }
    }
}
