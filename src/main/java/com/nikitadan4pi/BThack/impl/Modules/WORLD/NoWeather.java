package com.nikitadan4pi.BThack.impl.Modules.WORLD;


import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;

public class NoWeather extends Module {
    public NoWeather() {
        super("NoWeather",
                "lang.module.NoWeather",
                KeyboardUtils.RELEASE,
                MCategory.WORLD,
                false
        );

    }
}
