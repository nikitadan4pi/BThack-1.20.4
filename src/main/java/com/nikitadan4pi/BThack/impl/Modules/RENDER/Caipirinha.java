package com.nikitadan4pi.BThack.impl.Modules.RENDER;

import com.nikitadan4pi.BThack.Core.Render.BThackRender;
import com.nikitadan4pi.BThack.api.Events.Render.RenderHudPostEvent;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.nikitadan4pi.BThack.api.Utils.Ticker;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.ferra13671.TextureUtils.GLTexture;
import com.ferra13671.TextureUtils.PathMode;

import java.util.ArrayList;
import java.util.Arrays;

public class Caipirinha extends Module {

    public static NumberSetting size;
    public static NumberSetting speed;

    public Caipirinha() {
        super("Caipirinha",
                "lang.module.Caipirinha",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        size = new NumberSetting("Size", this, 7, 15, 5, true);
        speed = new NumberSetting("Speed", this, 2.2, 1.4, 2.70, false);

        initSettings(
                size,
                speed
        );
    }

    private final Ticker ticker = new Ticker();
    private double currentCaipirinha = 0;

    @Override
    public void onEnable() {
        super.onEnable();
        ticker.reset();
    }

    ArrayList<GLTexture> caipirinhi = new ArrayList<>(Arrays.asList(
            GLTexture.fromPath("assets/bthack/caipirinha/caipirinha1.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA, true),
            GLTexture.fromPath("assets/bthack/caipirinha/caipirinha2.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA, true),
            GLTexture.fromPath("assets/bthack/caipirinha/caipirinha3.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA, true),
            GLTexture.fromPath("assets/bthack/caipirinha/caipirinha4.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA, true),
            GLTexture.fromPath("assets/bthack/caipirinha/caipirinha5.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA, true),
            GLTexture.fromPath("assets/bthack/caipirinha/caipirinha6.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA, true)
    ));

    @EventSubscriber
    public void onGuiRender(RenderHudPostEvent e) {
        if (nullCheck()) return;

        if (ticker.passed(50)) {

            currentCaipirinha += 0.15 * speed.getValue();

            if (currentCaipirinha > 6)
                currentCaipirinha = 0;

            ticker.reset();
        }

        int _size = mc.getWindow().getScaledWidth() / size.getValue().intValue();

        float x = (mc.getWindow().getScaledWidth() / 2f) + ((mc.getWindow().getScaledWidth() / 4f) - (_size / 2f));
        float y = (float) ((mc.getWindow().getScaledHeight() - _size) + (_size * 0.065));
        BThackRender.drawTextureRect(caipirinhi.get((int) currentCaipirinha), x, y, x + _size, y + _size);
    }
}
