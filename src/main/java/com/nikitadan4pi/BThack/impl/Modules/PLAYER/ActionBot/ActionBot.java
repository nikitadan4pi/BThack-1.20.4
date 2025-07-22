package com.nikitadan4pi.BThack.impl.Modules.PLAYER.ActionBot;


import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.GuiButtonSetting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.nikitadan4pi.BThack.api.Gui.ActionBot.ActionBotConfigGui;

public class ActionBot extends Module {
    ActionBotRunTimeThread thread;

    public static BooleanSetting repeat;
    public static GuiButtonSetting openConfig;

    public ActionBot() {
        super("ActionBot",
                "lang.module.ActionBot",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );

        repeat = new BooleanSetting("Repeat", this, false);


        openConfig = new GuiButtonSetting("Open Config", this, ActionBotConfigGui::new);

        initSettings(
                repeat,
                openConfig
        );
    }

    @Override
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }

        super.onEnable();

        thread = new ActionBotRunTimeThread();
        thread.start();
    }

    @Override
    public void onDisable() {
        if (nullCheck() || thread == null) {
            super.onDisable();
        }

        try {
            if (thread.isAlive())
                thread.stop();
        } catch (Exception ignored) {}
    }
}
