package com.jetug.chassis_core.common.foundation.entity;

import com.jetug.chassis_core.common.foundation.item.DrillItem;
import com.jetug.chassis_core.common.foundation.container.menu.ArmorStationMenu;
import com.jetug.chassis_core.common.foundation.container.menu.PowerArmorMenu;
import com.jetug.chassis_core.common.data.enums.*;
import com.jetug.chassis_core.common.foundation.item.ChassisEquipment;
import com.jetug.chassis_core.common.foundation.item.ChassisArmor;
import com.jetug.chassis_core.common.data.constants.Global;
import com.jetug.chassis_core.common.foundation.registery.ItemRegistry;
import com.jetug.chassis_core.common.util.helpers.*;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.*;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;

import static com.jetug.chassis_core.common.foundation.EntityHelper.*;
import static com.jetug.chassis_core.common.data.enums.BodyPart.*;
import static com.jetug.chassis_core.common.util.helpers.PlayerUtils.*;
import static net.minecraft.util.Mth.*;
import static net.minecraft.world.InteractionHand.*;
import static org.apache.logging.log4j.Level.*;

public abstract class WearableChassis extends ArmorChassisBase implements IAnimatable {
    public static final float ROTATION = (float) Math.PI / 180F;
    public static final int EFFECT_DURATION = 9;
    public static final int MAX_PUNCH_FORCE = 20;
    public static final int DASH_DURATION = 10;
    public static final int PUNCH_DURATION = 10;

    public final Speedometer speedometer = new Speedometer(this);

    protected boolean isJumping;
    protected float playerJumpScale;

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);


    public WearableChassis(EntityType<? extends ArmorChassisBase> type, Level worldIn) {
        super(type, worldIn);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ArmorChassisBase.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 1000.0D)
                .add(Attributes.ATTACK_DAMAGE, 0.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.20D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.0D)
                .add(Attributes.JUMP_STRENGTH, 0.5D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8D);
    }

//    @Override
//    public EntityDimensions getDimensions(Pose pPose) {
//        return super.getDimensions(pPose).scale(1,  isShiftDown()? 0.5f : 1);
//    }

    @Override
    public void tick() {
        super.tick();
        speedometer.tick();
        timer.tick();
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        float finalDamage = getDamageAfterAbsorb(damage);

        if(damageSource == DamageSource.CACTUS)
            return false;

        if(isServerSide){
            if(damageSource == DamageSource.FALL) {
                damageArmorItem(LEFT_LEG_ARMOR, damageSource, damage);
                damageArmorItem(RIGHT_LEG_ARMOR, damageSource, damage);
            }
            else{
                damageArmorItem(HELMET, damageSource , damage);
                damageArmorItem(BODY_ARMOR, damageSource , damage);
                damageArmorItem(LEFT_ARM_ARMOR, damageSource , damage);
                damageArmorItem(RIGHT_ARM_ARMOR, damageSource , damage);
                damageArmorItem(LEFT_LEG_ARMOR, damageSource, damage);
                damageArmorItem(RIGHT_LEG_ARMOR, damageSource, damage);
            }
        }

        if (hasPlayerPassenger()) getPlayerPassenger().hurt(damageSource, finalDamage);

        return true;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if(hasPlayerPassenger()) this.yHeadRot = this.getYRot();
    }

