package com.jetug.power_armor_mod.common.util.extensions;

import com.jetug.power_armor_mod.common.foundation.entity.WearableChassis;
import com.jetug.power_armor_mod.common.data.enums.ActionType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

import static com.jetug.power_armor_mod.client.ClientConfig.*;
import static com.jetug.power_armor_mod.common.network.PacketSender.*;

public class PlayerExtension {

    public static boolean isWearingChassis(){
        return isWearingChassis(getLocalPlayer());
    }

    public static boolean isWearingChassis(Entity player){
        return player != null && player.getVehicle() instanceof WearableChassis;
    }

    public static WearableChassis getPlayerChassis(){
        return getPlayerChassis(getLocalPlayer());
    }

    @Nullable
    public static WearableChassis getPlayerChassis(Player player){
        if(player.getVehicle() instanceof WearableChassis)
            return (WearableChassis) player.getVehicle();
        else return null;
    }

    public static void stopWearingArmor(Player player) {
        player.stopRiding();
        doServerAction(ActionType.DISMOUNT);
        player.setInvisible(false);
    }

    public static void sendMessage(String text){
        var player = Minecraft.getInstance().player;
        try {
            player.sendMessage(new TextComponent(text), player.getUUID());
        }
        catch (Exception ignored) {}
    }
}
