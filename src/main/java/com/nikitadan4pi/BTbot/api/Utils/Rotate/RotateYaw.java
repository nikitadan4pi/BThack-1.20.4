package com.nikitadan4pi.BTbot.api.Utils.Rotate;


import com.nikitadan4pi.BThack.api.Interfaces.Mc;

public class RotateYaw implements Mc {
    public static void rotateYaw(float position) {
        checkRotation(position, 0, 0);
        checkRotation(position, 0.125f, 45);
        checkRotation(position, 0.25f, 90);
        checkRotation(position, 0.375f, 135);
        checkRotation(position, 0.5f, 180);
        checkRotation(position, 0.625f, 225);
        checkRotation(position, 0.75f, 270);
        checkRotation(position, 0.875f, 315);
    }

    private static void checkRotation(float pos,float need, int yaw) {
        if (pos == need)
            mc.player.yaw = yaw;
    }
}
