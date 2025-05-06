package com.jetug.chassis_core.common.network.packet;

import com.jetug.chassis_core.common.data.enums.ActionType;
import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import com.mrcrayfish.framework.api.network.MessageContext;
import com.mrcrayfish.framework.api.network.message.PlayMessage;
import net.minecraft.network.FriendlyByteBuf;

import static com.jetug.chassis_core.common.util.helpers.PlayerUtils.isWearingChassis;

@SuppressWarnings("ConstantConditions")
public class S2CActionPacket extends PlayMessage<S2CActionPacket> {
    ActionType action = null;

    public S2CActionPacket(ActionType action) {
        this.action = action;
    }

    public S2CActionPacket() {}

    @Override
    public void encode(S2CActionPacket actionPacket, FriendlyByteBuf buffer) {
        buffer.writeByte(actionPacket.action.getId());
    }

    @Override
    public S2CActionPacket decode(FriendlyByteBuf buffer) {
        var action = ActionType.getById(buffer.readByte());
        return new S2CActionPacket(action);
    }

    @Override
    public void handle(S2CActionPacket message, MessageContext supplier) {
        supplier.execute((() ->
        {
            var player = supplier.getPlayer();
            if (!isWearingChassis(player)) return;
            var armor = (WearableChassis) player.getVehicle();

            switch (message.action) {
                case DISMOUNT -> armor.exitArmor();
                case OPEN_GUI -> armor.openGUI(player);
            }

        }));
        supplier.setHandled(true);


    }
}