package com.nikitadan4pi.BThack.impl.Modules.WORLD;

import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;


public class SkyColor extends Module {

    public static NumberSetting skyRed;
    public static NumberSetting skyGreen;
    public static NumberSetting skyBlue;

    public SkyColor() {
        super("SkyColour",
                "lang.module.SkyColor",
                KeyboardUtils.RELEASE,
                MCategory.WORLD,
                false
        );

        skyRed = new NumberSetting("Red", this, 21, 0, 255, true);
        skyGreen = new NumberSetting("Green", this, 191, 0, 255, true);
        skyBlue = new NumberSetting("Blue", this, 219, 0, 255, true);

        initSettings(
                skyRed,
                skyGreen,
                skyBlue
        );
    }
}
