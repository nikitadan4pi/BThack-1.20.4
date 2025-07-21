package com.nikitadan4pi.BThack.impl.Modules.MOVEMENT;

import com.nikitadan4pi.BThack.api.Events.Render.RenderWorldEvent;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.nikitadan4pi.BThack.api.Utils.Ticker;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

public class ShiftSpam extends Module {

    public static NumberSetting activeDelay;
    public static NumberSetting deActiveDelay;

    public ShiftSpam() {
        super("ShiftSpam",
                "lang.module.ShiftSpam",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );

        activeDelay = new NumberSetting("Delay Active", this, 0.1,0.05,1,false);
        deActiveDelay = new NumberSetting("Delay deActive", this, 0.1,0.05,1,false);

        initSettings(
                activeDelay,
                deActiveDelay
        );
    }

    private final Ticker ticker = new Ticker();
    private boolean sneaked = false;

    @Override
    public void onEnable() {
        super.onEnable();
        ticker.reset();
        sneaked = false;
    }

    @EventSubscriber
    public void onTick(RenderWorldEvent.Last e) {
        if (nullCheck()) return;

        if (ticker.passed(sneaked ? (activeDelay.getValue() * 1000) : (deActiveDelay.getValue() * 1000))) {
            mc.options.sneakKey.setPressed(!mc.options.sneakKey.isPressed());

            sneaked = !sneaked;
            ticker.reset();
        }
    }
}
