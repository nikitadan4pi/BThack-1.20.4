package com.nikitadan4pi.BThack.impl.Modules.CLIENT;

import com.nikitadan4pi.BThack.BThack;
import com.nikitadan4pi.BThack.api.Module.OneActionModule;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;

public class HudEditor extends OneActionModule {

    public HudEditor() {
        super("HudEditor",
                "lang.module.HudEditor",
                KeyboardUtils.RELEASE,
                MCategory.CLIENT,
                false
        );
    }

    @Override
    public void playOnSound() {
        //No action
    }

    @Override
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }

        mc.setScreen(BThack.instance.hudMoverScreen);
    }
}
