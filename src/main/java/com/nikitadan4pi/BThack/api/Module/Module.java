package com.nikitadan4pi.BThack.api.Module;

import com.nikitadan4pi.BTbot.api.Utils.Controller.ClientPlayerController;
import com.nikitadan4pi.BThack.BThack;
import com.nikitadan4pi.BThack.Core.Client.ModuleList;
import com.nikitadan4pi.BThack.api.Category.Categories;
import com.nikitadan4pi.BThack.api.Category.Category;
import com.nikitadan4pi.BThack.api.Managers.Managers;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.Setting;
import com.nikitadan4pi.BThack.api.SoundSystem.SoundSystem;
import com.nikitadan4pi.BThack.api.SoundSystem.Sounds;
import com.nikitadan4pi.BThack.api.Utils.ChatUtils;
import com.nikitadan4pi.BThack.impl.Modules.CLIENT.ChatNotifications;
import com.nikitadan4pi.BThack.impl.Modules.CLIENT.ClientSetting;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Formatting;

public class Module {
    public final String name;
    private final String description;
    public boolean toggled;
    private final boolean autoEnabled;
    private int keyCode;
    private final Category category;
    public static MinecraftClient mc = MinecraftClient.getInstance();
    public final ClientPlayerController pc;

    public String arrayListInfo = "";

    public boolean visible = true;
    public boolean allowRemapVisible = true;
    public boolean allowRemapKeyCode = true;

    public Module(String name, String description, int key, MCategory c, boolean autoEnabled) {
        this(name, description, key, c.category, autoEnabled);
    }

    public Module(String name, String description, int key, Category c, boolean autoEnabled) {
        this.name = name;
        this.description = description.endsWith(".") ? description : !description.startsWith(".lang") ? description : description + ".";
        this.keyCode = key;
        this.category = c;
        this.autoEnabled = autoEnabled;

        pc = BThack.instance.playerController;
    }

    public boolean isEnabled() {
        return toggled;
    }

    public boolean isAutoEnabled() {
        return autoEnabled;
    }

    public int getKey() {
        return keyCode;
    }

    public static boolean nullCheck() {
        MinecraftClient mc = MinecraftClient.getInstance();
        return mc.player == null || mc.world == null;
    }

    public void onEnable() {
        BThack.EVENT_BUS.register(this);
    }

    public void onDisable() {
        BThack.EVENT_BUS.unregister(this);
    }

    public void onChangeSetting(Setting<?> setting) {}

    public void playOnSound() {
        if (ModuleList.clientSetting.toggleSound.getValue())
            SoundSystem.playSound(Sounds.MODULE_ON, ClientSetting.volume.getValue().floatValue());
    }

    public void playOffSound() {
        if (ModuleList.clientSetting.toggleSound.getValue())
            SoundSystem.playSound(Sounds.MODULE_OFF, ClientSetting.volume.getValue().floatValue());
    }


    public void setKey(int key) {
        this.keyCode = key;
    }

    public Category getCategory() {
        return category;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        if (description.startsWith("lang.")) {
            return LanguageSystem.translate(description);
        } else {
            return this.description;
        }
    }

    public String getChatName() {
        return "[" + this.name + "]";
    }

    public void sendNotification(String text) {
        if (ModuleList.chatNotifications.isEnabled() && ChatNotifications.moduleMessages.getValue()) {
            ChatUtils.sendMessage(getChatName() + " " + text);
        }
    }


    public enum MCategory {
        COMBAT(Categories.COMBAT),
        MISC(Categories.MISC),
        CLIENT(Categories.CLIENT),
        RENDER(Categories.RENDER),
        MOVEMENT(Categories.MOVEMENT),
        PLAYER(Categories.PLAYER);


        public final Category category;
        MCategory(Category category) {
            this.category = category;
        }
    }

    public void toggle() {
        toggled = !toggled;
        if (toggled) {
            sendToggleMessage();
            playOnSound();
            onEnable();
        } else {
            sendToggleMessage();
            playOffSound();
            onDisable();
        }
    }

    public void setToggled(boolean toggled) {
        if (this.toggled == toggled) return;
        this.toggled = toggled;
        if (this.toggled) {
            sendToggleMessage();
            playOnSound();
            onEnable();
        } else {
            sendToggleMessage();
            playOffSound();
            onDisable();
        }
    }

    public boolean isVisible(){return this.visible;}

    public void setVisible(boolean visible){this.visible = visible;}

    public void setQuietlyToggled(boolean toggled) {
        if (this.toggled == toggled) return;
        this.toggled = toggled;
        if (this.toggled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public void initSettings(Setting<?>... settings) {
        for (Setting<?> setting : settings)
            Managers.SETTINGS_MANAGER.addModuleSetting(setting);
    }

    public void sendToggleMessage() {
        if (ModuleList.chatNotifications.isEnabled() && ChatNotifications.moduleToggle.getValue()) {
            if (toggled) {
                ChatUtils.sendMessage(this.getName() + ": " + Formatting.GREEN + "Enabled");
            } else {
                ChatUtils.sendMessage(this.getName() + ": " + Formatting.RED + "Disabled");
            }
        }
    }

}
