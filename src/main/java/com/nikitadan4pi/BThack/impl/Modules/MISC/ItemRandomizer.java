package com.nikitadan4pi.BThack.impl.Modules.MISC;

import com.nikitadan4pi.BTbot.api.Utils.Generate.GenerateNumber;
import com.nikitadan4pi.BThack.api.Events.ClientTickEvent;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.InventoryUtils;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

public class ItemRandomizer extends Module {

    public ItemRandomizer() {
        super("ItemRandomizer",
                "lang.module.ItemRandomizer",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );
    }

    @EventSubscriber
    public void onClientTick(ClientTickEvent e) {
        if (nullCheck()) return;


        InventoryUtils.swapItem(GenerateNumber.generateInt(0, 8));
    }
}
