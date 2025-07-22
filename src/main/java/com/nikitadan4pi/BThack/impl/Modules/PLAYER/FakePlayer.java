package com.nikitadan4pi.BThack.impl.Modules.PLAYER;

import com.nikitadan4pi.BThack.api.Events.DisconnectEvent;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.nikitadan4pi.BThack.api.Utils.PlayerUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.Entity;

public class FakePlayer extends Module {

    public FakePlayer() {
        super("FakePlayer",
                "lang.module.FakePlayer",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );
    }

    private Entity fakePlayer;

    @Override
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }
        super.onEnable();
        if (mc.player != null && mc.world != null) {
            fakePlayer = PlayerUtils.createNewFakePlayer(mc.player, "FakePlayer");
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (fakePlayer != null) {
            PlayerUtils.removeEntity(fakePlayer);
            fakePlayer = null;
        }
    }

    @EventSubscriber
    public void onDisconnect(DisconnectEvent e) {
        fakePlayer = null;
        toggle();
    }
}
