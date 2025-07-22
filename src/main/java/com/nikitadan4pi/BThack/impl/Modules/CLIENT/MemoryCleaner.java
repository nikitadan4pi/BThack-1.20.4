package com.nikitadan4pi.BThack.impl.Modules.CLIENT;

import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;

public class MemoryCleaner extends Module {

    public static BooleanSetting showMessages;

    public MemoryCleaner() {
        super("MemoryCleaner",
                "lang.module.MemoryCleaner",
                KeyboardUtils.RELEASE,
                MCategory.CLIENT,
                true
        );

        allowRemapKeyCode = false;

        showMessages = new BooleanSetting("Show Messages", this, true);

        initSettings(showMessages);
    }
}
