package com.nikitadan4pi.BThack.impl.Modules.RENDER;

import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;

public class CameraClip extends Module {

    public CameraClip() {
        super("CameraClip",
                "lang.module.CameraClip",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );
    }
}
