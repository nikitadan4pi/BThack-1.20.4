package com.nikitadan4pi.BThack.impl.Modules.RENDER;

import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.nikitadan4pi.BThack.api.Category.Category;
import com.nikitadan4pi.BThack.api.Events.Render.RenderWorldEvent;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.*;
import com.nikitadan4pi.BThack.api.Managers.managers.Thread.ThreadManager;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;


public class Ambience extends Module {

    public static CategorySetting skyCaregory;

    public static NumberSetting skyRed;
    public static NumberSetting skyGreen;
    public static NumberSetting skyBlue;
    public static NumberSetting starBrightness;
    public static BooleanSetting changeMoonPhase;
    public static NumberSetting moonPhase;
    public static NumberSetting cloudsRed;
    public static NumberSetting cloudsGreen;
    public static NumberSetting cloudsBlue;
    public static CategorySetting timeCategory;
    public static ModeSetting mode;
    public static NumberSetting customTime;
    public static NumberSetting spinSpeed;
    public static NumberSetting extraSpin;

    public static CategorySetting fogCategory;

    public static ColorSetting fogColor;
    public static BooleanSetting rainbow;
    public static BooleanSetting overworld;
    public static BooleanSetting nether;
    public static BooleanSetting end;

    public static long time = 0L;

    public Ambience() {
        super("Ambience",
                "lang.module.Ambience",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        skyCaregory = new CategorySetting("Sky", this);

        skyRed = new NumberSetting("Red", this, 21, 0, 255, true).inCategory(skyCaregory);
        skyGreen = new NumberSetting("Green", this, 191, 0, 255, true).inCategory(skyCaregory);
        skyBlue = new NumberSetting("Blue", this, 219, 0, 255, true).inCategory(skyCaregory);
        starBrightness = new NumberSetting("Star Bright.", this, 0.5,0,1, false).inCategory(skyCaregory);
        changeMoonPhase = new BooleanSetting("Ch. Moon Phase", this, false).inCategory(skyCaregory);
        moonPhase = new NumberSetting("Moon Phase", this, 5, 0, 7, true, changeMoonPhase::getValue).inCategory(skyCaregory);
        cloudsRed = new NumberSetting("Red", this, 255, 0, 255, false).inCategory(skyCaregory);
        cloudsGreen = new NumberSetting("Green", this, 255, 0, 255, false).inCategory(skyCaregory);
        timeCategory = new CategorySetting("Time", this);
        cloudsBlue = new NumberSetting("Blue", this, 255, 0, 255, false).inCategory(skyCaregory);

        mode = new ModeSetting("Mode", this, new ArrayList<>(Arrays.asList("Day", "Night", "Morning", "Sunset", "Spin", "Custom"))).inCategory(timeCategory);
        customTime = new NumberSetting("Custom Time", this, 10000, 1, 24000, true, () -> mode.getValue().equals("Custom")).inCategory(timeCategory);
        spinSpeed = new NumberSetting("Spin speed", this, 500,250,2500,true, () -> mode.getValue().equals("Spin")).inCategory(timeCategory);
        extraSpin = new NumberSetting("Extra Spin", this, 1, 1, 2.5, false, () -> mode.getValue().equals("Spin")).inCategory(timeCategory);

        fogCategory = new CategorySetting("Fog", this);

        fogColor = new ColorSetting("Color", this, Color.white, () -> !rainbow.getValue()).inCategory(fogCategory);
        rainbow = new BooleanSetting("Rainbow", this, false).inCategory(fogCategory);
        overworld = new BooleanSetting("Overworld", this, true).inCategory(fogCategory);
        nether = new BooleanSetting("Nether", this, true).inCategory(fogCategory);
        end = new BooleanSetting("End", this, true).inCategory(fogCategory);



        initSettings(
                skyCaregory,

                timeCategory,

                fogCategory
        );
    }
    @Override
    public void onChangeSetting(Setting<?> setting) {
        if (setting == customTime) {
            time = customTime.getValue().longValue();
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        ThreadManager.startNewThread(thread -> {
            while (this.isEnabled()) {
                if (mc.player != null && mc.world != null) {
                    String _mode = mode.getValue();

                    //ModuleList.customDayTime.arrayListInfo = _mode;

                    if (Objects.equals(_mode, "Day")) {
                        time = 5000L;
                    }
                    if (Objects.equals(_mode, "Night")) {
                        time = 17000L;
                    }
                    if (Objects.equals(_mode, "Morning")) {
                        time = 0L;
                    }
                    if (Objects.equals(_mode, "Sunset")) {
                        time = 13000L;
                    }
                    if (Objects.equals(_mode, "Custom")) {
                        time = customTime.getValue().longValue();
                    }
                    if (Objects.equals(_mode, "Spin")) {
                        double speed = spinSpeed.getValue();
                        float speedFactor = extraSpin.getValue().floatValue();

                        long newTime = (long) (this.time + ((speed * speedFactor) / 50));
                        if (newTime >= 24000L) newTime = 0L;
                        time = newTime;
                        try {
                            thread.sleep(20);
                        } catch (InterruptedException ignored) {}
                        //if (mc.world != null)
                        //    mc.world.setTimeOfDay(time);
                    } else {
                        //if (mc.world != null)
                        //    mc.world.setTimeOfDay(time);
                        try {
                            thread.sleep(200);
                        } catch (InterruptedException ignored) {}
                        //if (mc.world != null)
                        //    mc.world.setTimeOfDay(time);
                    }
                } else {
                    try {
                        thread.sleep(200);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        });
    }

    @EventSubscriber
    public void onRender(RenderWorldEvent.End e) {
        if (mc.world != null)
            mc.world.setTimeOfDay(time);
    }
}
