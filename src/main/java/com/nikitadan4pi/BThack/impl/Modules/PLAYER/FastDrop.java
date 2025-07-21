package com.nikitadan4pi.BThack.impl.Modules.PLAYER;

import com.nikitadan4pi.BThack.api.Events.ClientTickEvent;
import com.nikitadan4pi.BThack.api.Managers.Managers;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class FastDrop extends Module {

    public static NumberSetting delay;

    public FastDrop() {
        super("FastDrop",
                "lang.module.FastDrop",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );

        delay = new NumberSetting("Delay", this, 0, 0, 4, true);

        initSettings(
                delay
        );
    }

    private int ticks;

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        if (mc.options.dropKey.isPressed() && ticks > delay.getValue()) {
            Managers.NETWORK_MANAGER.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.DROP_ITEM,
                    BlockPos.ORIGIN, Direction.DOWN));
            mc.player.dropSelectedItem(Screen.hasControlDown());
            ticks = 0;
        }
        ++ticks;
    }
}
