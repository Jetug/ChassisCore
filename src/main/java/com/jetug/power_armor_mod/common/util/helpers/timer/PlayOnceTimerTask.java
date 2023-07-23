package com.jetug.power_armor_mod.common.util.helpers.timer;

public class PlayOnceTimerTask implements TimerTask {
    private final Runnable predicate;
    private final int initTicks;
    private int ticks;
    private boolean isCompleted = false;

    public PlayOnceTimerTask(int ticks, Runnable predicate) {
        this.predicate = predicate;
        this.initTicks = ticks;
        this.ticks = ticks;
    }

    public void reset() {
        ticks = initTicks;
        isCompleted = false;
    }

    @Override
    public boolean isCompleted() {
        return isCompleted;
    }

    @Override
    public void tick() {
        if (ticks == 0) {
            predicate.run();
            isCompleted = true;
        }
        ticks -= 1;
    }
}
