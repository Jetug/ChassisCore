package com.jetug.chassis_core.common.foundation.entity;

import com.jetug.chassis_core.client.ClientConfig;
import com.jetug.chassis_core.client.render.utils.GeoUtils;
import com.jetug.chassis_core.client.render.utils.ResourceHelper;
import com.jetug.chassis_core.common.config.Equipment;
import com.jetug.chassis_core.common.config.holders.BodyPart;
import com.jetug.chassis_core.common.data.json.ChassisConfig;
import com.jetug.chassis_core.common.events.ContainerChangedEvent;
import com.jetug.chassis_core.common.foundation.item.ChassisArmor;
import com.jetug.chassis_core.common.foundation.item.ChassisEquipment;
import com.jetug.chassis_core.common.foundation.item.StackUtils;
import com.jetug.chassis_core.common.network.data.ArmorData;
import com.jetug.chassis_core.common.util.helpers.timer.TickTimer;
import mod.azure.azurelib.cache.object.GeoBone;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

import static com.jetug.chassis_core.common.data.constants.ChassisPart.*;
import static com.jetug.chassis_core.common.data.constants.NBT.ITEMS_TAG;
import static com.jetug.chassis_core.common.util.extensions.Collection.arrayListOf;
import static com.jetug.chassis_core.common.util.helpers.ContainerUtils.copyContainer;
import static com.jetug.chassis_core.common.util.helpers.ContainerUtils.isContainersEqual;
import static com.jetug.chassis_core.common.util.helpers.InventoryHelper.deserializeInventory;
import static com.jetug.chassis_core.common.util.helpers.InventoryHelper.serializeInventory;
import static java.util.Arrays.stream;
import static java.util.Collections.addAll;

public class ChassisBase extends EmptyLivingEntity implements ContainerListener {
    public static final int INVENTORY_SIZE = 6;
    public static HashMap<BodyPart, Integer> PART_IDS = new HashMap<>();

    protected static final BodyPart[] ARMOR_PARTS = new BodyPart[]{
            BodyPart.HEAD     ,
            BodyPart.BODY     ,
            BodyPart.LEFT_ARM ,
            BodyPart.RIGHT_ARM,
            BodyPart.LEFT_LEG ,
            BodyPart.RIGHT_LEG,
    };

    static {
        var i = 0;
        for (var part: ARMOR_PARTS) {
            PART_IDS.put(part, i++);
        }
    }

    protected final TickTimer timer = new TickTimer();
    protected final boolean isClientSide = level().isClientSide;
    protected final boolean isServerSide = !level().isClientSide;
    private final Lazy<String> chassisId = Lazy.of(() -> ResourceHelper.getResourceName(ForgeRegistries.ENTITY_TYPES.getKey(this.getType())));
    private final HashMap<String, ArrayList<GeoBone>> bonesToRender = new HashMap<>();
    protected Collection<String> bonesToHide = new ArrayList<>();
    protected SimpleContainer inventory;
    protected float totalDefense;
    protected float totalToughness;
    protected int inventorySize = 6;
    protected HashMap<BodyPart, Integer> NEW_PART_IDS = PART_IDS;

    private int tickTimer = 10;
    private ChassisConfig config = null;
    private ListTag serializedInventory;
    private Container previousContainer;

    public ChassisBase(EntityType<? extends LivingEntity> pEntityType, Level pLevel, HashMap<BodyPart, Integer> newPartIds) {
        super(pEntityType, pLevel);
        this.NEW_PART_IDS = newPartIds;
        this.inventorySize = newPartIds.size();
        init();
    }

    public ChassisBase(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        init();
    }

    public static ChassisEquipment getAsChassisEquipment(ItemStack itemStack) {
        return (ChassisEquipment) itemStack.getItem();
    }

    public static <T, R> Collection<R> returnCollection(Collection<T> collection, Function<T, R> function) {
        var result = new ArrayList<R>();
        for (var item : collection) {
            var val = function.apply(item);
            if (val != null) result.add(val);
        }
        return result;
    }

    public void init() {
        noCulling = true;
        initInventory();
        updateParams();
    }

    //GETTERS
    public float getTotalDefense() {
        return totalDefense;
    }

    public float getTotalToughness() {
        return totalToughness;
    }

    public Collection<String> getBonesToHide() {
        return bonesToHide;
    }

    public int getInventorySize() {
        return inventorySize;
    }

