package com.jetug.chassis_core.common.network.data;

import com.jetug.chassis_core.common.network.*;
import com.jetug.chassis_core.common.network.packet.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;

public class ArmorData {
    public static final String INVENTORY = "inventory";
    public static final String HEAT = "Heat";
    public static final String ID = "ID";
    public static final String ATTACK_CHARGE = "AttackCharge";

    public int entityId = -1;
    public ListTag inventory;
    public int heat;
    public int attackCharge;

    public ArmorData() {
    }

    public ArmorData(int entityId) {
        this.entityId = entityId;
    }

    public CompoundTag serializeNBT() {
        var nbt = new CompoundTag();
        nbt.putInt(ID, entityId);
        nbt.put(INVENTORY, inventory);
        nbt.putInt(HEAT, heat);
        nbt.putInt(ATTACK_CHARGE, attackCharge);

        return nbt;
    }

    public void deserializeNBT(CompoundTag nbt) {
        entityId = nbt.getInt(ID);
        inventory = (ListTag) nbt.get(INVENTORY);
        heat = nbt.getInt(HEAT);
        attackCharge = nbt.getInt(ATTACK_CHARGE);
    }

//    public void sentToClientPlayer(ServerPlayer player) {
//        PacketHandler.sendTo(new S2CCassisPacket(this), player);
//    }

    public void sentToServer() {
        PacketHandler.sendToServer(new C2SChassisPacket(this));
    }

    public void sentToClient() {
        PacketHandler.sendToAllPlayers(new S2CCassisPacket(this));
    }

    public boolean syncWithAll() {
        return false;
    }
}
