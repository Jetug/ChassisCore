package com.jetug.chassis_core.common.foundation.container.slot;

import com.jetug.chassis_core.common.foundation.item.ChassisEquipment;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class EquipmentSlot extends Slot {
    private final String bodyPart;

    public EquipmentSlot(String bodyPart, Container itemHandler, int index, int x, int y) {
        super(itemHandler, index, x, y);
        this.bodyPart = bodyPart;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return super.mayPlace(stack)
                && stack.getItem() instanceof ChassisEquipment item
                && Objects.equals(item.part, bodyPart);
    }
}