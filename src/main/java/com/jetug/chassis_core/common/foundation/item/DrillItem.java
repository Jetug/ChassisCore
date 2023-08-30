package com.jetug.chassis_core.common.foundation.item;

import com.jetug.chassis_core.client.render.renderers.item.DrillRenderer;
import com.jetug.chassis_core.common.foundation.ModCreativeModeTab;
import com.jetug.chassis_core.common.util.helpers.PlayerUtils;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

import static com.jetug.generated.animations.DrillAnimation.*;
import static com.jetug.chassis_core.common.util.helpers.PlayerUtils.getPlayerChassis;
import static com.jetug.chassis_core.common.util.helpers.PlayerUtils.isWearingChassis;
import static com.jetug.chassis_core.common.util.helpers.AnimationHelper.setAnimation;
import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.LOOP;
import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.PLAY_ONCE;

public class DrillItem extends AnimatableItem{
    public DrillItem() {
        super(new Properties().tab(ModCreativeModeTab.MY_TAB).stacksTo(1));
    }

    @Override
    protected DrillRenderer createRenderer() {
        return new DrillRenderer();
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::animate));
    }

    private <T extends IAnimatable> PlayState animate(AnimationEvent<T> event) {
        var controller = event.getController();
//
//        if (PlayerUtils.isWearingChassis()) {
//            var armor = PlayerUtils.getPlayerChassis();
//            controller.animationSpeed = 1;
//
//            if (armor.isPunching()) {
//                controller.animationSpeed = 2;
//                setAnimation(controller, PUNCH, PLAY_ONCE);
//            } else if (armor.isMaxCharge()) {
//                setAnimation(controller, PUNCH_MAX_CHARGE, LOOP);
//            } else if (armor.isChargingAttack()) {
//                controller.animationSpeed = 0.3;
//                setAnimation(controller, PUNCH_CHARGE, LOOP);
//            }
//            else {
//                setAnimation(controller, DRILL_USE, LOOP);
//            }
//        }

        return PlayState.CONTINUE;
    }
}
