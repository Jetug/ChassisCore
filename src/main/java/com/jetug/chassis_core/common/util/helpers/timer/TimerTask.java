package com.jetug.chassis_core.common.util.helpers.timer;

public interface TimerTask {
    boolean isCompleted();

    void tick();
}
