package com.nikitadan4pi.BThack.impl.Modules.RENDER;

import com.nikitadan4pi.BThack.api.Events.ClientTickEvent;
import com.nikitadan4pi.BThack.api.Events.PacketEvent;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import java.util.ArrayList;
import java.util.Arrays;

public class NoRender extends Module {

    public static BooleanSetting noWeather;
    public static BooleanSetting noFog;
    public static BooleanSetting explosions;
    public static BooleanSetting particles;
    public static BooleanSetting overlay;
    public static BooleanSetting waterFog;
    public static BooleanSetting lavaFog;
    public static BooleanSetting powderSnowFog;
    public static BooleanSetting armor;
    public static BooleanSetting totemAnimation;
    public static BooleanSetting nausea;
    public static BooleanSetting fallingBlocks;
    public static BooleanSetting armorStands;

    public static BooleanSetting noSwing;
    public static ModeSetting mode;
    public static BooleanSetting chestRender;
    public static NumberSetting chestRadius;

    public static BooleanSetting shulkerRender;
    public static NumberSetting shulkerRadius;

    public static BooleanSetting eTableRender;
    public static NumberSetting eTableRadius;

    public NoRender() {
        super("NoRender",
                "lang.module.NoRender",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        noWeather = new BooleanSetting("Weather", this, true);
        noFog = new BooleanSetting("Fog", this, true);
        explosions = new BooleanSetting("Explosions", this, true);
        particles = new BooleanSetting("Particles", this, false);
        overlay = new BooleanSetting("Overlay", this, true);
        waterFog = new BooleanSetting("Water Fog", this, true);
        lavaFog = new BooleanSetting("Lava Fog", this, true);
        powderSnowFog = new BooleanSetting("PowderSnow Fog", this, true);
        armor = new BooleanSetting("Armor", this, false);
        totemAnimation = new BooleanSetting("Totem Animation", this, false);
        nausea = new BooleanSetting("Nausea", this, true);
        fallingBlocks = new BooleanSetting("Falling Blocks", this, true);
        armorStands = new BooleanSetting("Armor Stands", this, false);

        noSwing = new BooleanSetting("No Swing", this, false);
        mode = new ModeSetting("Mode", this, new ArrayList<>(Arrays.asList("Client", "Server")));

        chestRender = new BooleanSetting("Chest Render", this, false);
        chestRadius = new NumberSetting("CRender Range", this, 10, 5, 50, false, chestRender::getValue);

        shulkerRender = new BooleanSetting("Shulker Render", this, false);
        shulkerRadius = new NumberSetting("SRender Range", this, 20, 5, 50, false, shulkerRender::getValue);

        eTableRender = new BooleanSetting("ETable Render", this, false);
        eTableRadius = new NumberSetting("ETRender Range", this, 10, 5, 50, false, eTableRender::getValue);


        initSettings(
                noWeather,
                noFog,
                explosions,
                particles,
                overlay,
                waterFog,
                lavaFog,
                powderSnowFog,
                armor,
                totemAnimation,
                nausea,
                fallingBlocks,
                armorStands,

                noSwing,
                mode,

                chestRender,
                chestRadius,

                shulkerRender,
                shulkerRadius,

                eTableRender,
                eTableRadius
        );
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onPacketReceive(PacketEvent.Receive e) {
        if (nullCheck()) return;

        if (explosions.getValue() && e.getPacket() instanceof ExplosionS2CPacket packet) {
            mc.world.playSound(mc.player ,packet.getX(), packet.getY(), packet.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE);
            e.setCancelled(true);
        }
    }

    @EventSubscriber
    public void onPacket(PacketEvent.Send e) {
        if (!noSwing.getValue()) return;
        if (mode.getValue().equals("Server")  && e.getPacket() instanceof HandSwingC2SPacket) {
            e.setCancelled(true);
        }
    }

    @EventSubscriber
    public void onClientTick(ClientTickEvent e) {
        if (nullCheck() || !noSwing.getValue()) return;
        mc.player.handSwinging = false;
        mc.player.handSwingTicks = 0;
        mc.player.handSwingProgress = 0.0f;
        mc.player.lastHandSwingProgress = 0.0f;
    }
}