    public void damageArmor(DamageSource damageSource, float damage) {
        if (isServerSide) {
            if (!damageSource.is(DamageTypes.FALL)) {
                damageArmorItem(BodyPart.HEAD     , damageSource, damage);
                damageArmorItem(BodyPart.BODY     , damageSource, damage);
                damageArmorItem(BodyPart.LEFT_ARM , damageSource, damage);
                damageArmorItem(BodyPart.RIGHT_ARM, damageSource, damage);
            }
            damageArmorItem(BodyPart.LEFT_LEG , damageSource, damage);
            damageArmorItem(BodyPart.RIGHT_LEG, damageSource, damage);
        }
    }

    public void damageArmorItem(BodyPart chassisPart, DamageSource damageSource, float damage) {
        var itemStack = getEquipment(chassisPart);

        if (itemStack.getItem() instanceof ChassisArmor armorItem) {
            armorItem.damageArmor(itemStack, (int) damage);
            setEquipment(chassisPart, itemStack);
        }
    }

//    public ArrayList<String> getHiddenBones(){
//        var hidden = new ArrayList<String>();
//        for(var item : getVisibleEquipment()) {
//            var config = getAsChassisEquipment(item);
//            var allMods = config.getConfig().mods.clone();
//            var mods = StackUtils.getAttachments(item);
//
//            Arrays.stream(allMods).toList().removeAll(mods);
//            hidden.add(Arrays.toString(allMods));
//        }
//        return hidden;
//    }

    public ArrayList<String> getMods() {
        var res = new ArrayList<String>();
        for (var config : getItemConfigs())
            res.addAll(List.of(config.getMods()));
        return res;
    }

    public ArrayList<String> getVisibleMods() {
        var res = new ArrayList<String>();
        for (var item : getVisibleEquipment())
            res.addAll(StackUtils.getAttachments(item));
        return res;
    }

    @OnlyIn(Dist.CLIENT)
    public Collection<GeoBone> getEquipmentBones(String frameBoneName) {
        var result = bonesToRender.get(frameBoneName);
        return result == null ? new ArrayList<>() : result;
    }

    public Integer getPartId(BodyPart chassisPart) {
        var val = NEW_PART_IDS.get(chassisPart);
        return val != null ? val : 0;
    }


    @Override
    public void tick() {
        super.tick();

        syncDataWithClient();
//        syncDataWithServer();

        if(isClientSide) {
            if (tickTimer == 0) {
                tickTimer = 10;
                updateBones();
            } else tickTimer--;
        }
    }

    @Override
    protected void tickEffects() {
        removeEffects();
    }

