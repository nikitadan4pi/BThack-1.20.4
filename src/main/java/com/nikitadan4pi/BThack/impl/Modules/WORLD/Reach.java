package com.nikitadan4pi.BThack.impl.Modules.WORLD;

import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;

public class Reach extends Module {

    public final NumberSetting range = new NumberSetting("Range", this, 0.5, 0.1, 4, false);

    public Reach() {
        super("Reach",
                "lang.module.Reach",
                KeyboardUtils.RELEASE,
                MCategory.WORLD,
                false
        );

        initSettings(
                range
        );
    }
}
