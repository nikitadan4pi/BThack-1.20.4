package com.nikitadan4pi.BThack.Core.Client.Systems;

import com.nikitadan4pi.BThack.Core.Client.Client;
import com.nikitadan4pi.BThack.api.Events.InputEvent;
import com.nikitadan4pi.BThack.api.Interfaces.Mc;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

public class KeyHandler implements Mc {

    @EventSubscriber
    public void onKey(InputEvent.KeyInputEvent e) {
        if (e.getAction() == InputEvent.KeyInputEvent.Action.PRESS) {
            if (e.getKeyCode() != KeyboardUtils.RELEASE && mc.currentScreen == null) {
                Client.keyPress(e.getKeyCode());
            }

            if (!KeyboardUtils.getActiveKeys().contains(e.getKeyCode())) {
                KeyboardUtils.addActiveKey(e.getKeyCode());
            }
        } else if (e.getAction() == InputEvent.KeyInputEvent.Action.RELEASE) {
            if (KeyboardUtils.getActiveKeys().contains(e.getKeyCode())) {
                KeyboardUtils.removeActiveKey(e.getKeyCode());
            }
        }
    }
}
