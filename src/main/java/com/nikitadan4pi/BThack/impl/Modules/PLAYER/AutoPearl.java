package com.nikitadan4pi.BThack.impl.Modules.PLAYER;


import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.nikitadan4pi.BThack.api.Module.OneActionModule;
import com.nikitadan4pi.BThack.api.Utils.ItemUtils;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import net.minecraft.item.Items;

public class AutoPearl extends OneActionModule {

    public static BooleanSetting swingHand;

    public AutoPearl() {

        super("AutoPearl",
                "lang.module.AutoPearl",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );

        swingHand = new BooleanSetting("Swing Hand", this, true);

        initSettings(
                swingHand
        );
    }

    @Override
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }

        ItemUtils.useItem(Items.ENDER_PEARL, swingHand.getValue());
        toggle();
    }
}
