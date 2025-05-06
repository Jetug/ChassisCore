package com.jetug.chassis_core.common.network.packet;

import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CInventoryPacket {
    public int chassisId = -1;
    public ListTag inventory;
    public static final String INVENTORY = "inventory";

    public S2CInventoryPacket() {}

    public S2CInventoryPacket(int chassisId, ListTag inventory) {
        this.chassisId = chassisId;
        this.inventory = inventory;
    }

    public static void write(S2CInventoryPacket message, FriendlyByteBuf buffer) {
        var nbt = new CompoundTag();
        nbt.put(INVENTORY, message.inventory);
        buffer.writeInt(message.chassisId);
        buffer.writeNbt(nbt);
    }

    public static S2CInventoryPacket read(FriendlyByteBuf buffer) {
        var entityId = buffer.readInt();
        var inventory = (ListTag) buffer.readNbt().get(INVENTORY);
        return new S2CInventoryPacket(entityId, inventory);
    }

    public static void handle(S2CInventoryPacket message, Supplier<NetworkEvent.Context> context) {
        var player = Minecraft.getInstance().player;
        if(player == null) return;
        var entity = player.level().getEntity(message.chassisId);

        if (entity instanceof WearableChassis powerArmor)
            powerArmor.setArmorData(message.inventory);
    }
}