package com.nikitadan4pi.BThack.impl.Modules.MOVEMENT;

import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;

public class NoSRotations extends Module {
    public NoSRotations() {
        super("NoSRotations",
                "lang.module.NoSRotations",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );
    }
}
