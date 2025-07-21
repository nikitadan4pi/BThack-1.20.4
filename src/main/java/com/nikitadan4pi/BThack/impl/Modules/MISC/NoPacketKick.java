package com.nikitadan4pi.BThack.impl.Modules.MISC;

import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;

public class NoPacketKick extends Module {

    public static BooleanSetting chatNotify;

    public NoPacketKick() {
        super("NoPacketKick",
                "lang.module.NoPacketKick",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );

        chatNotify = new BooleanSetting("ChatNotify", this, false);

        initSettings(
                chatNotify
        );
    }
}
