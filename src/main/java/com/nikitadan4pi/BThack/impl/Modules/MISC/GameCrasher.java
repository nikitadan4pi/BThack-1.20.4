package com.nikitadan4pi.BThack.impl.Modules.MISC;

import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;

public class GameCrasher extends Module {
    public GameCrasher() {
        super("GameCrasher",
                "lang.module.GameCrasher",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );
    }

    @Override
    public void onEnable() {
        this.setToggled(false);
        throw new RuntimeException("Your game is crashed using the BThack module \"GameCrasher\".");
    }
}
