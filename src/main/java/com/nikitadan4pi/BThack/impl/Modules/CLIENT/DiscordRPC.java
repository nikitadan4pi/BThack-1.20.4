package com.nikitadan4pi.BThack.impl.Modules.CLIENT;

import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.DiscordUtils;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;

public class DiscordRPC extends Module {

    public static BooleanSetting secret;

    public DiscordRPC() {
        super("DiscordRPC",
                "lang.module.DiscordRPC",
                KeyboardUtils.RELEASE,
                MCategory.CLIENT,
                true
        );
        allowRemapKeyCode = false;

        secret = new BooleanSetting("Secret :3", this, false);

        initSettings(secret);

        DiscordUtils.init();
    }

    @Override
    public void onEnable() {
        DiscordUtils.startup();
    }

    @Override
    public void onDisable() {
        DiscordUtils.shutdown();
    }
}
