package com.nikitadan4pi.BThack.impl.Modules.MISC;

import com.nikitadan4pi.BThack.api.Managers.Managers;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;

public class CleanMemory extends Module {
    public CleanMemory() {
        super("CleanMemory",
                "lang.module.CleanMemory",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );
    }

    @Override
    public void onEnable() {
        Managers.MEMORY_MANAGER.cleanMemory();
    }
}
