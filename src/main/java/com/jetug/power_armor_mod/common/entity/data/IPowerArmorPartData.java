package com.jetug.power_armor_mod.common.entity.data;

public interface IPowerArmorPartData extends IData {
    double getDurability();
    void setDurability(double value);

    double getDefense();

    void setDefense(double value);

    void copyFrom(IPowerArmorPartData source);
}