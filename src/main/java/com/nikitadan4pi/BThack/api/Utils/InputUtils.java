package com.nikitadan4pi.BThack.api.Utils;

import com.nikitadan4pi.BThack.api.Interfaces.Mc;
import net.minecraft.client.input.Input;

@SuppressWarnings("DataFlowIssue")
public final class InputUtils implements Mc {

    public static void setForward(boolean forward) {
        Input input = mc.player.input;
        mc.player.input.pressingForward = forward;
    }

    public static void setBackward(boolean backward) {
        Input input = mc.player.input;
        mc.player.input.pressingForward = backward;
    }

    public static void setLeft(boolean left) {
        Input input = mc.player.input;
        mc.player.input.pressingLeft = left;
    }

    public static void setRight(boolean right) {
        Input input = mc.player.input;
        mc.player.input.pressingRight = right;
    }

    public static void setJumping(boolean jumping) {
        Input input = mc.player.input;
        mc.player.input.jumping = jumping;
    }

    public static void setSneaking(boolean sneaking) {
        Input input = mc.player.input;
        mc.player.input.sneaking = sneaking;
    }

    public static void setSprinting(boolean sprinting) {
        Input input = mc.player.input;
        mc.player.setSprinting(sprinting);
    }

    public static void setInput(boolean forward, boolean backward, boolean left, boolean right, boolean jump, boolean sneak, boolean sprint) {
        return;
    }
}
