package com.jetug.chassis_core.common.network.packet;

import com.jetug.chassis_core.common.data.enums.ActionType;
import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static com.jetug.chassis_core.common.util.helpers.PlayerUtils.isWearingChassis;

@SuppressWarnings("ConstantConditions")
public class ActionPacket {
    ActionType action = null;

    public ActionPacket(ActionType action) {
        this.action = action;
    }

    public ActionPacket() {
    }

    public static void write(ActionPacket message, FriendlyByteBuf buffer) {
        buffer.writeByte(message.action.getId());
    }

    public static ActionPacket read(FriendlyByteBuf buffer) {
        var action = ActionType.getById(buffer.readByte());
        return new ActionPacket(action);
    }

    public static void handle(ActionPacket message, Supplier<NetworkEvent.Context> context) {
        var player = context.get().getSender();
        if (!isWearingChassis(player)) return;
        var armor = (WearableChassis) player.getVehicle();

        switch (message.action) {
            case DISMOUNT -> armor.exitArmor();
            case OPEN_GUI -> armor.openGUI(player);
//            case ADD_ATTACK_CHARGE -> armor.addAttackCharge();
//            case RESET_ATTACK_CHARGE -> armor.resetAttackCharge();
        }
    }
}