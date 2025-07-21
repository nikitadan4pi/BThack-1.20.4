package com.nikitadan4pi.BThack.impl.Modules.CLIENT;

import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;

public class CustomFont extends Module {

    public CustomFont() {
        super("CustomFont",
                "lang.module.CustomFont",
                KeyboardUtils.RELEASE,
                MCategory.CLIENT,
                false
        );
    }

    @Override
    public void onEnable() {
        //ArrayListComponent.updateSizes();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        //ArrayListComponent.updateSizes();
        super.onDisable();
    }
}
