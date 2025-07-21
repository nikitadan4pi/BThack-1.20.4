package com.nikitadan4pi.BThack.api.Events.Entity;


import com.ferra13671.MegaEvents.Base.Event;

public class JumpHeightEvent extends Event {

    private float jumpHeight;

    public JumpHeightEvent(float jumpHeight) {
        this.jumpHeight = jumpHeight;
    }

    public float getJumpHeight() {
        return jumpHeight;
    }

    public void  setJumpHeight(float value) {
        this.jumpHeight = value;
    }
}
