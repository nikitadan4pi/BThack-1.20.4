package com.nikitadan4pi.BThack.api.Utils.Grim;

import com.nikitadan4pi.BThack.api.Interfaces.Mc;
import com.nikitadan4pi.BThack.api.Managers.Managers;
import com.nikitadan4pi.BThack.api.Utils.Rotate.RotateUtils;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public final class GrimUtils implements Mc {

    public static void sendPreActionGrimPackets(float rotYaw, float rotPitch) {
        //RotateUtils.sendPreActionGrimPackets(rotYaw, rotPitch);
        Managers.NETWORK_MANAGER.sendPacket(new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY(), mc.player.getZ(), rotYaw, rotPitch, mc.player.isOnGround()));
    }

    public static void sendPostActionGrimPackets() {
        Managers.NETWORK_MANAGER.sendPacket(new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY(), mc.player.getZ(), mc.player.getYaw(), mc.player.getPitch(), mc.player.isOnGround()));
    }
}
