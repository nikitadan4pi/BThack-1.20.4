package com.nikitadan4pi.BTbot.api.Utils.Motion.Align;


import com.nikitadan4pi.BThack.api.Interfaces.Mc;

public class WhereToAlign implements Mc {
    private double needX = 0;
    private double needZ = 0;

    public WhereToAlign() {}

    public double getNeedX() {
        return this.needX;
    }

    public double getNeedZ() {
        return this.needZ;
    }


    private void NumberMath() {
        needX = Math.floor(mc.player.getX()) + 0.5;
        needZ = Math.floor(mc.player.getZ()) + 0.5;
    }

    public void execute() {
        NumberMath();
    }
}