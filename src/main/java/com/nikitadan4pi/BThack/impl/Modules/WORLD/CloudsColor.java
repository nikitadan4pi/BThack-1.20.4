package com.nikitadan4pi.BThack.impl.Modules.WORLD;

import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;

public class CloudsColor extends Module {

    public static NumberSetting cloudsRed;
    public static NumberSetting cloudsGreen;
    public static NumberSetting cloudsBlue;

    public CloudsColor() {
        super("CloudsColor",
                "lang.module.CloudsColor",
                KeyboardUtils.RELEASE,
                MCategory.WORLD,
                false
        );

        cloudsRed = new NumberSetting("Red", this, 255, 0, 255, false);
        cloudsGreen = new NumberSetting("Green", this, 255, 0, 255, false);
        cloudsBlue = new NumberSetting("Blue", this, 255, 0, 255, false);

        initSettings(
                cloudsRed,
                cloudsGreen,
                cloudsBlue
        );
    }
}
