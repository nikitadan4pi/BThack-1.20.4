package com.nikitadan4pi.BThack.api.IMixin;

import net.minecraft.network.packet.Packet;

public interface ModifyClientConnection {

    void sendPacketNoEvent(Packet<?> packet);
}
