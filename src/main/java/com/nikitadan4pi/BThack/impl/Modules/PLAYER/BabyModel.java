package com.nikitadan4pi.BThack.impl.Modules.PLAYER;


import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;

public class BabyModel extends Module {
    public BabyModel() {
        super("BabyModel",
                "lang.module.BabyModel",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );
    }
}
