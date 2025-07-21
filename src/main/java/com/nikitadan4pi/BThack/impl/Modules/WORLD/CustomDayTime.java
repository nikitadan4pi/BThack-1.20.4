package com.nikitadan4pi.BThack.impl.Modules.WORLD;

import com.nikitadan4pi.BThack.Core.Client.ModuleList;
import com.nikitadan4pi.BThack.api.Events.Render.RenderWorldEvent;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.Setting;
import com.nikitadan4pi.BThack.api.Managers.managers.Thread.ThreadManager;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class CustomDayTime extends Module {

    public static ModeSetting mode;
    public static NumberSetting customTime;
    public static NumberSetting spinSpeed;
    public static NumberSetting extraSpin;


    public static long time = 0L;

    public CustomDayTime() {
        super("CustomDayTime",
                "lang.module.CustomDayTime",
                KeyboardUtils.RELEASE,
                MCategory.WORLD,
                false
        );

        mode = new ModeSetting("Mode", this, new ArrayList<>(Arrays.asList("Day", "Night", "Morning", "Sunset", "Spin", "Custom")));
        customTime = new NumberSetting("Custom Time", this, 10000, 1, 24000, true, () -> mode.getValue().equals("Custom"));
        spinSpeed = new NumberSetting("Spin speed", this, 500,250,2500,true, () -> mode.getValue().equals("Spin"));
        extraSpin = new NumberSetting("Extra Spin", this, 1, 1, 2.5, false, () -> mode.getValue().equals("Spin"));

        initSettings(
                mode,
                customTime,
                spinSpeed,
                extraSpin
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
            while (ModuleList.customDayTime.isEnabled()) {
                if (mc.player != null && mc.world != null) {
                    String _mode = mode.getValue();

                    ModuleList.customDayTime.arrayListInfo = _mode;

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

                        long newTime = (long) (CustomDayTime.time + ((speed * speedFactor) / 50));
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
