package com.nikitadan4pi.BThack.api.Managers.managers.TravelChange;

import com.nikitadan4pi.BThack.Core.Client.ModuleList;
import com.nikitadan4pi.BThack.api.Events.ClientTickEvent;
import com.nikitadan4pi.BThack.api.Events.PacketEvent;
import com.nikitadan4pi.BThack.api.Events.Player.PlayerTraverRotEvent;
import com.nikitadan4pi.BThack.api.Events.Player.VelocityUpdateEvent;
import com.nikitadan4pi.BThack.api.Interfaces.Mc;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.InputUtils;
import com.nikitadan4pi.BThack.api.Utils.MathUtils;
import com.nikitadan4pi.BThack.api.Utils.Modules.AimBotUtils;
import com.nikitadan4pi.BThack.api.Utils.Rotate.RotateUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TravelChangeManager implements Mc {
    /*
    TravelChangers priority:

    ElytraFlight: 1000000
    KillAura: 5000
    MoveTask/TunnelTask: 1000
    Sprint: 500
     */

    private final List<TravelChanger> changers = new CopyOnWriteArrayList<>();
    private float yaw;
    private float pitch;

    public void addChanger(TravelChanger changer) {
        if (!ModuleList.elytraFlight.mode.getValue().equals("bounce") && !ModuleList.elytraFlight.isEnabled()) {
            return;
        }
        if (!changers.contains(changer)) {
            changers.add(changer);
            filterChangers();
        }
    }

    public void removeChanger(TravelChanger changer) {
        if (changers.contains(changer)) {
            changers.remove(changer);
            filterChangers();
        }
    }

    public boolean containsChanger(TravelChanger changer) {
        return changers.contains(changer);
    }

    private void filterChangers() {
        changers.sort(Comparator.comparing(changer -> changer.priority));
        Collections.reverse(changers);
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (Module.nullCheck()) return;
        if (!changers.isEmpty()) {
            Float[] rots = changers.get(0).rotateGetter.get();
            yaw = rots[0];
            pitch = rots[1];
        }
    }

    @EventSubscriber
    public void onTravelRot(PlayerTraverRotEvent e) {
        if (!changers.isEmpty()) {
            TravelChanger changer = changers.get(0);
            if (changer.needRewriteTravelRot && changer.needTravelChange.get()) {
                e.yaw = yaw;
                e.pitch = pitch;
            }
        }
    }

    @EventSubscriber
    public void onPacketSend(PacketEvent.Send e) {
        if (!changers.isEmpty()) {
            if (e.getPacket() instanceof PlayerMoveC2SPacket packet && (packet instanceof PlayerMoveC2SPacket.Full || packet instanceof PlayerMoveC2SPacket.LookAndOnGround)) {
                if (packet.yaw == mc.player.getYaw() && packet.pitch == mc.player.getPitch()) {
                    if (changers.get(0).needTravelChange.get()) {
                        packet.yaw = yaw;
                        packet.pitch = pitch;
                    }
                }
            }
        }
    }

    @EventSubscriber
    public void onUpdateVelocity(VelocityUpdateEvent e) {
        if (!changers.isEmpty()) {
            if (!mc.player.isFallFlying() && changers.get(0).needTravelChange.get()) {
                e.setVelocity(AimBotUtils.movementInputToVelocity(yaw, e.getMovementInput(), e.getSpeed()));
                changers.get(0).preUpdateVelocityRunnable.run();
            }
        }
    }

    private void moveFix(boolean sneaking, TravelChanger travelChanger) {
        float forward = (mc.player.input.hasForwardMovement() ? 1 : mc.player.input.pressingBack ? -1 : 0);
        float sideways = (mc.player.input.pressingLeft ? 1 : mc.player.input.pressingRight ? -1 : 0);
        Matrix4f matrix = new Matrix4f();
        matrix.rotate((float) Math.toRadians(mc.player.getYaw() - RotateUtils.getCameraYaw()), 0, 1, 0);
        Vec3d updatedInput = MathUtils.transformPos(matrix, sideways, 0, forward);

        forward = (float) (travelChanger.strongMoveFix.get() ? updatedInput.getZ() : Math.round(updatedInput.getZ())) * (sneaking ? (float) 0.3 : 1);
        sideways = (float) (travelChanger.strongMoveFix.get() ? updatedInput.getX() : Math.round(updatedInput.getX())) * (sneaking ? (float) 0.3 : 1);

        InputUtils.setForward(forward > 0.0f);
        InputUtils.setBackward(forward < 0.0f);
        InputUtils.setLeft(sideways > 0.0f);
        InputUtils.setRight(sideways < 0.0f);

        mc.player.input.movementForward = forward;
        mc.player.input.movementSideways = sideways;
    }
}
