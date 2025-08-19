package com.nikitadan4pi.BThack.impl.Modules.CLIENT;

import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.nikitadan4pi.BThack.api.Module.OneActionModule;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;

import java.util.ArrayList;

public class ClientSetting extends OneActionModule {

    public static BooleanSetting bthackMainMenu;
    public static ModeSetting language;
    public static BooleanSetting playStartMusic;
    public static BooleanSetting toggleSound;
    public static NumberSetting volume;
    public static BooleanSetting movementFix;

    public ClientSetting(){
        super("Client Setting",
                "lang.module.ClientSettings",
                KeyboardUtils.RELEASE,
                MCategory.CLIENT,
                false
        );

        bthackMainMenu = new BooleanSetting("Main Menu", this, true);
        language = new ModeSetting("Lang", this, new ArrayList<>(LanguageSystem.getLoadedLangs()));
        playStartMusic = new BooleanSetting("Play Start Music", this, true);
        toggleSound = new BooleanSetting("ToggleSound", this, true);
        volume = new NumberSetting("Volume", this, 0.25, 0.1, 1, false);
        movementFix = new BooleanSetting("Movement Fix", this, false);

        initSettings(
                bthackMainMenu,
                language,
                playStartMusic,
                toggleSound,
                volume,
                movementFix
        );
    }
}
