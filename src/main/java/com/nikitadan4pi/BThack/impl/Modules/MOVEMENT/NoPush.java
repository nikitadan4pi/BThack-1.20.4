package com.nikitadan4pi.BThack.impl.Modules.MOVEMENT;

import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;

public class NoPush extends Module {

    public static BooleanSetting blocks;
    public static BooleanSetting entities;
    public static BooleanSetting liquids;

    public NoPush() {
        super("NoPush",
                "lang.module.NoPush",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );

        blocks = new BooleanSetting("Blocks", this, true);
        entities = new BooleanSetting("Entities", this, false);
        liquids = new BooleanSetting("Liquids", this, true);

        initSettings(
                blocks,
                entities,
                liquids
        );
    }
}
