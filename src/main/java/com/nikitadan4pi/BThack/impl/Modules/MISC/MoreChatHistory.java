package com.nikitadan4pi.BThack.impl.Modules.MISC;

import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;

public class MoreChatHistory extends Module {

    public static NumberSetting size;

    public MoreChatHistory() {
        super(  "MoreChatHistory",
                "lang.module.MoreChatHistory",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );

        size = new NumberSetting("Size", this, 5000, 150, 10000, true);

        initSettings(
                size
        );
    }
}
