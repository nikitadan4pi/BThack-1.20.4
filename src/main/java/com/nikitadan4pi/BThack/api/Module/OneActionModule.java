package com.nikitadan4pi.BThack.api.Module;

import com.nikitadan4pi.BThack.Core.Client.ModuleList;
import com.nikitadan4pi.BThack.api.Category.Category;
import com.nikitadan4pi.BThack.api.Utils.ChatUtils;
import com.nikitadan4pi.BThack.impl.Modules.CLIENT.ChatNotifications;
import net.minecraft.util.Formatting;

public class OneActionModule extends Module {

    public OneActionModule(String name, String description, int key, MCategory c, boolean autoEnabled) {
        super(name, description, key, c, autoEnabled);
    }

    public OneActionModule(String name, String description, int key, Category c, boolean autoEnabled) {
        super(name, description, key, c, autoEnabled);
    }

    @Override
    public void sendToggleMessage() {
        if (toggled && ModuleList.chatNotifications.isEnabled() && ChatNotifications.moduleToggle.getValue()) {
            ChatUtils.sendMessage(this.getName() + ": " + Formatting.YELLOW + "Toggled");
        }
    }

    @Override
    public void playOffSound() {
        //No Action
    }

    @Override
    public void toggle() {
        toggled = !toggled;
        if (toggled) {
            sendToggleMessage();
            playOnSound();
            onEnable();
            onDisable();
            toggled = false;
        } else {
            onDisable();
        }
    }

    @Override
    public void setToggled(boolean toggled) {
        this.toggled = toggled;
        if (this.toggled) {
            sendToggleMessage();
            playOnSound();
            onEnable();
            onDisable();
            this.toggled = false;
        } else {
            onDisable();
        }
    }

    @Override
    public void setQuietlyToggled(boolean toggled) {
        this.toggled = toggled;
        if (this.toggled) {
            onEnable();
            onDisable();
            this.toggled = false;
        } else {
            onDisable();
        }
    }
}
