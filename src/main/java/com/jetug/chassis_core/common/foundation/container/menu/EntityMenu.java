package com.jetug.chassis_core.common.foundation.container.menu;

import com.jetug.chassis_core.common.data.holders.ChassisPart;
import com.jetug.chassis_core.common.foundation.container.slot.EquipmentSlot;
import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import com.jetug.chassis_core.common.util.Pos2I;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;

public abstract class EntityMenu extends MenuBase {
    protected final WearableChassis entity;
    //private final HashMap<String, Integer> slotsMap = new HashMap<>();
    //private Integer slotId = 0;

    public EntityMenu(MenuType<?> pMenuType, int containerId, Container container, Inventory playerInventory,
                      WearableChassis entity, int size, int inventoryPosY) {
        super(pMenuType, containerId, container, playerInventory, size, inventoryPosY);
        this.entity = entity;
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return super.stillValid(playerIn) && this.entity.isAlive() && this.entity.distanceTo(playerIn) < 8.0F;
    }

    public WearableChassis getEntity() {
        return entity;
    }

    protected abstract int getId(ChassisPart chassisPart);

    protected void createSlot(ChassisPart chassisPart, Pos2I pos) {
        try {
            //if(entity == null) return;
            this.addSlot(new EquipmentSlot(chassisPart, container, getId(chassisPart), pos.x, pos.y));
        } catch (Exception ignored) {
        }
    }
}