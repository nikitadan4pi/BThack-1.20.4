package com.nikitadan4pi.BThack.impl.Modules.MOVEMENT;


import com.nikitadan4pi.BThack.api.Events.ClientTickEvent;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.nikitadan4pi.BThack.mixins.accessor.ILivingEntity;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

public class NoJumpDelay extends Module {
    public NoJumpDelay() {
        super("NoJumpDelay",
                "lang.module.NoJumpDelay",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );

    }

    @EventSubscriber
    public void onUpdate(ClientTickEvent e) {
        if (nullCheck()) return;

        ((ILivingEntity) mc.player).setJumpingCooldown(0);
    }
}