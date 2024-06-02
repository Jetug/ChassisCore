package com.jetug.chassis_core.common.util.helpers;

import com.jetug.chassis_core.common.data.enums.ActionType;
import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

import static com.jetug.chassis_core.common.network.PacketSender.doServerAction;

public class PlayerUtils {
    public static void addEffect(Player player, MobEffect effect, int amplifier) {
        player.addEffect(new MobEffectInstance(effect, WearableChassis.EFFECT_DURATION, amplifier, false, false));
    }

    @OnlyIn(Dist.CLIENT)
    public static LocalPlayer getLocalPlayer() {
        return Minecraft.getInstance().player;
    }

    @OnlyIn(Dist.CLIENT)
    public static boolean isLocalWearingChassis() {
        return isWearingChassis(getLocalPlayer());
    }

    public static boolean isWearingChassis(Entity entity) {
        return entity != null && entity.getVehicle() instanceof WearableChassis;
    }

    @OnlyIn(Dist.CLIENT)
    public static WearableChassis getLocalPlayerChassis() {
        return getEntityChassis(getLocalPlayer());
    }

    @Nullable
    public static WearableChassis getEntityChassis(Entity entity) {
        if (entity.getVehicle() instanceof WearableChassis)
            return (WearableChassis) entity.getVehicle();
        else return null;
    }

    public static void stopWearingArmor(Player player) {
        if(PlayerUtils.isWearingChassis(player)) {
            player.stopRiding();
            doServerAction(ActionType.DISMOUNT);
            player.setInvisible(false);
        }
    }
}
