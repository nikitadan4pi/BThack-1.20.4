package com.nikitadan4pi.BThack.impl.Modules.PLAYER;

import com.nikitadan4pi.BThack.Core.Client.Client;
import com.nikitadan4pi.BThack.Core.Client.ModuleList;
import com.nikitadan4pi.BThack.api.Events.ClientTickEvent;
import com.nikitadan4pi.BThack.api.Events.GuiOpenEvent;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.Setting;
import com.nikitadan4pi.BThack.api.Managers.managers.Thread.ThreadManager;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.ChatUtils;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.nikitadan4pi.BThack.api.Utils.List.ItemList.ItemLists;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Formatting;

import java.util.Arrays;

public class ChestStealer extends Module {

    public static NumberSetting stealDelay;

    public static ModeSetting steal;
    public static ModeSetting mode;

    public static BooleanSetting autoClose;

    public ChestStealer() {
        super("ChestStealer",
                "lang.module.ChestStealer",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );

        stealDelay = new NumberSetting("Steal Delay", this, 100,0,1000,true);

        steal = new ModeSetting("Steal", this, Arrays.asList("All", "Select"));
        mode = new ModeSetting("Mode", this, Arrays.asList("WhiteList", "BlackList"), () -> steal.getValue().equals("Select"));

        autoClose = new BooleanSetting("Auto Close", this, true);

        initSettings(
                stealDelay,

                steal,
                mode,

                autoClose
        );
    }

    public static boolean active = false;

    @Override
    public void onChangeSetting(Setting<?> setting) {
        if (setting == steal) {
            if (steal.getValue().equals("Select"))
                ChatUtils.sendMessage(Formatting.GRAY + "Use: " + Client.clientInfo.getChatPrefix() + ItemLists.get("ChestStealer").editBlockListCommand.getUsage());
        }
    }

    @EventSubscriber
    public void onSetScreen(GuiOpenEvent e) {
        if (!(mc.currentScreen instanceof GenericContainerScreen)) active = false;
    }

    @EventSubscriber
    public void onUpdate(ClientTickEvent e) {
        if (nullCheck()) return;

        if (mc.player.currentScreenHandler instanceof GenericContainerScreenHandler) {
            if (!active) {
                ThreadManager.startNewThread(thread -> {
                    if (mc.player.currentScreenHandler instanceof GenericContainerScreenHandler container) {
                        if (container.getInventory().isEmpty() || checkFullInventory()) {
                            while (mc.currentScreen instanceof GenericContainerScreen) {
                                try {
                                    thread.sleep(100);
                                } catch (InterruptedException ignored) {}
                            }
                            ChestStealer.active = false;
                            thread.stop();
                        }
                        for (int index = 0; index < container.slots.size() - 36; ++index) {
                            if (mc.player.currentScreenHandler instanceof GenericContainerScreenHandler && ModuleList.chestStealer.isEnabled()) {
                                if (checkFullInventory()) break;
                                if (filterStack(container.getInventory().getStack(index))) {
                                    pc.clickSlot(container.syncId, index, 0, SlotActionType.QUICK_MOVE);
                                    try {
                                        thread.sleep(stealDelay.getValue().longValue());
                                    } catch (InterruptedException ignored) {}
                                }

                                if (container.getInventory().isEmpty()) {
                                    if (autoClose.getValue()) {
                                        pc.closeScreen();
                                        ChestStealer.active = false;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                });
                active = true;
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        active = false;
    }

    public boolean filterStack(ItemStack stack) {
        if (steal.getValue().equals("All")) return stack.getItem() != Items.AIR;
        else {
            if (mode.getValue().equals("WhiteList")) return ItemLists.get("ChestStealer").items.contains(stack.getItem());
            else return !ItemLists.get("ChestStealer").items.contains(stack.getItem());
        }
    }

    public boolean checkFullInventory() {
        for (int i = 0; i < 36; i++) {
            if (mc.player.getInventory().getStack(i).getItem() == Items.AIR) return false;
        }
        return true;
    }
}
