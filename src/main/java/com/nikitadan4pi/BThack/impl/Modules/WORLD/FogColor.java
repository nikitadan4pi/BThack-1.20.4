package com.nikitadan4pi.BThack.impl.Modules.WORLD;

import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.ColorSetting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;

import java.awt.*;

public class FogColor extends Module {

    public static ColorSetting fogColor;
    public static BooleanSetting rainbow;

    public static BooleanSetting overworld;
    public static BooleanSetting nether;
    public static BooleanSetting end;

    public FogColor() {
        super("FogColor",
                "lang.module.FogColor",
                KeyboardUtils.RELEASE,
                MCategory.WORLD,
                false
        );

        fogColor = new ColorSetting("Color", this, Color.white, () -> !rainbow.getValue());
        rainbow = new BooleanSetting("Rainbow", this, false);

        overworld = new BooleanSetting("Overworld", this, true);
        nether = new BooleanSetting("Nether", this, true);
        end = new BooleanSetting("End", this, true);

        initSettings(
                fogColor,
                rainbow,
                overworld,
                nether,
                end
        );
    }
}
