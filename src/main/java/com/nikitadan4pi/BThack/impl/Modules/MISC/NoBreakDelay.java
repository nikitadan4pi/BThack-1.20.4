package com.nikitadan4pi.BThack.impl.Modules.MISC;

import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;

public class NoBreakDelay extends Module {
    public static BooleanSetting noInstant;

    public NoBreakDelay() {
        super("NoBreakDelay",
                "lang.module.NoBreakDelay",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );

        noInstant = new BooleanSetting("No Instant", this, true);

        initSettings(
                noInstant
        );
    }
}