    public void removeEffects() {
        for (var effectInstance : this.getActiveEffects()) {
            removeEffect(effectInstance.getEffect());
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        saveInventory(compound);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        loadInventory(compound);
    }

    @Override
    public void containerChanged(@NotNull Container container) {
        if (previousContainer == null || !isContainersEqual(previousContainer, container)) {
            containerReallyChanged(container);
            previousContainer = copyContainer(container);
        }
    }

    @Nullable
    public ChassisConfig getConfig() {
        if (config == null)
            config = ClientConfig.modResourceManager.getFrameConfig(getModelId());
        return config;
    }

    public String getModelId() {
        return chassisId.get();
    }

    public void containerReallyChanged(Container container) {
        updateParams();
        serializedInventory = serializeInventory(inventory);
        MinecraftForge.EVENT_BUS.post(new ContainerChangedEvent(this));
    }

    public boolean isEquipmentVisible(BodyPart chassisPart) {
        if (isArmorItem(chassisPart))
            return hasArmor(chassisPart);
        else return !getEquipment(chassisPart).isEmpty();
    }

    public boolean isArmorItem(BodyPart chassisPart) {
        return stream(ARMOR_PARTS).toList().contains(chassisPart);
    }

    public boolean hasArmor(BodyPart chassisPart) {
        return getArmorDurability(chassisPart) != 0;
    }

    public Collection<BodyPart> getEquipment() {
        return NEW_PART_IDS.keySet();
    }

    public Collection<String> getPovEquipment() {
        return Collections.singleton(RIGHT_ARM_ARMOR);
    }

    public boolean hasEquipment(BodyPart part) {
        return !getEquipment(part).isEmpty();
    }

    public Collection<ItemStack> getVisibleEquipment() {
        return returnCollection(getEquipment(),
                (part) -> isEquipmentVisible(part) ?
                        getEquipment(part) : null);
    }

    public Collection<Equipment> getItemConfigs() {
        return returnCollection(getVisibleEquipment(), (equipment) -> ((ChassisEquipment) equipment.getItem()).getConfig());
    }

    public ItemStack getEquipment(BodyPart chassisPart) {
        return inventory.getItem(getPartId(chassisPart));
    }

    public void setEquipment(BodyPart chassisPart, ItemStack itemStack) {
        inventory.setItem(getPartId(chassisPart), itemStack);
    }

    public int getArmorDurability(BodyPart chassisPart) {
        var itemStack = getEquipment(chassisPart);
        if (itemStack.isEmpty()) return 0;
        return itemStack.getMaxDamage() - itemStack.getDamageValue();
    }

    public ArmorData getArmorData() {
        var data = new ArmorData(getId());
        data.inventory = serializedInventory;
        return data;
    }

    public void setArmorData(ArmorData data) {
        if (isClientSide) setClientArmorData(data);
        else setServerArmorData(data);
    }

    public void setClientArmorData(ArmorData data) {
        deserializeInventory(inventory, data.inventory);
    }

    public void setServerArmorData(ArmorData data) {
        //deserializeInventory(inventory, data.inventory);
        //heat = data.heat;
        //setAttackCharge(data.attackCharge);
    }

    public void setInventory(ListTag tags) {
        deserializeInventory(inventory, tags);
    }

    protected void initInventory() {
        SimpleContainer inventoryBuff = this.inventory;
        this.inventory = new SimpleContainer(inventorySize);
        if (inventoryBuff != null) {
            inventoryBuff.removeListener(this);
            int i = Math.min(inventoryBuff.getContainerSize(), this.inventory.getContainerSize());
            for (int j = 0; j < i; ++j) {
                var itemStack = inventoryBuff.getItem(j);
                if (!itemStack.isEmpty())
                    this.inventory.setItem(j, itemStack.copy());
            }
        }
        this.inventory.addListener(this);
        serializedInventory = serializeInventory(inventory);
    }

    protected void syncDataWithClient() {
        if (isServerSide) getArmorData().sentToClient();
    }

    protected void syncDataWithServer() {
        if (isClientSide) getArmorData().sentToServer();
    }

    protected void saveInventory(CompoundTag compound) {
        if (inventory == null) return;
        compound.put(ITEMS_TAG, serializeInventory(inventory));
    }

    protected void loadInventory(@NotNull CompoundTag compound) {
        ListTag nbtTags = compound.getList(ITEMS_TAG, 10);
        initInventory();
        deserializeInventory(inventory, nbtTags);
    }

    protected float getMinSpeed() {
        return 0.05f;
    }

    private void updateParams() {
        updateTotalArmor();
        updateSpeed();
        if (isClientSide)
            updateBones();
    }

    @OnlyIn(Dist.CLIENT)
    protected void updateBones() {
        bonesToRender.clear();
        bonesToHide.clear();

        for (var stack : getVisibleEquipment()) {
            var item = getAsChassisEquipment(stack);
            var config = item.getConfig();
            if(config == null) continue;
            addAll(bonesToHide, config.getHide());

            for (var attachment : config.getAttachments()) {
                if (stream(config.getMods()).toList().contains(attachment.armor)
                        && !StackUtils.hasAttachment(stack, attachment.armor))
                    continue;

                var bone = GeoUtils.getBone(config.getModel(), attachment.armor);
                if (bone == null) continue;

                if (!bonesToRender.containsKey(attachment.frame))
                    bonesToRender.put(attachment.frame, arrayListOf(bone));
                else bonesToRender.get(attachment.frame).add(bone);
            }
        }
    }

    protected void updateSpeed() {
        setSpeed(getSpeedAttribute());
    }

    public void updateTotalArmor() {
        this.totalDefense = 0;
        this.totalToughness = 0;

        for (var part : ARMOR_PARTS) {
            if (getEquipment(part).getItem() instanceof ChassisArmor armorItem) {
                this.totalDefense += armorItem.getMaterial().getDefenseForSlot(part);
                this.totalToughness += armorItem.getMaterial().getToughness();
            }
        }
    }

    public float getSpeedAttribute() {
        return (float) getAttributeValue(Attributes.MOVEMENT_SPEED);
    }
}
