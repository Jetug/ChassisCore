package com.jetug.chassis_core.mixin;

import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.jetug.chassis_core.common.util.helpers.PlayerUtils.*;

@Mixin(Mob.class)
public abstract class LivingEntityMixin {
    @Shadow(remap = false) private LivingEntity target;

    @Inject(method = "setTarget", at = @At("TAIL"), remap = false)
    public void setTarget(LivingEntity pTarget, CallbackInfo ci) {
        if(pTarget instanceof Player player && isWearingChassis(player)){
            target = (LivingEntity) player.getVehicle();
        }
        else if(pTarget instanceof WearableChassis powerArmor
                && (!powerArmor.hasPlayerPassenger() || powerArmor.getPlayerPassenger().isCreative())){
            target = null;
        }
    }
}