//    @Override
//    public boolean isInvisible() {
//        var clientPlayer = Minecraft.getInstance().player;
//        var pov = Minecraft.getInstance().options.getCameraType();
//
//        if (hasPassenger(clientPlayer) && pov == CameraType.FIRST_PERSON)
//            return true;
//        return super.isInvisible();
//    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        if (super.isInvulnerableTo(damageSource))
            return true;
        else
            return damageSource.getEntity() == getControllingPassenger();
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 vector, InteractionHand hand) {
        Global.LOGGER.log(INFO, level.isClientSide);
        var stack = player.getItemInHand(hand);

        if(isServerSide && !player.isPassenger()) {
            if (stack.getItem() == Items.STICK)
                return giveEntityItemToPlayer(player, this, hand);
            if (player.isShiftKeyDown()) {
                openGUI(player);
                return InteractionResult.SUCCESS;
            } else if (!isVehicle()) {
                this.doPlayerRide(player);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    @Override
    public void positionRider(Entity entity) {
        super.positionRider(entity);

        var yOffset = getPlayerPassenger().isShiftKeyDown() ?  1.2f : 1.0f;
        var posY = getY() + getPassengersRidingOffset() + entity.getMyRidingOffset() - yOffset;
        entity.setPos(getX(), posY, getZ());

        if (entity instanceof LivingEntity livingEntity)
            livingEntity.yBodyRot = yBodyRot;
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        if (!isAlive()) return;
        if (isVehicle() && hasPlayerPassenger())
            travelWithPlayer(travelVector);
        else {
            this.flyingSpeed = 0.02F;
            super.travel(travelVector);
        }
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity p_20123_) {
        return super.getDismountLocationForPassenger(p_20123_);
    }

    @Override
    public void checkDespawn() {}

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean canBeRiddenInWater(Entity rider) {
        return true;
    }

    @Override
    protected float tickHeadTurn(float pYRot, float pAnimStep) {
        if(hasPlayerPassenger())
            return super.tickHeadTurn(pYRot, pAnimStep);
        return pAnimStep;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public void damageArmorItem(BodyPart bodyPart, DamageSource damageSource, float damage) {
        Global.LOGGER.info("damageArmorItem" + isClientSide);
        var itemStack = inventory.getItem(bodyPart.getId());

        if(itemStack.getItem() instanceof ChassisArmor armorItem)
            armorItem.damageArmor(itemStack, (int) damage);
    }

    @Nullable
    public ChassisEquipment getEquipmentItem(BodyPart part) {
        var stack = getEquipment(part);
        if(!stack.isEmpty())
            return (ChassisEquipment) stack.getItem();
        return null;
    }

    public void openGUI(Player player) {
        Global.referenceMob = this;

        if (isServerSide) {
            player.openMenu(new MenuProvider() {
                @Override
                public AbstractContainerMenu createMenu(int id, Inventory menu, Player player) {
                    return new PowerArmorMenu(id, inventory, menu, WearableChassis.this);
                }

                @Override
                public Component getDisplayName() {
                    return WearableChassis.this.getDisplayName();
                }
            });
        }
    }

    public void openStationGUI(Player player) {
        Global.referenceMob = this;

        if (isServerSide) {
            player.openMenu(new MenuProvider() {
                @Override
                public AbstractContainerMenu createMenu(int id, Inventory menu, Player player) {
                    return new ArmorStationMenu(id, inventory, menu, WearableChassis.this);
                }

                @Override
                public Component getDisplayName() {
                    return WearableChassis.this.getDisplayName();
                }
            });
        }
    }

    public Boolean isWalking(){
        if (!hasPlayerPassenger())
            return false;

        var player = getPlayerPassenger();
        return player.xxa != 0.0 || player.zza != 0.0;
    }

    public boolean hasPlayerPassenger(){
        return getControllingPassenger() instanceof Player;
    }

    public Player getPlayerPassenger(){
        if(getControllingPassenger() instanceof Player player)
            return player;
        return null;
    }

    public ItemStack getPlayerItem(EquipmentSlot slot){
        return hasPlayerPassenger() ? getPlayerPassenger().getItemBySlot(slot) : ItemStack.EMPTY;
    }

    public void jump(){
        playerJumpScale = 1.0F;
    }

    private float getDamageAfterAbsorb(float damage){
        return CombatRules.getDamageAfterAbsorb(damage, totalDefense, totalToughness);
    }

    private void doPlayerRide(Player player) {
        player.setYRot(getYRot());
        player.setXRot(getXRot());
        player.startRiding(this);
    }

    boolean isDrillItemInHand() {
        return getPlayerPassenger().getItemInHand(MAIN_HAND).getItem() instanceof DrillItem;
    }

    boolean playerHandIsEmpty() {
        return getPlayerPassenger().getItemInHand(MAIN_HAND).isEmpty();
    }

    private boolean isJumping() {
        return this.isJumping;
    }

    private double getCustomJump() {
        return this.getAttributeValue(Attributes.JUMP_STRENGTH);
    }



//    @Override
//    public boolean causeFallDamage(float height, float p_225503_2_, @NotNull DamageSource damageSource) {
//        pushEntitiesAround();
//
//        int immune = 5;
//        int damage = this.calculateFallDamage(height, p_225503_2_) / 2;
//        if (damage <= immune) {
//            return false;
//        } else {
//            if (this.isVehicle()) {
//                for(Entity entity : getIndirectPassengers()) {
//                    entity.hurt(DamageSource.FALL, damage - immune);
//                }
//            }
//            animateHurt();
//            playBlockFallSound();
//            return true;

//        }
//    }

    private void travelWithPlayer(Vec3 travelVector) {
        var player = getPlayerPassenger();
        setRotationMatchingPassenger(player);

        if (playerJumpScale > 0.0F && !isJumping() && onGround)
            jump(player);

        this.flyingSpeed = getSpeed() * 0.1F;

        if (isControlledByLocalInstance())
            super.travel(new Vec3(player.xxa, travelVector.y, player.zza));
        else setDeltaMovement(Vec3.ZERO);

        if (onGround) {
            playerJumpScale = 0.0F;
            isJumping = false;
        }
    }

    private void jump(Player player) {
        var jump = getCustomJump() * playerJumpScale * getBlockJumpFactor();
        setDeltaMovement(getDeltaMovement().x, jump, getDeltaMovement().z);
        isJumping  = true;
        hasImpulse = true;

        if (player.zza > 0.0F) {
            float x = sin(getYRot() * ROTATION);
            float z = cos(getYRot() * ROTATION);
            setDeltaMovement(getDeltaMovement().add(
                    -0.4F * x * playerJumpScale,
                    0.0D,
                    0.4F * z * playerJumpScale));
        }

        playerJumpScale = 0.0F;
    }

    private void setRotationMatchingPassenger(LivingEntity livingEntity) {
        this.yRotO = getYRot();
        this.setYRot(livingEntity.getYRot());
        this.setXRot(livingEntity.getXRot() * 0.5F);
        this.setRot(getYRot(), getXRot());
    }
}