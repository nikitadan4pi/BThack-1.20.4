package com.nikitadan4pi.BThack.impl.Modules.MOVEMENT;

import com.nikitadan4pi.BThack.Core.Client.ModuleList;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Thread.IThread;
import com.nikitadan4pi.BThack.api.Managers.managers.Thread.ThreadManager;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;

public class CameraRotator extends Module {
    public static NumberSetting speed;
    public static BooleanSetting inversion;

    public CameraRotator() {
        super("CameraRotator",
                "lang.module.CameraRotator",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );

        speed = new NumberSetting("Speed", this, 1,0.1,4,false);
        inversion = new BooleanSetting("Inversion", this, false);

        initSettings(
                speed,
                inversion
        );
    }

    @Override
    public void onEnable() {
        ThreadManager.startNewThread(new IThread() {
            double a = 0;
            float b = 0;

            @Override
            public void start(Thread thread) {
                while (ModuleList.cameraRotator.isEnabled()) {
                    if (!nullCheck()) {
                        a = speed.getValue();
                        b = (float) (1080 * a);
                        b = b / 950;
                        if (!inversion.getValue()) {
                            mc.player.yaw = mc.player.yaw + b;
                        } else {
                            mc.player.yaw = mc.player.yaw - b;
                        }
                        try {
                            thread.sleep(1);
                        } catch (InterruptedException ignored) {
                        }
                    } else Thread.yield();
                }
            }
        });
    }
}
