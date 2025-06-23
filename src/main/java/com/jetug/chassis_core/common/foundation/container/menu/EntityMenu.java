package com.jetug.chassis_core.common.foundation.container.menu;

import com.jetug.chassis_core.ChassisCore;
import com.jetug.chassis_core.common.data.holders.ChassisPart;
import com.jetug.chassis_core.common.foundation.container.slot.EquipmentSlot;
import com.jetug.chassis_core.common.foundation.entity.Chassis;
import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import com.jetug.chassis_core.common.util.Pos2I;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;

import javax.annotation.Nullable;

public abstract class EntityMenu extends MenuBase {
    protected final Chassis chassis;

    public EntityMenu(MenuType<?> pMenuType, int containerId, Container container, Inventory playerInventory,
                      Chassis entity, int inventoryPosY) {
        super(pMenuType, containerId, container, playerInventory, inventoryPosY);
        this.chassis = entity;
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return super.stillValid(playerIn) && this.chassis.isAlive() && this.chassis.distanceTo(playerIn) < 8.0F;
    }

    public Chassis getChassis() {
        return chassis;
    }

    @Nullable
    protected Integer getId(ChassisPart chassisPart) {
        return chassis.getPartId(chassisPart);
    }

    protected void createSlot(ChassisPart chassisPart, Pos2I pos) {
        try {
            var id = getId(chassisPart);
            if(id != null)
                this.addSlot(new EquipmentSlot(chassisPart, container, getId(chassisPart), pos.x, pos.y));
        } catch (Exception e) {
            ChassisCore.LOGGER.error(e.getMessage(), e);
        }
    }
}