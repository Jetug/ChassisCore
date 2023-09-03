package com.jetug.chassis_core.mixin;

import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Predicate;

import static com.jetug.chassis_core.common.util.helpers.PlayerUtils.*;

@Mixin(ProjectileUtil.class)
public class MixinProjectileUtil {
    @Unique
    private static final String getEntityHitResult = "getEntityHitResult(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;D)Lnet/minecraft/world/phys/EntityHitResult;";

    @ModifyVariable(method = getEntityHitResult, at = @At("HEAD"), ordinal = 0, argsOnly = true, remap = false)
    private static Predicate<Entity> getEntityHitResult(Predicate<Entity> filterMod, Entity entity, Vec3 eyePosition, Vec3 pickVector, AABB box,
                                                         Predicate<Entity> filter){

        var player = entity instanceof Player ? entity : Minecraft.getInstance().player;

        if(isWearingChassis(player))
        {
            return (levelEntity) ->{
                var isNotFrameEntity = !(levelEntity instanceof WearableChassis powerArmor && player.getVehicle() == powerArmor);
                return filterMod.test(levelEntity) && isNotFrameEntity;
            };
        }

        return filterMod;
    }
}
