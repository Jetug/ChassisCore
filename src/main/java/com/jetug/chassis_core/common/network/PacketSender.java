package com.jetug.chassis_core.common.network;

import com.jetug.chassis_core.common.data.enums.*;
import com.jetug.chassis_core.common.network.actions.*;
import com.jetug.chassis_core.common.network.packet.*;

@SuppressWarnings("rawtypes")
public class PacketSender {
    public static void doServerAction(ActionType action) {
        PacketHandler.getPlayChannel().sendToServer(new S2CActionPacket(action));
    }

    public static void doServerAction(Action action, int entityId) {
        PacketHandler.getPlayChannel().sendToServer(new C2SGenericPacket(entityId, action));
    }
}
