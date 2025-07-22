package com.nikitadan4pi.BThack.impl.Modules.MOVEMENT;

import com.nikitadan4pi.BThack.Core.Client.ModuleList;
import com.nikitadan4pi.BThack.api.Events.ClientTickEvent;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.nikitadan4pi.BThack.mixins.accessor.IEntity;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

public class ElytraFastClose extends Module {

    public ElytraFastClose() {
        super("ElytraFastClose",
                "lang.module.ElytraFastClose",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        if (mc.player.verticalCollision) {
            if (ModuleList.elytraFlight.isEnabled()) return;
            IEntity entity = (IEntity) mc.player;
            if (entity.invokeGetFlag(7))
                entity.invokeSetFlag(7, false);
        }
    }
}
