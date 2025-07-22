package com.nikitadan4pi.BThack.impl.Modules.WORLD;

import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;

@Deprecated
public class AutoFarm extends Module {

    public AutoFarm() {
        super("AutoFarm",
                "",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );
    }
}
