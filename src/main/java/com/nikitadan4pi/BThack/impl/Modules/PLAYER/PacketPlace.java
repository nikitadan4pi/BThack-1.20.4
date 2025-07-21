package com.nikitadan4pi.BThack.impl.Modules.PLAYER;


import com.nikitadan4pi.BThack.api.Events.Block.UseBlockEvent;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Thread.ThreadManager;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;

public class PacketPlace extends Module {

    public static BooleanSetting shifting;
    public static NumberSetting sendPackets;

    public PacketPlace() {
        super("PacketPlace",
                "lang.module.PacketPlace",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );

        shifting = new BooleanSetting("Shifting", this, false);
        sendPackets = new NumberSetting("Send Packets", this, 2, 1, 15, true);

        initSettings(
                shifting,
                sendPackets
        );
    }

    @EventSubscriber
    public void onPlaceBlock(UseBlockEvent e) {

        e.setCancelled(true);
        ThreadManager.startNewThread(thread -> {
            try {
                thread.sleep(5);
            } catch (InterruptedException ignored) {}

            for (int i = 0; i < (sendPackets.getValue().intValue()); i++) {
                if (shifting.getValue())
                    mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY));

                mc.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, e.getBlockHitResult(), 0));

                if (shifting.getValue())
                    mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY));

                try {
                    thread.sleep(5);
                } catch (InterruptedException ignored) {}
            }
        });
    }
}
