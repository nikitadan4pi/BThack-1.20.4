package com.nikitadan4pi.BThack.impl.Modules.MOVEMENT;

import com.nikitadan4pi.BThack.BThack;
import com.nikitadan4pi.BThack.api.Events.ClientTickEvent;
import com.nikitadan4pi.BThack.api.Events.Entity.UpdateInputEvent;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.BaritoneUtils;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

import java.util.Arrays;

public class AutoWalk extends Module {

    public static BooleanSetting disableOnDisconnect;
    public static ModeSetting mode;


    public AutoWalk() {
        super("AutoWalk",
                "lang.module.AutoWalk",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );

        disableOnDisconnect = new BooleanSetting("Disable On Disconnect", this, true);
        mode = new ModeSetting("Mode", this, Arrays.asList("Forward", "Baritone"));

        initSettings(
                disableOnDisconnect,
                mode
        );
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (mode.getValue().equals("Baritone"))
            if (BThack.isBaritonePresent()) BaritoneUtils.cancelAllPathing();
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) {
            if (disableOnDisconnect.getValue()) toggle();
            return;
        }

        if (mode.getValue().equals("Baritone"))  baritoneAction();
    }

    @EventSubscriber
    public void onInputUpdate(UpdateInputEvent e) {
        if (mode.getValue().equals("Forward")) forwardAction();
    }

    public void forwardAction() {
        mc.player.input.movementForward = 1;
    }

    public void baritoneAction() {
        if (!BThack.isBaritonePresent()) {
            toggle();
            return;
        }

        if (!BaritoneUtils.isActive())
            BaritoneUtils.autoWalkUpdate();
    }
}
