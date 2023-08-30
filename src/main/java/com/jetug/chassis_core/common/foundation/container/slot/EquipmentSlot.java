package com.jetug.chassis_core.common.foundation.container.slot;

import com.jetug.chassis_core.common.data.enums.*;
import com.jetug.chassis_core.common.foundation.item.ChassisEquipment;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class EquipmentSlot extends Slot {
    private final BodyPart bodyPart;

    public EquipmentSlot(BodyPart bodyPart, Container itemHandler, int index, int x, int y) {
        super(itemHandler, index, x, y);
        this.bodyPart = bodyPart;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return super.mayPlace(stack)
                && stack.getItem() instanceof ChassisEquipment item
                && item.part == bodyPart;
    }
}