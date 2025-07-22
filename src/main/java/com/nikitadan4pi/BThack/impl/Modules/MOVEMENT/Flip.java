package com.nikitadan4pi.BThack.impl.Modules.MOVEMENT;

import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.nikitadan4pi.BThack.api.Module.OneActionModule;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;

public class Flip extends OneActionModule {

    public static BooleanSetting saveSpeed;

    public Flip() {
        super("Flip",
                "lang.module.Flip",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );

        saveSpeed = new BooleanSetting("Save Speed", this, true);

        initSettings(
                saveSpeed
        );
    }

    @Override
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }

        double mX = mc.player.velocity.x;
        double mZ = mc.player.velocity.z;

        mc.player.setYaw(mc.player.getYaw() - 180);
        if (saveSpeed.getValue()) {
            mc.player.velocity.x = -mX;
            mc.player.velocity.z = -mZ;
        }
    }
}
