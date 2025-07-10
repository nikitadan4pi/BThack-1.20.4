package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Events.PacketEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Grim.GrimNoFallUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.mixins.accessor.ILivingEntity;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

import java.util.Arrays;

public class NoFall extends Module {

    public static ModeSetting mode;

    public NoFall() {
        super("NoFall",
                "lang.module.NoFall",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );

        mode = new ModeSetting("Mode", this, Arrays.asList("Grim", "Default"));

        initSettings(
                mode
        );
    }

    private boolean started = false;
    private boolean skipTick = true;
    private double fallDistance = 0;

    @EventSubscriber
    public void onPacketSend(PacketEvent.Send e) {
        if (e.getPacket() instanceof PlayerMoveC2SPacket packet) {
            if (mode.getValue().equals("Default"))
                packet.onGround = true;
            else if (started)
                packet.onGround = false;
        }
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (!Module.nullCheck() && !mc.player.isOnGround()) {
            double yDelta = mc.player.getY() - mc.player.prevY;
            if (yDelta < 0)
                fallDistance -= yDelta;
            else fallDistance = 0;
        } else  fallDistance = 0;
        if (nullCheck()) {
            started = false;
            skipTick = true;
            return;
        }
        if (!mc.player.isOnGround() && fallDistance > 3 && !started)
            started = true;

        if (started) {
            mc.options.jumpKey.setPressed(false);
            if (mc.player.isOnGround()) {
                if (skipTick) {
                    skipTick = false;
                    return;
                }
                mc.player.jump();
                ((ILivingEntity) mc.player).setJumpingCooldown(10);
                started = false;
                skipTick = true;
                GrimNoFallUtils.updateFallDamage();
            }
        }
    }
}
