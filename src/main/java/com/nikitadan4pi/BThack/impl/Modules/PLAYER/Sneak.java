package com.nikitadan4pi.BThack.impl.Modules.PLAYER;

import com.nikitadan4pi.BThack.api.Events.Entity.UpdateInputEvent;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

import java.util.Arrays;

public class Sneak extends Module {

    public static ModeSetting mode;

    public Sneak() {
        super("Sneak",
                "lang.module.Sneak",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );

        mode = new ModeSetting("Mode", this, Arrays.asList("Always", "Only Motion"));

        initSettings(
                mode
        );
    }

    @EventSubscriber
    public void onInputUpdate(UpdateInputEvent e) {
        if (mode.getValue().equals("Always"))
            mc.player.input.sneaking = true;
        else {
            if (mc.player.input.movementForward != 0 || mc.player.input.movementSideways != 0)
                mc.player.input.sneaking = true;
        }
    }
}
