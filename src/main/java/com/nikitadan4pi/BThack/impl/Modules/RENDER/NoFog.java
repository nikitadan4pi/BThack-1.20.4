package com.nikitadan4pi.BThack.impl.Modules.RENDER;


import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;

public class NoFog extends Module {
    public NoFog() {
        super("NoFog",
                "lang.module.NoFog",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );
    }
}
