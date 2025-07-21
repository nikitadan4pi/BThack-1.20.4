package com.nikitadan4pi.BThack.impl.Modules.MISC;

import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;

public class PortalGod extends Module {

    public PortalGod() {
        super("PortalGod",
                "lang.module.PortalGod",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );
    }
}
