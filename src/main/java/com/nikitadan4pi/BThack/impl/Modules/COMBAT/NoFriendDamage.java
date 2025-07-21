package com.nikitadan4pi.BThack.impl.Modules.COMBAT;

import com.nikitadan4pi.BThack.api.Events.Entity.AttackEntityEvent;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Social.SocialManagers;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

public class NoFriendDamage extends Module {
    public NoFriendDamage() {
        super("NoFriendDamage",
                "lang.module.NoFriendDamage",
                KeyboardUtils.RELEASE,
                MCategory.COMBAT,
                false
        );
    }

    @EventSubscriber
    public void onPacket(AttackEntityEvent e) {
        if (nullCheck()) return;

        if (SocialManagers.FRIENDS.contains(e.getEntity().getDisplayName().getString())) {
            e.setCancelled(true);
        }
    }
}
