package com.nikitadan4pi.BThack.impl.Modules.PLAYER;

import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.nikitadan4pi.BThack.mixins.gui_and_hud.MixinDeathScreen;

/**
 * @see MixinDeathScreen
 */

public class AutoRespawn extends Module {
    public AutoRespawn() {
        super("AutoRespawn",
                "lang.module.AutoRespawn",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );
    }
}