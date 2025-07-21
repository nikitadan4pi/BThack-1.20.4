package com.nikitadan4pi.BThack.impl.Modules.PLAYER;


import com.nikitadan4pi.BThack.Core.Client.ModuleList;
import com.nikitadan4pi.BThack.api.Events.ClientTickEvent;
import com.nikitadan4pi.BThack.api.Managers.Managers;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.SoundSystem.SoundSystem;
import com.nikitadan4pi.BThack.api.SoundSystem.Sounds;
import com.nikitadan4pi.BThack.api.Utils.InventoryUtils;
import com.nikitadan4pi.BThack.api.Utils.ItemUtils;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.nikitadan4pi.BThack.api.Utils.Ticker;
import com.nikitadan4pi.BThack.impl.Modules.CLIENT.ClientSetting;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

import java.util.Arrays;

public class AutoFirework extends Module {

    public static ModeSetting mode;

    public static NumberSetting delay;

    public static BooleanSetting swingHand;

    public AutoFirework() {
        super("AutoFirework",
                "lang.module.AutoFirework",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );

        mode = new ModeSetting("Mode", this, Arrays.asList("One", "Always"));

        delay = new NumberSetting("Delay", this, 0, 0, 5000, true, () -> mode.getValue().equals("Always"));

        swingHand = new BooleanSetting("Swing Hand", this, true);

        initSettings(
                mode,

                delay,

                swingHand
        );
    }

    Ticker ticker = new Ticker();

    @Override
    public void playOffSound() {
        if (mode.getValue().equals("Always"))
            if (ModuleList.clientSetting.toggleSound.getValue())
                SoundSystem.playSound(Sounds.MODULE_OFF, ClientSetting.volume.getValue().floatValue());
    }

    @Override
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }
        ticker.reset();

        if (mode.getValue().equals("One")) {
            useFirework(swingHand.getValue());
            toggle();
        } else super.onEnable();
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;
        if (!mc.player.isFallFlying()) return;
        if (mode.getValue().equals("One")) {
            toggle();
            return;
        }

        if (!Managers.FIREWORK_MANAGER.isUsingFireWork()) {
            if (ticker.passed(delay.getValue())) {
                useFirework(swingHand.getValue());
            }
        } else ticker.reset();
    }

    public static void useFirework(boolean swing) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player != null && mc.player.isFallFlying()) {
            int oldSlot = mc.player.getInventory().selectedSlot;
            int inventorySlot = InventoryUtils.findItem(Items.FIREWORK_ROCKET);
            if (inventorySlot != -1) {
                if (inventorySlot < 9)
                    InventoryUtils.swapItem(inventorySlot);
                else
                    InventoryUtils.swapItemOnInventory(oldSlot, inventorySlot);
                ItemUtils.useItem(Hand.MAIN_HAND, swing);
                if (inventorySlot < 9)
                    InventoryUtils.swapItem(oldSlot);
                else
                    InventoryUtils.swapItemOnInventory(oldSlot, inventorySlot);

                Managers.FIREWORK_MANAGER.resetLastClientUseFireworkTicker();
            }
        }
    }
}
