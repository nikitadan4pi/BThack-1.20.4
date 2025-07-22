package com.nikitadan4pi.BThack.api.Managers.managers.TravelChange;

import java.util.function.Supplier;

public class TravelChanger {
    public int priority;
    /** [0] - yaw   [1] - pitch */
    public final Supplier<Float[]> rotateGetter;
    public Runnable preUpdateVelocityRunnable;
    public Supplier<Boolean> needTravelChange;
    public boolean needRewriteTravelRot = true;
    public boolean withMoveFix;
    public Supplier<Boolean> strongMoveFix;

    public TravelChanger(int priority, Supplier<Float[]> rotateGetter, Runnable preUpdateVelocityRunnable, Supplier<Boolean> needTravelChange) {
        this.priority = priority;
        this.rotateGetter = rotateGetter;
        this.preUpdateVelocityRunnable = preUpdateVelocityRunnable;
        this.needTravelChange = needTravelChange;
    }

    public TravelChanger(int priority, Supplier<Float[]> rotateGetter, Supplier<Boolean> strongMoveFix) {
        this.priority = priority;
        this.rotateGetter = rotateGetter;
        this.withMoveFix = true;
        this.strongMoveFix = strongMoveFix;
    }

    public TravelChanger(int priority, Supplier<Float[]> rotateGetter) {
        this(priority, rotateGetter, () -> {}, () -> true);
    }
}
