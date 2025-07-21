package com.nikitadan4pi.BThack.impl.Modules.RENDER;


import com.nikitadan4pi.BThack.api.Events.ClientTickEvent;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.effect.StatusEffects;

public class AntiHazard extends Module {
    public AntiHazard() {
        super("AntiHazard",
                "lang.module.AntiHazard",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        if (mc.player.hasStatusEffect(StatusEffects.BLINDNESS)) {
            mc.player.removeStatusEffect(StatusEffects.BLINDNESS);
        }
        if (mc.player.hasStatusEffect(StatusEffects.NAUSEA)) {
            mc.player.removeStatusEffect(StatusEffects.NAUSEA);
        }
    }
}
