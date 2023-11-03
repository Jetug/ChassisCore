package com.jetug.chassis_core.mixin.client;

import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.Boat;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin implements ClientGamePacketListener {
    @Final
    @Shadow private Minecraft minecraft;
    @Shadow private ClientLevel level;
    @Final
    @Shadow private static Logger LOGGER;

    @Inject(method = "handleSetEntityPassengersPacket", at = @At("HEAD"), cancellable = true)
    public void handleSetEntityPassengersPacket(ClientboundSetPassengersPacket pPacket, CallbackInfo ci) {
        PacketUtils.ensureRunningOnSameThread(pPacket, this, this.minecraft);
        Entity entity = this.level.getEntity(pPacket.getVehicle());

        if (entity == null) {
            LOGGER.warn("Received passengers for unknown entity");
        } else {
            boolean flag = entity.hasIndirectPassenger(this.minecraft.player);
            entity.ejectPassengers();

            for(int i : pPacket.getPassengers()) {
                Entity entity1 = this.level.getEntity(i);
                if (entity1 != null) {
                    entity1.startRiding(entity, true);
                    if (entity1 == this.minecraft.player && !flag) {
                        if (entity instanceof Boat) {
                            this.minecraft.player.yRotO = entity.getYRot();
                            this.minecraft.player.setYRot(entity.getYRot());
                            this.minecraft.player.setYHeadRot(entity.getYRot());
                        }
                        if(!(entity instanceof WearableChassis))
                            this.minecraft.gui.setOverlayMessage(new TranslatableComponent("mount.onboard", this.minecraft.options.keyShift.getTranslatedKeyMessage()), false);
                    }
                }
            }
        }
        ci.cancel();
    }
}