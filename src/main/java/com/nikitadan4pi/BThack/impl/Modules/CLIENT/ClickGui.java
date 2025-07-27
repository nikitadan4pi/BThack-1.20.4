package com.nikitadan4pi.BThack.impl.Modules.CLIENT;

import com.nikitadan4pi.BThack.BThack;
import com.nikitadan4pi.BThack.Core.Client.ModuleList;
import com.nikitadan4pi.BThack.Core.Render.Utils.ColorUtils;
import com.nikitadan4pi.BThack.Core.Render.Utils.RainbowUtils;
import com.nikitadan4pi.BThack.api.Animation.Easing;
import com.nikitadan4pi.BThack.api.GuiSystem.BThackScreens;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.*;
import com.nikitadan4pi.BThack.api.Module.OneActionModule;
import com.nikitadan4pi.BThack.api.Shader.ShaderProgram;
import com.nikitadan4pi.BThack.api.Shader.Shaders;
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
    public static ColorSetting textColor;
    public static BooleanSetting gradient;
    public static BooleanSetting rainbow;
    public static ColorSetting color1;
    public static ColorSetting color2;
    public static NumberSetting scale;
    public static NumberSetting speed;

    public static BooleanSetting frameOutline;
    public static BooleanSetting moduleOutline;
    public static BooleanSetting settingsOutline;

    public static NumberSetting opacity;

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

        color = new ColorSetting("ClickGui Color", this, new Color(119, 0, 189));
        backgroundColor = new ColorSetting("Background Color", this, new Color(17, 17, 17)).withBlockedAlpha();
        textColor = new ColorSetting("Font Color", this, Color.white);
        gradient = new BooleanSetting("Gradient", this, true);
        rainbow = new BooleanSetting("Rainbow", this, false);
        color1 = new ColorSetting("Color1", this, new Color(195, 85, 251), gradient::getValue).withBlockedAlpha();
        color2 = new ColorSetting("Color2", this, new Color(105, 0, 166), gradient::getValue).withBlockedAlpha();

        scale = new NumberSetting("Scale", this, 1, 0.3, 4, false, () -> rainbow.getValue() || gradient.getValue());
        speed = new NumberSetting("Speed", this, 1, 0.3, 4, false, () -> rainbow.getValue() || gradient.getValue());


        frameOutline = new BooleanSetting("Frame Outline", this, true);
        moduleOutline = new BooleanSetting("Module Outline", this, true);
        settingsOutline = new BooleanSetting("Settings Outline", this, true);

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
                textColor,
                gradient,
                rainbow,
                color1,
                color2,
                scale,
                speed,

                frameOutline,
                moduleOutline,
                settingsOutline,

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
        if (setting == rainbow && rainbow.getValue()) gradient.setValue(false);
        if (setting == gradient && gradient.getValue()) rainbow.setValue(false);
    }

    @Override
    public void playOnSound() {
        //No action
    }

    public ShaderProgram getCurrentShader() {
        if (gradient.getValue()) return Shaders.INSTANCE.XY_GRADIENT;
        else return Shaders.INSTANCE.X_RAINBOW;
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
            return color.getValue().hashCode();
    }

    public void prepareCurrentShader(float alpha, float brightness) {
        if (gradient.getValue()) {
            Shaders.INSTANCE.XY_GRADIENT.setUniformValue("scale", scale.getValue().floatValue());
            Shaders.INSTANCE.XY_GRADIENT.setUniformValue("speed", speed.getValue().floatValue());
            Shaders.INSTANCE.XY_GRADIENT.setUniformValue("brightness", brightness);

            Shaders.INSTANCE.XY_GRADIENT.setUniformValue("color1", color1.getValue().getRed() / 255f, color1.getValue().getGreen() / 255f, color1.getValue().getBlue() / 255f, alpha);
            Shaders.INSTANCE.XY_GRADIENT.setUniformValue("color2", color2.getValue().getRed() / 255f, color2.getValue().getGreen() / 255f, color2.getValue().getBlue() / 255f, alpha);
        } else {
            Shaders.INSTANCE.X_RAINBOW.setUniformValue("alpha", alpha);
            Shaders.INSTANCE.X_RAINBOW.setUniformValue("brightness", brightness);
            Shaders.INSTANCE.X_RAINBOW.setUniformValue("scale", scale.getValue().floatValue());
            Shaders.INSTANCE.X_RAINBOW.setUniformValue("speed", speed.getValue().floatValue());
        }
    }

    public boolean isShaderEnabled() {
        return gradient.getValue() || rainbow.getValue();
    }

    public static float applyGuiScale(float cord) {
        return (float) (cord * guiScale.getValue());
    }

    public static int applyGuiScale(int cord) {
        return (int) (cord * guiScale.getValue());
    }
}
