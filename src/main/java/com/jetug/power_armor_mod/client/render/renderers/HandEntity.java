package com.jetug.power_armor_mod.client.render.renderers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import static com.jetug.generated.animations.ArmorChassisHandAnimation.*;
import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.*;
import static com.jetug.power_armor_mod.common.util.helpers.AnimationHelper.*;
import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.*;
import static software.bernie.geckolib3.util.GeckoLibUtil.*;

public class HandEntity implements IAnimatable {
    public AnimationFactory factory = createFactory(this);
    public LocalPlayer player;

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
        player = Minecraft.getInstance().player;
    }

    @SuppressWarnings("ConstantConditions")
    private <T extends IAnimatable> PlayState predicate(AnimationEvent<T> event) {
        if(!isWearingChassis()) return PlayState.STOP;

        var controller = event.getController();
        var armor = getPlayerChassis();
        controller.animationSpeed = 1;

        if(armor.isPunching()){
            controller.animationSpeed = 2;
            setAnimation(controller, PUNCH, PLAY_ONCE);
        }
        else if(armor.isMaxCharge()){
            setAnimation(controller,PUNCH_MAX_CHARGE, LOOP);
        }
        else if(armor.isChargingAttack()){
            controller.animationSpeed = 0.3;
            setAnimation(controller,PUNCH_CHARGE, LOOP);
        }
        else if(player.swinging){
            controller.animationSpeed = 3;
            setAnimation(controller,HIT, LOOP);
        }
        else if(armor.isWalking()){
            setAnimation(controller,WALK, LOOP);
        }
        else {
            setAnimation(controller,IDLE, LOOP);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}