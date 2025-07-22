package com.nikitadan4pi.BThack.api.Managers.managers;

import com.nikitadan4pi.BThack.api.IMixin.ModifyClientConnection;
import com.nikitadan4pi.BThack.api.Interfaces.Mc;
import com.nikitadan4pi.BThack.mixins.accessor.IClientWorld;
import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.network.packet.Packet;

public class NetworkManager implements Mc {

    public void sendSequencePacket(SequencedPacketCreator packetCreator) {
        if (mc.getNetworkHandler() == null || mc.world == null) return;
        try (PendingUpdateManager pendingUpdateManager = ((IClientWorld) mc.world).getPendingManager().incrementSequence()){
            int i = pendingUpdateManager.getSequence();
            mc.getNetworkHandler().sendPacket(packetCreator.predict(i));
        }
    }

    public void sendPacket(Packet<?> packet) {
        mc.getNetworkHandler().sendPacket(packet);
    }

    public void sendPacketNoEvent(Packet<?> packet) {
        ((ModifyClientConnection) mc.getNetworkHandler().getConnection()).sendPacketNoEvent(packet);
    }
}
