package com.nikitadan4pi.BThack.api.Animation;

public class Animation {
    private final Easing easing;
    private int millis;

    private long startMillis;


    public Animation(Easing easing, int millis) {
        this.easing = easing;
        this.millis = millis;

        startMillis = System.currentTimeMillis();
    }

    public void setMillis(int millis) {
        this.millis = millis;
    }

    public void reset() {
        startMillis = System.currentTimeMillis();
    }

    public double getEase() {
        long currentMillis = getPassedMillis();
        return currentMillis >= millis ? 1 : easing.ease(currentMillis / (double) millis);
    }

    public int getMillis() {
        return millis;
    }

    public long getStartMillis() {
        return startMillis;
    }

    public long getPassedMillis() {
        return System.currentTimeMillis() - startMillis;
    }
}
