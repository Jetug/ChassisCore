package com.jetug.power_armor_mod.common.capability.data;

import com.jetug.power_armor_mod.common.network.PacketHandler;
import com.jetug.power_armor_mod.common.network.packet.ArmorPartClientPacket;
import com.jetug.power_armor_mod.common.network.packet.ArmorPartPacket;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class ArmorPartData implements IArmorPartData {
    public static final String DURABILITY = "durability";
    public static final String DEFENSE = "defense";
    private static final int size = 6;

    private final Entity entity;
    private float durability;
    private double defense;

    private float head    ;
    private float body    ;
    private float leftArm ;
    private float rightArm;
    private float leftLeg ;
    private float rightLeg;

    public ArmorPartData(Entity entity) {
        this.entity = entity;
    }

//    @Override
//    public float[] getDurabilityArray() {
//        return durability;
//    }
//
//    @Override
//    public void setDurabilityArray(float[] array) {
//        if(array != null && array.length == size)
//            durability = array;
//    }

    @Override
    public float getDurability(BodyPart part) {
        switch (part){
            case HEAD:
                return head;
                //break;
            case BODY:
                return body;
                //break;
            case LEFT_ARM:
                return leftArm;
                //break;
            case RIGHT_ARM:
                return rightArm;
                //break;
            case LEFT_LEG:
                return leftLeg;
                //break;
            case RIGHT_LEG:
                return rightLeg;
                //break;
        }
        return head;
    }

    @Override
    public void setDurability(BodyPart part, float value) {
        switch (part){
            case HEAD:
                head = value;
                break;
            case BODY:
                body = value;
                break;
            case LEFT_ARM:
                leftArm = value;
                break;
            case RIGHT_ARM:
                rightArm = value;
                break;
            case LEFT_LEG:
                leftLeg = value;
                break;
            case RIGHT_LEG:
                rightLeg = value;
                break;
        }
        durability = value;
    }

    @Override
    public double getDefense() {
        return defense;
    }

    @Override
    public void setDefense(double value) {
        defense = value;
    }

    @Override
    public Entity getEntity() {
        return entity;
    }

//    @Override
//    public void copyFrom(IArmorPartData source) {
//        durability = source.getDurabilityArray();
//        defense = source.getDefense();
//    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();

        int count = -1;
        nbt.putFloat(DURABILITY + count++, head     );
        nbt.putFloat(DURABILITY + count++, body     );
        nbt.putFloat(DURABILITY + count++, leftArm  );
        nbt.putFloat(DURABILITY + count++, rightArm );
        nbt.putFloat(DURABILITY + count++, leftLeg  );
        nbt.putFloat(DURABILITY + count++, rightLeg );

        //nbt.putFloat(DURABILITY, durability);
        nbt.putDouble(DEFENSE, defense);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        int count = -1;

        head     = nbt.getFloat(ArmorPartData.DURABILITY + count++);
        body     = nbt.getFloat(ArmorPartData.DURABILITY + count++);
        leftArm  = nbt.getFloat(ArmorPartData.DURABILITY + count++);
        rightArm = nbt.getFloat(ArmorPartData.DURABILITY + count++);
        leftLeg  = nbt.getFloat(ArmorPartData.DURABILITY + count++);
        rightLeg = nbt.getFloat(ArmorPartData.DURABILITY + count++);

        //durability = nbt.getFloat(ArmorPartData.DURABILITY);
        defense = nbt.getDouble(ArmorPartData.DEFENSE);
//
//        durability = data.getDouble(DURABILITY);
//        defense = data.getDouble(DEFENSE);
    }

    @Override
    public void syncWithClient(ServerPlayerEntity player) {
        PacketHandler.sendTo(new ArmorPartPacket(this), player);

//        boolean isClientSide = player.level.isClientSide;
//        PacketHandler.sendToAllPlayers(new ArmorPartPacket(this), player.server);
//        ArmorPartPacket packet = new ArmorPartPacket(this);
//        PacketHandler.sendTo(packet, player);
    }

    @Override
    public void syncWithServer() {
        PacketHandler.sendToServer(new ArmorPartClientPacket(this));
    }

    @Override
    public boolean syncWithAll() {
        return false;
    }
}