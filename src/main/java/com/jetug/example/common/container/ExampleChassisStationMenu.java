package com.jetug.example.common.container;

import com.jetug.chassis_core.common.foundation.container.menu.EntityMenu;
import com.jetug.chassis_core.common.foundation.entity.WearableChassis;
import com.jetug.example.common.entities.ExampleChassis;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;

import static com.jetug.chassis_core.common.data.constants.ChassisPart.*;
import static com.jetug.chassis_core.common.data.constants.Gui.*;
import static com.jetug.example.common.registery.ContainerRegistry.EXAMPLE_STATION_MENU;

public class ExampleChassisStationMenu extends EntityMenu {
    private static final int INVENTORY_POS_Y = 105;
    public static final int SIZE = 10;

    public ExampleChassisStationMenu(int i, Inventory playerInventory) {
        this(i, new SimpleContainer(SIZE), playerInventory, null);
    }

    public ExampleChassisStationMenu(int containerId, Container container, Inventory playerInventory, WearableChassis entity) {
        super(EXAMPLE_STATION_MENU.get(), containerId, container, playerInventory, entity, SIZE, INVENTORY_POS_Y);
        createSlot(BODY_FRAME       , FRAME_BODY_SLOT_POS      );
        createSlot(LEFT_ARM_FRAME   , FRAME_LEFT_ARM_SLOT_POS  );
        createSlot(RIGHT_ARM_FRAME  , FRAME_RIGHT_ARM_SLOT_POS );
        createSlot(LEFT_LEG_FRAME   , FRAME_LEFT_LEG_SLOT_POS  );
        createSlot(RIGHT_LEG_FRAME  , FRAME_RIGHT_LEG_SLOT_POS );
        createSlot(ENGINE           , ENGINE_SLOT_POS2         );
        createSlot(BACK             , BACK_SLOT_POS            );
        createSlot(COOLING          , COOLING_SLOT_POS         );
        createSlot(LEFT_HAND        , LEFT_HAND_SLOT_POS );
        createSlot(RIGHT_HAND       , RIGHT_HAND_SLOT_POS);
    }

    @Override
    protected int getId(String chassisPart) {
        return ExampleChassis.getId(chassisPart);
    }
}
