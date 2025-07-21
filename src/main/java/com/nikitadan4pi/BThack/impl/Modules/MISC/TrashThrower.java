package com.nikitadan4pi.BThack.impl.Modules.MISC;

import com.nikitadan4pi.BThack.Core.Client.Client;
import com.nikitadan4pi.BThack.api.Events.ClientTickEvent;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.ChatUtils;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.nikitadan4pi.BThack.api.Utils.List.ItemList.ItemLists;
import com.nikitadan4pi.BThack.api.Utils.Ticker;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Formatting;

public class TrashThrower extends Module {

    public static NumberSetting delay;

    public TrashThrower() {
        super("TrashThrower",
                "lang.module.TrashThrower",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );

        delay = new NumberSetting("Delay", this, 200, 50, 1000, true);

        initSettings(
                delay
        );
    }
    private boolean firstOpened = true;
    private final Ticker ticker = new Ticker();

    @Override
    public void onEnable() {
        super.onEnable();
        ticker.reset();
        if (!nullCheck()) {
            if (firstOpened) {
                ChatUtils.sendMessage(Formatting.GRAY + "Use: " + Client.clientInfo.getChatPrefix() + ItemLists.get("TrashThrower").editBlockListCommand.getUsage());
                firstOpened = false;
            }
        }
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        if (ticker.passed(delay.getValue())) {
            for (int i = 0; i < 36; i++) {
                if (ItemLists.get("TrashThrower").items.contains(mc.player.getInventory().getStack(i).getItem())) {
                    pc.clickSlot(0, (i < 9 ? i + 36 : i), 0, SlotActionType.PICKUP);
                    pc.clickSlot(0, -999, 0, SlotActionType.PICKUP);
                }
            }
            ticker.reset();
        }
    }
}
