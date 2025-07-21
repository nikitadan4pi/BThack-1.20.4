package com.nikitadan4pi.BThack.impl.Modules.RENDER;

import com.nikitadan4pi.BThack.Core.Render.BThackRender;
import com.nikitadan4pi.BThack.Core.Render.Line.RenderLine;
import com.nikitadan4pi.BThack.api.Events.Entity.AttackEntityEvent;
import com.nikitadan4pi.BThack.api.Events.Render.RenderWorldEvent;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.nikitadan4pi.BThack.api.Utils.Ticker;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.Entity;

import java.util.Arrays;

public class AttackTrace extends Module {

    public static NumberSetting renderTime;

    public AttackTrace() {
        super("AttackTrace",
                "lang.module.AttackTrace",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        renderTime = new NumberSetting("Render Time", this, 20, 5, 50, false);

        initSettings(
                renderTime
        );
    }

    private Entity attackEntity = null;
    private final Ticker ticker = new Ticker();

    @EventSubscriber
    public void onRender(RenderWorldEvent.Last e) {
        if (nullCheck()) return;

        if (attackEntity != null && attackEntity.isAlive()) {
            if (mc.player.distanceTo(attackEntity) < 20) {
                BThackRender.lineRender.prepareLineRenderer();
                BThackRender.lineRender.renderLines(Arrays.asList(new RenderLine(attackEntity, 1, 0.5f, 0.5f, 1)));
                BThackRender.lineRender.stopLineRenderer();
            }
        }
        if (ticker.passed(renderTime.getValue() * 1000)) {
            attackEntity = null;
        }
    }

    @EventSubscriber
    public void onAttack(AttackEntityEvent e) {
        if (nullCheck()) return;

        attackEntity = e.getEntity();
        ticker.reset();
    }
}
