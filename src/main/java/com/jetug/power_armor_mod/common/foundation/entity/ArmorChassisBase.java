package com.jetug.power_armor_mod.common.foundation.entity;

import com.jetug.power_armor_mod.client.ClientConfig;
import com.jetug.power_armor_mod.common.data.json.FrameSettings;
import com.jetug.power_armor_mod.common.events.*;
import com.jetug.power_armor_mod.common.foundation.container.menu.*;
import com.jetug.power_armor_mod.common.foundation.item.*;
import com.jetug.power_armor_mod.client.render.utils.ResourceHelper;
import com.jetug.power_armor_mod.common.util.helpers.HeatController;
import com.jetug.power_armor_mod.common.util.helpers.timer.*;
import com.jetug.power_armor_mod.common.network.data.*;
import com.jetug.power_armor_mod.common.data.enums.*;

import net.minecraft.nbt.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.*;
import net.minecraft.world.item.*;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.*;

import javax.annotation.Nullable;
import java.util.*;

import static com.jetug.power_armor_mod.common.network.data.ArmorData.*;
import static com.jetug.power_armor_mod.common.data.constants.NBT.*;
import static com.jetug.power_armor_mod.common.data.enums.BodyPart.*;
import static com.jetug.power_armor_mod.common.util.helpers.ContainerUtils.copyContainer;
import static com.jetug.power_armor_mod.common.util.helpers.ContainerUtils.isContainersEqual;
import static com.jetug.power_armor_mod.common.util.helpers.InventoryHelper.*;
import static com.jetug.power_armor_mod.common.util.helpers.MathHelper.*;

public class ArmorChassisBase extends EmptyLivingEntity implements ContainerListener {
    protected static final int MAX_ATTACK_CHARGE = 60;
    public static final int COOLING = 5;
    public static final int P_SIZE = values().length;

    protected final TickTimer timer = new TickTimer();
    protected final boolean isClientSide = level.isClientSide;
    protected final boolean isServerSide = !level.isClientSide;

    protected float totalDefense;
    protected float totalToughness;
    protected int attackCharge = 0;
    public SimpleContainer inventory;

    public HeatController heatController = new HeatController(){
        @Override
        public int getHeatCapacity(){
            return hasEquipment(ENGINE) ? ((EngineItem)getEquipment(ENGINE).getItem()).overheat : 0;
        }
    };

    private String chassisId = null;
    private FrameSettings settings = null;
    private ListTag serializedInventory;
    private Container previousContainer;

    public final BodyPart[] armor = new BodyPart[]{
            HELMET,
            BODY_ARMOR,
            LEFT_ARM_ARMOR,
            RIGHT_ARM_ARMOR,
            LEFT_LEG_ARMOR,
            RIGHT_LEG_ARMOR,
    };

    public final BodyPart[] equipment = BodyPart.values();


    public ArmorChassisBase(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        noCulling = true;
        initInventory();
        updateParams();
        //getType().getModelId()
    }

