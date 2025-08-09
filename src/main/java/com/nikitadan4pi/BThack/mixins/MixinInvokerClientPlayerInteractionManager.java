package com.nikitadan4pi.BThack.mixins;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import javax.sound.midi.Sequencer;

@Mixin(ClientPlayerInteractionManager.class)
interface InvokerClientPlayerInteractionManager {
    @Invoker("sendSequencedPacket")
    public static void sendSequencedPacket0(ClientWorld world, SequencedPacketCreator creator){};
}