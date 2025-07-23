package com.nikitadan4pi.BThack.api.GuiSystem;

import com.nikitadan4pi.BThack.api.Gui.Screen.ClickGui.ClickGuiScreen;
import com.nikitadan4pi.BThack.api.Gui.Screen.ExitScreen;
import com.nikitadan4pi.BThack.api.Gui.Screen.HudEditor.HudEditorScreen;
import com.nikitadan4pi.BThack.api.Gui.Screen.MainMenu.BThackCreditsScreen;
import com.nikitadan4pi.BThack.api.Gui.Screen.MainMenu.BThackMainMenuScreen;

public class BThackScreens {
    private static boolean inited = false;

    public static ClickGuiScreen CLICK_GUI;
    public static BThackMainMenuScreen BTHACK_MAIN_MENU;
    public static HudEditorScreen HUD_EDITOR;
    public static BThackCreditsScreen BTHACK_CREDITS;
    public static ExitScreen EXIT;


    public static void init() {
        if (inited) return;

        CLICK_GUI = new ClickGuiScreen();
        BTHACK_MAIN_MENU = new BThackMainMenuScreen();
        HUD_EDITOR = new HudEditorScreen();
        BTHACK_CREDITS = new BThackCreditsScreen(BTHACK_MAIN_MENU);
        EXIT = new ExitScreen();
        inited = true;
    }
}
