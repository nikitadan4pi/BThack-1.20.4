package com.nikitadan4pi.BThack.impl.Modules.MISC;


import com.nikitadan4pi.BThack.api.Events.ClientTickEvent;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Thread.ThreadManager;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Social.SocialManagers;
import com.nikitadan4pi.BThack.api.Utils.ChatUtils;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.nikitadan4pi.BThack.api.Utils.Modules.AimBotUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class EnemyRadar extends Module {

    public static BooleanSetting autoDisconnect;
    public static NumberSetting shutdownDelay;

    protected static boolean pause = false;

    public EnemyRadar() {
        super("EnemyRadar",
                "lang.module.EnemyRadar",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );

        autoDisconnect = new BooleanSetting("AutoDisconnect", this, false);
        shutdownDelay = new NumberSetting("Shutdown Delay", this, 3, 1, 10, true, autoDisconnect::getValue);

        initSettings(
                autoDisconnect,
                shutdownDelay
        );
    }

    private static final ArrayList<PlayerEntity> reportedEnemies = new ArrayList<>();

    @EventSubscriber
    public void onWorldRender(ClientTickEvent e) {
        if (nullCheck() || pause) return;

        arrayListInfo = autoDisconnect.getValue() ? "AutoDisconnect" : "";

        for (PlayerEntity player : mc.world.getPlayers()) {
            if (SocialManagers.ENEMIES.contains(player) && !reportedEnemies.contains(player) && player != mc.player) {

                String pattern = "#0.0";
                String health = new DecimalFormat(pattern).format(player.getHealth());

                String message = Formatting.GOLD + String.format(LanguageSystem.translate("lang.module.EnemyRadar.spotted"), Formatting.AQUA, Formatting.WHITE + player.getDisplayName().getString(), Formatting.AQUA, Formatting.WHITE + health, Formatting.AQUA, Formatting.WHITE + AimBotUtils.getDirection(player));


                ChatUtils.sendMessage(message);

                reportedEnemies.add(player);

                if (autoDisconnect.getValue()) {
                    ThreadManager.startNewThread(thread -> {

                        int delay = shutdownDelay.getValue().intValue();

                        ChatUtils.sendMessage(Formatting.RED + LanguageSystem.translate("lang.module.radars.disconnectCountdown"));

                        while (delay != 0) {
                            ChatUtils.sendMessage(String.format(Formatting.RED + LanguageSystem.translate("lang.module.radars.disconnectAfter"), delay));

                            try {
                                thread.sleep(1000);
                            } catch (InterruptedException ignored) {}

                            delay--;
                        }

                        mc.player.networkHandler.onDisconnect(new DisconnectS2CPacket(Text.of(message)));

                        pause = false;
                    });
                    pause = true;
                }
            }
        }

        reportedEnemies.removeIf(player -> !mc.world.getPlayers().contains(player));
    }
}
