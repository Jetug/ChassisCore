package com.jetug.chassis_core.common.config;

import com.jetug.chassis_core.common.data.holders.ChassisPart;
import com.jetug.chassis_core.modules.config.annotation.Ignored;
import com.jetug.chassis_core.modules.config.utils.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Set;

public class ChassisConfig implements INBTSerializable<CompoundTag>{
    @Ignored private Set<ChassisPart> parts;

    @Override
    public CompoundTag serializeNBT() {
        var tag = new CompoundTag();
        tag.put("parts", NbtUtils.serializeSet(this.parts));
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        if (tag.contains("parts", Tag.TAG_COMPOUND)) {
            this.parts = NbtUtils.deserializeSet(tag.getCompound("parts"), ChassisPart::getType);
        }
    }

    public ChassisConfig copy() {
        var config = new ChassisConfig();
        config.parts = this.parts;
        return config;
    }

    public static ChassisConfig create(CompoundTag tag) {
        var config = new ChassisConfig();
        config.deserializeNBT(tag);
        return config;
    }

    public static class Builder {
        private final ChassisConfig config;

        private Builder() {
            this.config = new ChassisConfig();
        }

        private Builder(ChassisConfig projectile) {
            this.config = projectile.copy();
        }

        public static ChassisConfig.Builder create() {
            return new ChassisConfig.Builder();
        }

        public static ChassisConfig.Builder create(ChassisConfig projectile) {
            return new ChassisConfig.Builder(projectile);
        }

        public ChassisConfig build() {
            return this.config.copy();
        }

        public ChassisConfig.Builder setParts(Set<ChassisPart> parts) {
            this.config.parts = parts;
            return this;
        }
    }
}