    @Override
    public void tick() {
        super.tick();
        syncDataWithClient();
        syncDataWithServer();
        heatController.subHeat(COOLING);
        if(isInLava()){
            heatController.addHeat(20);
        }
        if (isInWaterOrRain()){
            heatController.subHeat(5);
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
        compound.putInt(HEAT, heatController.getHeat());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        loadInventory(compound);
        heatController.setHeat(compound.getInt(HEAT));
    }

    @Override
    public void containerChanged(@NotNull Container container) {
        if (previousContainer == null || !isContainersEqual(previousContainer, container)) {
            containerRealyChanged(container);
            previousContainer = copyContainer(container);
        }
    }

    @Nullable
    public FrameSettings getSettings(){
        if(settings == null)
            settings = ClientConfig.modResourceManager.getFrameSettings(getModelId());
        return settings;
    }

    public String getModelId(){
        if(chassisId == null)
            chassisId = ResourceHelper.getResourceName(getType().getRegistryName());
        return chassisId;
    }

    public void containerRealyChanged(Container container){
        updateParams();
        serializedInventory = serializeInventory(inventory);
        MinecraftForge.EVENT_BUS.post(new ContainerChangedEvent(this));
    }


    
    public int getAttackChargeInPercent(){
        return getInPercents(attackCharge, MAX_ATTACK_CHARGE);
    }

    public boolean isChargingAttack(){
        return attackCharge > 5;
    }

    public boolean isMaxCharge(){
        return attackCharge == MAX_ATTACK_CHARGE;
    }

    public void addAttackCharge() {
        var value = 1;
        //if(isClientSide) doServerAction(ActionType.ADD_ATTACK_CHARGE);
        if(attackCharge + value <= MAX_ATTACK_CHARGE)
            attackCharge += value;
    }

    public void resetAttackCharge() {
        //if(isClientSide) doServerAction(ActionType.RESET_ATTACK_CHARGE);
        setAttackCharge(0);
    }

    public void setAttackCharge(int value){
        if(value == 0){
            var v = attackCharge;
        }
        if(value < 0)
            attackCharge = 0;
        else if(value > MAX_ATTACK_CHARGE)
            attackCharge = MAX_ATTACK_CHARGE;
        else
            attackCharge = value;
    }

    public Iterable<ItemStack> getPartSlots(){
        var items = new ArrayList<ItemStack>();

        for(int i = 0; i < PowerArmorMenu.SIZE; i++){
            items.add(inventory.getItem(i));
        }

        return items;
    }

    public boolean isEquipmentVisible(BodyPart bodyPart){
        if(bodyPart.isArmorItem())
            return hasArmor(bodyPart);
        else return !getEquipment(bodyPart).isEmpty();
    }

    public boolean hasArmor(BodyPart bodyPart) {
        return getArmorDurability(bodyPart) != 0;
    }

    public ItemStack getEquipment(BodyPart part) {
        return inventory.getItem(part.ordinal());
    }

    public boolean hasEquipment(BodyPart part){
        return !inventory.getItem(part.ordinal()).isEmpty();
    }

    public boolean hasPowerKnuckle(){
        return (getEquipment(RIGHT_HAND).getItem() instanceof PowerKnuckle);
    }

    public PowerKnuckle getPowerKnuckle(){
        return (PowerKnuckle) getEquipment(RIGHT_HAND).getItem();
    }

    public boolean hasJetpack(){
        return getEquipment(BACK).getItem() instanceof JetpackItem;
    }

    public JetpackItem getJetpack(){
        return (JetpackItem) getEquipment(BACK).getItem();
    }

    public int getArmorDurability(BodyPart bodyPart) {
        var itemStack = inventory.getItem(bodyPart.getId());
        if(itemStack.isEmpty()) return 0;
        return itemStack.getMaxDamage() - itemStack.getDamageValue();
    }

    public ArmorData getArmorData(){
        var data = new ArmorData(getId());
        data.inventory = serializedInventory;//serializeInventory(inventory);
        data.heat = heatController.getHeat();
        //data.attackCharge = attackCharge;

        return data;
    }

    public void setArmorData(ArmorData data){
        if(isClientSide) setClientArmorData(data);
        else setServerArmorData(data);
    }

    public void setClientArmorData(ArmorData data){
        deserializeInventory(inventory, data.inventory);
    }

    public void setServerArmorData(ArmorData data){
        //deserializeInventory(inventory, data.inventory);
        //heat = data.heat;
        //setAttackCharge(data.attackCharge);
    }

    public void setInventory(ListTag tags){
        deserializeInventory(inventory, tags);
    }

    protected void initInventory(){
        SimpleContainer inventoryBuff = this.inventory;
        this.inventory = new SimpleContainer(P_SIZE);
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
        if(isServerSide) getArmorData().sentToClient();
    }

    protected void syncDataWithServer() {
        if(isClientSide) getArmorData().sentToServer();
    }

    protected void saveInventory(CompoundTag compound){
        if (inventory == null) return;
        compound.put(ITEMS_TAG, serializeInventory(inventory));
    }

    protected void loadInventory(@NotNull CompoundTag compound) {
        ListTag nbtTags = compound.getList(ITEMS_TAG, 10);
        initInventory();
        deserializeInventory(inventory, nbtTags);
    }

    private void updateParams(){
        updateTotalArmor();
        updateSpeed();
    }

    private void updateSpeed(){
        if(inventory.getItem(ENGINE.ordinal()).getItem() instanceof EngineItem engine)
             setSpeed(getSpeedAttribute() * engine.speed);
        else {
            setSpeed(0.05f);
            return;
        }

        for (var part : armor){
            if (inventory.getItem(part.ordinal()).getItem() instanceof ChassisArmor armorItem)
                setSpeed(getSpeed() * armorItem.speed);
        }
    }

    private void updateTotalArmor(){
        totalDefense   = 0;
        totalToughness = 0;

        for (var part : armor){
            if (inventory.getItem(part.ordinal()).getItem() instanceof ChassisArmor armorItem){
                totalDefense   += armorItem.getMaterial().getDefenseForSlot(part);
                totalToughness += armorItem.getMaterial().getToughness();
            }
        }
    }

    private float getSpeedAttribute(){
        return (float)getAttributeValue(Attributes.MOVEMENT_SPEED);
    }
}
