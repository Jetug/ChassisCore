package com.jetug.power_armor_mod.common.network.packet;

import com.jetug.power_armor_mod.common.foundation.entity.WearableChassis;
import com.jetug.power_armor_mod.common.network.data.ArmorData;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PowerArmorPacket{
    public CompoundTag nbt = null;

    public PowerArmorPacket() {}

    public PowerArmorPacket(ArmorData armorData) {
        nbt = armorData.serializeNBT();
    }

    public PowerArmorPacket(CompoundTag armorData) {
        nbt = armorData;
    }

    public ArmorData getArmorData(){
        var data = new ArmorData();
        data.deserializeNBT(nbt);
        return data;
    }

    public static void write(PowerArmorPacket message, FriendlyByteBuf buffer) {
        buffer.writeNbt(message.nbt);
    }

    public static PowerArmorPacket read(FriendlyByteBuf buffer) {
        return new PowerArmorPacket(buffer.readNbt());
    }

    public static void handle(PowerArmorPacket message, Supplier<NetworkEvent.Context> context) {

        boolean isClientSide = context.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT || context.get().getDirection() == NetworkDirection.LOGIN_TO_CLIENT;
        boolean isServerSide = context.get().getDirection() == NetworkDirection.PLAY_TO_SERVER || context.get().getDirection() == NetworkDirection.LOGIN_TO_SERVER;

        Player player = null;

        if(isClientSide){
            player = Minecraft.getInstance().player;
        }
        else if(isServerSide) {
            player = context.get().getSender();
        }

        var data = message.getArmorData();
        var entity = player.level.getEntity(data.entityId);

        if(entity instanceof WearableChassis powerArmor)
            powerArmor.setArmorData(data);
    }
}