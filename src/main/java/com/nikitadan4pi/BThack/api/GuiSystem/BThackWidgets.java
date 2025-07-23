package com.nikitadan4pi.BThack.api.GuiSystem;

import com.nikitadan4pi.BThack.api.Gui.Widget.Config.ConfigsWidget;
import com.nikitadan4pi.BThack.api.Gui.Widget.Config.SaveConfigWidget;
import com.nikitadan4pi.BThack.api.Gui.Widget.LanguageSelectorWidget;
import com.nikitadan4pi.BThack.api.Gui.Widget.OutdatedVersionWidget;

public class BThackWidgets {
    private static boolean inited = false;

    public static ScreenWidget LANGUAGE_SELECTOR;
    public static ScreenWidget OUTDATED_VERSION;
    public static ScreenWidget CONFIGS_WIDGET;
    public static ScreenWidget SAVE_CONFIG_WIDGET;

    public static void init() {
        if (inited) return;

        LANGUAGE_SELECTOR = new LanguageSelectorWidget();
        OUTDATED_VERSION = new OutdatedVersionWidget();
        CONFIGS_WIDGET = new ConfigsWidget();
        SAVE_CONFIG_WIDGET = new SaveConfigWidget();
        inited = true;
    }
}
