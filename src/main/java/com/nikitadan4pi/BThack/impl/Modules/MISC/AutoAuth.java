package com.nikitadan4pi.BThack.impl.Modules.MISC;

import com.nikitadan4pi.BThack.Core.Client.Client;
import com.nikitadan4pi.BThack.api.Events.PacketEvent;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Thread.ThreadManager;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.ChatUtils;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.util.Formatting;

import java.util.HashMap;

public class AutoAuth extends Module {
    public static final HashMap<String, String> passwords = new HashMap<>();

    public static BooleanSetting autoToggle;
    public static NumberSetting delay;
    public static BooleanSetting antiFake;

    public AutoAuth() {
        super("AutoAuth",
                "lang.module.AutoAuth",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );

        autoToggle = new BooleanSetting("AutoToggle", this, false);
        delay = new NumberSetting("Delay", this, 1000, 500, 5000, true);
        antiFake = new BooleanSetting("AntiFake", this, true);

        initSettings(
                autoToggle,
                delay,
                antiFake
        );
    }

    @Override
    public void onEnable() {
        super.onEnable();
        ChatUtils.sendMessage(Formatting.GRAY + "Use: " + Client.clientInfo.getChatPrefix() + "autoAuth [[add] [player_name] [password]] / [[remove] [player_name]]");
    }

    @EventSubscriber
    public void onPacketReceive(PacketEvent.Receive e) {
        if (e.getPacket() instanceof GameMessageS2CPacket packet) {
            String text = packet.content().getString().toLowerCase();
            String password = passwords.get(mc.getSession().getUsername());
            if (text.contains("/reg") || text.contains("/register")) {
                if (antiFake.getValue())
                    if (isFake(text)) return;
                sendCommandAction("/reg " + password);
                return;
            }
            if (text.contains("/l") || text.contains("/login")) {
                if (antiFake.getValue())
                    if (isFake(text)) return;
                sendCommandAction("/l " + password);
            }
        }
    }

    public void sendCommandAction(String command) {
        ThreadManager.startNewThread(thread -> {
            try {
                thread.sleep(delay.getValue().longValue());
            } catch (Exception ignored) {}
            ChatUtils.sendCommand(command);
            if (autoToggle.getValue())
                setToggled(false);
        });
    }

    /**
     * Checks if this message is fake(Written by a player/personal message)
     */
    public boolean isFake(String message) {
        if (message.contains(LanguageSystem.translate("lang.module.AutoAuth.ruWord", "RU")) || message.contains("whispers") || message.contains("whispering")) return true;
        if (mc.player != null) {
            if (mc.player.networkHandler.getPlayerList().size() > 1) {
                for (PlayerListEntry info : mc.player.networkHandler.getPlayerList()) {
                    String playerName = getPlayerName(info);
                    if (playerName.length() > 3) {
                        if (message.contains("<" + playerName + ">")) return true;
                        if (!playerName.equals(mc.getSession().getUsername()))
                            if (message.contains(playerName)) return true;
                    }
                }
            }
        }
        return false;
    }

    public String getPlayerName(PlayerListEntry networkPlayerInfoIn) {
        return networkPlayerInfoIn.getDisplayName() != null ? networkPlayerInfoIn.getDisplayName().getString() : networkPlayerInfoIn.getProfile().getName();
    }
}
