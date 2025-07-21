package com.nikitadan4pi.BThack.impl.Modules.MOVEMENT;

import com.nikitadan4pi.BThack.api.Events.Player.PlayerTravelEvent;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.vehicle.BoatEntity;

public class EntitySpeed extends Module {

    public static NumberSetting boatSpeed;

    public EntitySpeed() {
        super("EntitySpeed",
                "lang.module.EntitySpeed",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );

        boatSpeed = new NumberSetting("BoatSpeed", this, 0.3, 0.1, 1.2, false);

        initSettings(
                boatSpeed
        );
    }

    @EventSubscriber
    public void onTravel(PlayerTravelEvent e) {
        if (mc.player.getControllingVehicle() != null) {
            double speed = boatSpeed.getValue();

            if (mc.player.getControllingVehicle() instanceof BoatEntity boatEntity) {
                if (mc.options.forwardKey.isPressed()) {
                    double yaw = Math.toRadians(boatEntity.yaw);
                    boatEntity.velocity.x = -Math.sin(yaw) * speed;
                    boatEntity.velocity.z = Math.cos(yaw) * speed;
                }
            }
        }
    }
}
