package com.ferra13671.BThack.impl.Modules.CLIENT;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.Core.Render.Utils.RainbowUtils;
import com.ferra13671.BThack.api.Animation.Easing;
import com.ferra13671.BThack.api.Managers.managers.ColourTheme.ColorTheme;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.*;
import com.ferra13671.BThack.api.Module.OneActionModule;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClickGui extends OneActionModule {

    public static int INT_OPACITY;
    public static int BACKGROUND_COLOR;
    public static int BACKGROUND_HOVERED_COLOR;

    public static ModeSetting activeTheme;
    public static BooleanSetting customColor;
    public static ColorSetting color;
    public static ColorSetting backgroundColor;
    public static BooleanSetting rainbow;
    public static NumberSetting rainbowSpeed;

    public static BooleanSetting frameOutline;
    public static BooleanSetting moduleOutline;
    public static BooleanSetting settingsOutline;

    public static NumberSetting opacity;

    public static BooleanSetting oldStyle;

    public static NumberSetting animationTime;
    public static ModeSetting easing;

    public static BooleanSetting shouldPause;

    public static NumberSetting guiScale;

    public ClickGui() {
        super("ClickGui",
                "lang.module.ClickGui",
                KeyboardUtils.KEY_RSHIFT,
                MCategory.CLIENT,
                false
        );

        ArrayList<String> options = new ArrayList<>();

        for (ColorTheme theme : Managers.COLOR_THEME_MANAGER.getColorThemes()) {
            options.add(theme.getName());
        }

        activeTheme = new ModeSetting("Theme", this, options);
        customColor = new BooleanSetting("Custom Color", this, false, () -> !rainbow.getValue());
        color = new ColorSetting("ClickGui Color", this, new Color(119, 0, 189), () -> !rainbow.getValue() && customColor.getValue());
        backgroundColor = new ColorSetting("Background Color", this, new Color(17, 17, 17), () -> !rainbow.getValue() && customColor.getValue()).withBlockedAlpha();
        rainbow = new BooleanSetting("Rainbow", this, false);
        rainbowSpeed = new NumberSetting("Rainbow speed", this, 2, 1, 4, true, () -> rainbow.getValue());


        frameOutline = new BooleanSetting("Frame Outline", this, true);
        moduleOutline = new BooleanSetting("Module Outline", this, true);
        settingsOutline = new BooleanSetting("Settings Outline", this, true);

        oldStyle = new BooleanSetting("Old Style", this, false);
        opacity = new NumberSetting("Opacity", this, 0.76, 0.1, 1, false);

        animationTime = new NumberSetting("Anim Time", this, 400, 250, 1500, true);
        List<String> easingList = new ArrayList<>();
        for (Easing eas : Easing.values()) {
            easingList.add(eas.name());
        }
        easing = new ModeSetting("Easing", this, easingList);
        easing.setValue("CIRC_OUT");
        easing.setIndex(17);

        shouldPause = new BooleanSetting("Should Pause", this, true);

        guiScale = new NumberSetting("Gui Scale", this, 1, 0.5, 1.5, false, () -> false);

        initSettings(
                activeTheme,
                color,
                backgroundColor,
                customColor,
                rainbow,
                rainbowSpeed,

                frameOutline,
                moduleOutline,
                settingsOutline,

                oldStyle,

                opacity,

                animationTime,
                easing,

                shouldPause,

                guiScale
        );
    }

    @Override
    public void onChangeSetting(Setting<?> setting) {
        updateColorTheme();
        if (setting == opacity) {
            INT_OPACITY = Math.min(255, (int) (255 * opacity.getValue()));
            BACKGROUND_COLOR = ColorUtils.integrateAlpha(ModuleList.clickGui.backgroundColor.getValue().hashCode(), INT_OPACITY);
            BACKGROUND_HOVERED_COLOR = ColorUtils.integrateAlpha(ModuleList.clickGui.backgroundColor.getBrighterValue().hashCode(), INT_OPACITY);
        }
    }

    @Override
    public void playOnSound() {
        //No action
    }

    @Override
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }

        if (mc.currentScreen == null) {
            BThack.instance.clickGui.firstIgnore = true;
            mc.setScreen(BThack.instance.clickGui);
        }

        toggle();
    }

    public void updateColorTheme() {
        for (ColorTheme theme : Managers.COLOR_THEME_MANAGER.getColorThemes()) {
            if (Objects.equals(ClickGui.activeTheme.getValue(), theme.getName())) {
                Client.clientInfo.setColorTheme(theme);
            }
        }
    }

    public static Easing getCurrentEasing() {
        return Easing.valueOf(easing.getValue());
    }

    public static int getClickGuiColor(boolean allowRainbow) {
        if (rainbow.getValue() && allowRainbow) {
            int rainbowType = rainbowSpeed.getValue().intValue();
            float speed = RainbowUtils.getRainbowRectSpeed(rainbowType)[0];
            int delay = (int) RainbowUtils.getRainbowRectSpeed(rainbowType)[1];

            return ColorUtils.rainbow(delay, speed);
        } else if (customColor.getValue()) {
            return color.getValue().hashCode();
        } else {
            return new Color(Client.clientInfo.getColorTheme().getModuleEnabledColour()).hashCode();
        }
    }

    public boolean isShaderEnabled() {
        return rainbow.getValue();
    }

    public static float applyGuiScale(float cord) {
        return (float) (cord * guiScale.getValue());
    }

    public static int applyGuiScale(int cord) {
        return (int) (cord * guiScale.getValue());
    }
}
