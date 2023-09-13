package com.jetug.chassis_core.common.foundation.item;

import com.jetug.chassis_core.common.data.json.EquipmentConfig;
import com.jetug.chassis_core.client.render.utils.ResourceHelper;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;

import static com.jetug.chassis_core.client.ClientConfig.*;

public class ChassisEquipment extends Item implements IAnimatable {
    public final String part;

    private final Lazy<String> name = Lazy.of(() -> ResourceHelper.getResourceName(getRegistryName()));
    private final Lazy<EquipmentConfig> config = Lazy.of(() -> modResourceManager.getEquipmentSettings(getName()));

    public ChassisEquipment(Properties pProperties, String part) {
        super(pProperties);
        this.part = part;
    }

    @Nullable
    public EquipmentConfig getConfig(){
        return config.get();
    }

    public String getName(){
        return name.get();
    }

    public AnimationFactory factory = GeckoLibUtil.createFactory(this);

    @Override
    public void registerControllers(AnimationData data) {}

    public AnimationFactory getFactory() {
        return this.factory;
    }
}
