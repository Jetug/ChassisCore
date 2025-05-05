package com.jetug.chassis_core.common.network.packet;

import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import com.jetug.chassis_core.common.network.data.ArmorData;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CCassisPacket {
    public CompoundTag nbt = null;

    public S2CCassisPacket() {}

    public S2CCassisPacket(ArmorData armorData) {
        nbt = armorData.serializeNBT();
    }

    public S2CCassisPacket(CompoundTag armorData) {
        nbt = armorData;
    }

    public static void write(S2CCassisPacket message, FriendlyByteBuf buffer) {
        buffer.writeNbt(message.nbt);
    }

    public static S2CCassisPacket read(FriendlyByteBuf buffer) {
        return new S2CCassisPacket(buffer.readNbt());
    }

    public static void handle(S2CCassisPacket message, Supplier<NetworkEvent.Context> context) {
        var player = Minecraft.getInstance().player;
        var data = message.getArmorData();
        if(player == null) return;
        var entity = player.level().getEntity(data.entityId);

        if (entity instanceof WearableChassis powerArmor)
            powerArmor.setArmorData(data);
    }

    public ArmorData getArmorData() {
        var data = new ArmorData();
        data.deserializeNBT(nbt);
        return data;
    }
}