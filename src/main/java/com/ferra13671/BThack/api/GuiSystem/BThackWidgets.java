package com.ferra13671.BThack.api.GuiSystem;

import com.ferra13671.BThack.api.Gui.Widget.Config.*;
import com.ferra13671.BThack.api.Gui.Widget.LanguageSelectorWidget;
import com.ferra13671.BThack.api.Gui.Widget.OutdatedVersionWidget;

public class BThackWidgets {
    private static boolean inited = false;

    public static ScreenWidget CONFIGS;
    public static ScreenWidget LANGUAGE_SELECTOR;
    public static ScreenWidget OUTDATED_VERSION;
    public static ScreenWidget SAVE_CONFIG;


    public static void init() {
        if (inited) return;

        CONFIGS = new ConfigsWidget();
        LANGUAGE_SELECTOR = new LanguageSelectorWidget();
        OUTDATED_VERSION = new OutdatedVersionWidget();
        SAVE_CONFIG = new SaveConfigWidget();

        inited = true;
    }
}
