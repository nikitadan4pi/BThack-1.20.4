package com.nikitadan4pi.BThack.impl.Modules.CLIENT;

import com.nikitadan4pi.BThack.BThack;
import com.nikitadan4pi.BThack.Core.Client.ModuleList;
import com.nikitadan4pi.BThack.Core.Render.Utils.ColorUtils;
import com.nikitadan4pi.BThack.Core.Render.Utils.RainbowUtils;
import com.nikitadan4pi.BThack.api.Animation.Easing;
import com.nikitadan4pi.BThack.api.GuiSystem.BThackScreens;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.*;
import com.nikitadan4pi.BThack.api.Module.OneActionModule;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ClickGui extends OneActionModule {

    public static int INT_OPACITY;
    public static int BACKGROUND_COLOR;
    public static int BACKGROUND_HOVERED_COLOR;

    //public static CategorySetting test;
    public static ColorSetting color;
    public static ColorSetting backgroundColor;
    public static ColorSetting fontColor;
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

        //test = new CategorySetting("test", this);

        color = new ColorSetting("ClickGui Color", this, new Color(119, 0, 189), () -> !rainbow.getValue());
        backgroundColor = new ColorSetting("Background Color", this, new Color(17, 17, 17), () -> !rainbow.getValue()).withBlockedAlpha();
        fontColor = new ColorSetting("Font Color", this, Color.white);
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
                //test,
                color,
                backgroundColor,
                fontColor,
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
            BThackScreens.CLICK_GUI.firstIgnore = true;
            mc.setScreen(BThackScreens.CLICK_GUI);
        }

        toggle();
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
        } else {
            return color.getValue().hashCode();
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
