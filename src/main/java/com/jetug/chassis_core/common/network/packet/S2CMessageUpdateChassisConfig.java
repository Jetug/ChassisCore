package com.jetug.chassis_core.common.network.packet;

import com.google.common.collect.ImmutableMap;
import com.jetug.chassis_core.common.config.ChassisConfig;
import com.jetug.chassis_core.common.network.managers.NetworkChassisManager;
import com.mrcrayfish.framework.api.network.MessageContext;
import com.mrcrayfish.framework.api.network.message.PlayMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.Validate;
import com.jetug.chassis_core.client.network.*;

/**
 * Author: MrCrayfish
 */
public class S2CMessageUpdateChassisConfig extends PlayMessage<S2CMessageUpdateChassisConfig> {
    private ImmutableMap<ResourceLocation, ChassisConfig> registeredGuns;

    public S2CMessageUpdateChassisConfig() {}

    @Override
    public void encode(S2CMessageUpdateChassisConfig message, FriendlyByteBuf buffer) {
        Validate.notNull(NetworkChassisManager.get());
        NetworkChassisManager.get().writeRegisteredAmmo(buffer);
    }

    @Override
    public S2CMessageUpdateChassisConfig decode(FriendlyByteBuf buffer) {
        S2CMessageUpdateChassisConfig message = new S2CMessageUpdateChassisConfig();
        message.registeredGuns = NetworkChassisManager.readRegisteredAmmo(buffer);
        return message;
    }

    @Override
    public void handle(S2CMessageUpdateChassisConfig message, MessageContext supplier) {
        supplier.execute((() -> ClientPlayHandler.handleUpdateAmmo(message)));
        supplier.setHandled(true);
    }

    public ImmutableMap<ResourceLocation, ChassisConfig> getRegisteredAmmo() {
        return this.registeredGuns;
    }
}
