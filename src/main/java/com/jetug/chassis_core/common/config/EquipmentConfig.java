package com.jetug.chassis_core.common.config;

import com.jetug.chassis_core.common.data.holders.ChassisPart;
import com.jetug.chassis_core.modules.config.annotation.Ignored;
import com.jetug.chassis_core.modules.config.utils.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Set;

public class EquipmentConfig implements INBTSerializable<CompoundTag>{
    @Ignored private ChassisPart part;
    @Ignored private Set<ResourceLocation> chassis;

    @Override
    public CompoundTag serializeNBT() {
        var tag = new CompoundTag();
        tag.putString("part", this.part.toString());
        tag.put("chassis", NbtUtils.serializeSet(this.chassis));
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        if (tag.contains("part", Tag.TAG_STRING)) {
            this.part = ChassisPart.getType(tag.getString("part"));
        }
        if (tag.contains("chassis", Tag.TAG_COMPOUND)) {
            this.chassis = NbtUtils.deserializeRLSet(tag.getCompound("chassis"));
        }
    }

    public EquipmentConfig copy() {
        var config = new EquipmentConfig();
        config.part = this.part;
        return config;
    }

    public ChassisPart getPart() {
        return part;
    }

    public Set<ResourceLocation> getChassis() {
        return chassis;
    }

    public static class Builder {
        private final EquipmentConfig config;

        private Builder() {
            this.config = new EquipmentConfig();
        }

        private Builder(EquipmentConfig projectile) {
            this.config = projectile.copy();
        }

        public static EquipmentConfig.Builder create() {
            return new EquipmentConfig.Builder();
        }

        public static EquipmentConfig.Builder create(EquipmentConfig projectile) {
            return new EquipmentConfig.Builder(projectile);
        }

        public EquipmentConfig build() {
            return this.config.copy();
        }

        public EquipmentConfig.Builder setPart(ChassisPart part) {
            this.config.part = part;
            return this;
        }
    }
}
