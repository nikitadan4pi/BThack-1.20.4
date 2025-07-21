package com.nikitadan4pi.BThack.impl.Modules.RENDER;

import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;

public class PasswordHider extends Module {

    public PasswordHider() {
        super("PasswordHider",
                "lang.module.PasswordHider",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );
    }
}
