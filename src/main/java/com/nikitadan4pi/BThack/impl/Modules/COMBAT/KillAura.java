package com.nikitadan4pi.BThack.impl.Modules.COMBAT;

import com.nikitadan4pi.BThack.Core.Client.ModuleList;
import com.nikitadan4pi.BThack.api.Events.ClientTickEvent;
import com.nikitadan4pi.BThack.api.Managers.Managers;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.TravelChange.TravelChanger;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Social.Clans.ClansUtils;
import com.nikitadan4pi.BThack.api.Utils.ItemUtils;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.nikitadan4pi.BThack.api.Utils.MathUtils;
import com.nikitadan4pi.BThack.api.Utils.Modules.KillAuraUtils;
import com.nikitadan4pi.BThack.api.Utils.Rotate.RotateMode;
import com.nikitadan4pi.BThack.api.Utils.Rotate.RotateUtils;
import com.nikitadan4pi.BThack.api.Utils.Ticker;
import com.nikitadan4pi.BThack.mixins.accessor.IMinecraftClient;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.util.function.Predicate;

public class KillAura extends Module {

    public static ModeSetting page;
    public static ModeSetting mode;
    public static ModeSetting attackMode;
    public static NumberSetting delay;
    public static NumberSetting range;
    //aura
    public static BooleanSetting instaRotate;
    public static ModeSetting rotateMath;
    public static NumberSetting updateRotateMin;
    public static NumberSetting updateRotateMax;
    public static NumberSetting rotateMinStep;
    public static NumberSetting rotateMaxStep;
    public static NumberSetting lockTicks;
    public static BooleanSetting grim;
    public static ModeSetting rotateMode;
    public static NumberSetting packets;

    public static BooleanSetting moveFix;
    public static ModeSetting moveFixMode;

    public static BooleanSetting ignoreWalls;
    public static BooleanSetting onlyCriticals;

    public static BooleanSetting players;
    public static BooleanSetting invisibles;
    public static BooleanSetting teammates;
    public static BooleanSetting friends;
    public static BooleanSetting hostiles;
    public static BooleanSetting passive;
    public static BooleanSetting golems;
    public static BooleanSetting otherMobs;
    public static BooleanSetting clanManager;
    public static ModeSetting clanMode;
    public static ModeSetting targetClan;


    public static BooleanSetting pauseIfEat;
    public static BooleanSetting pauseIfMine;
    public static BooleanSetting pauseIfBlink;
    public static BooleanSetting pauseIfGui;

    public static BooleanSetting circle;
    public static NumberSetting circleRange;


    public KillAura() {
        super("KillAura",
                "lang.module.KillAura",
                KeyboardUtils.RELEASE,
                MCategory.COMBAT,
                false
        );

        page = new ModeSetting("Page", this, new ArrayList<>(Arrays.asList("General", "Rotate", "Move Fix", "Targets", "Pause")));

        mode = new ModeSetting("Mode", this, new ArrayList<>(Arrays.asList("Aura", "TriggerBot")), () -> page.getValue().equals("General"));
        attackMode = new ModeSetting("AttackMode", this, new ArrayList<>(Arrays.asList("CoolDown", "Delay")), () -> page.getValue().equals("General"));
        delay = new NumberSetting("Delay(Second)", this, 1.4, 0.1, 4, false, () -> attackMode.getValue().equals("Delay") && page.getValue().equals("General"));
        range = new NumberSetting("Range", this, 3.62, 1, 10, false, () -> mode.getValue().equals("Aura") && page.getValue().equals("General"));
        ignoreWalls = new BooleanSetting("Ignore Walls", this, false, () -> page.getValue().equals("General"));
        onlyCriticals = new BooleanSetting("Only Criticals", this, false, () -> page.getValue().equals("General"));
        circle = new BooleanSetting("Circle", this, false, () -> mode.getValue().equals("Aura") && page.getValue().equals("General"));
        circleRange = new NumberSetting("Circle Range", this, 30, 0, 180, false, () -> circle.getValue() && mode.getValue().equals("Aura") && page.getValue().equals("General") && circle.getValue());

        instaRotate = new BooleanSetting("Insta Rotate", this, false, () -> page.getValue().equals("Rotate"));
        rotateMath = new ModeSetting("Rotate Math", this, Arrays.asList("New", "Old", "Always"), () -> !instaRotate.getValue() && page.getValue().equals("Rotate"));
        updateRotateMin = new NumberSetting("Update Rotate Min", this, 80, 0, 500, true, () -> !instaRotate.getValue() && !rotateMath.getValue().equals("Always") && page.getValue().equals("Rotate"));
        updateRotateMax = new NumberSetting("Update Rotate Max", this, 150, 0, 500, true, () -> !instaRotate.getValue() && !rotateMath.getValue().equals("Always") && page.getValue().equals("Rotate"));
        rotateMinStep = new NumberSetting("Rotate Min Step", this, 0.6125, 0.35, 0.8, false, () -> !instaRotate.getValue() && rotateMath.getValue().equals("New") && page.getValue().equals("Rotate"));
        rotateMaxStep = new NumberSetting("Rotate Max Step", this, 0.8, 0.35, 0.8, false, () -> !instaRotate.getValue() && rotateMath.getValue().equals("New") && page.getValue().equals("Rotate"));
        lockTicks = new NumberSetting("Lock Ticks", this, 5, 3, 10, true, () -> !instaRotate.getValue() && page.getValue().equals("Rotate"));
        grim = new BooleanSetting("Grim", this, true, () ->  page.getValue().equals("Rotate"));
        rotateMode = new ModeSetting("RotateMode", this, new ArrayList<>(Arrays.asList("Packet", "Vanilla", "None")), () -> page.getValue().equals("Rotate"));
        packets = new NumberSetting("Packets", this, 1, 1, 5, true, () -> rotateMode.getValue().equals("Packet") && page.getValue().equals("Rotate"));

        moveFix = new BooleanSetting("Move Fix", this, true, () -> page.getValue().equals("Move Fix"));
        moveFixMode = new ModeSetting("Mode", this, Arrays.asList("Legal", "Strong"), () -> page.getValue().equals("Move Fix"));

        players = new BooleanSetting("Players", this, true, () -> page.getValue().equals("Targets"));
        invisibles = new BooleanSetting("Invisibles", this, true, () -> players.getValue() && page.getValue().equals("Targets"));
        teammates = new BooleanSetting("Teammates", this, false, () -> players.getValue() && page.getValue().equals("Targets"));
        friends = new BooleanSetting("Friends", this, false, () -> players.getValue() && page.getValue().equals("Targets"));
        hostiles = new BooleanSetting("Hostiles", this, true, () -> page.getValue().equals("Targets"));
        passive = new BooleanSetting("Passive", this, true, () -> page.getValue().equals("Targets"));
        golems = new BooleanSetting("Golems", this, false, () -> page.getValue().equals("Targets"));
        otherMobs = new BooleanSetting("Other Mobs", this, true, () -> page.getValue().equals("Targets"));
        clanManager = ClansUtils.getClanManagerSetting(this);
        clanMode = ClansUtils.getClanModeSetting(this, clanManager);
        targetClan = ClansUtils.getClanTargetSetting(this, clanManager, clanMode);

        pauseIfEat = new BooleanSetting("If Eat", this, true, () -> page.getValue().equals("Pause"));
        pauseIfMine = new BooleanSetting("If Mine", this, true, () -> page.getValue().equals("Pause"));
        pauseIfBlink = new BooleanSetting("If Blink", this, false, () -> page.getValue().equals("Pause"));
        pauseIfGui = new BooleanSetting("If Gui", this,false, () -> page.getValue().equals("Pause"));

        initSettings(
                page,
                mode,
                attackMode,
                delay,
                range,
                ignoreWalls,
                onlyCriticals,
                circle,
                circleRange,
                instaRotate,
                rotateMath,
                updateRotateMin,
                updateRotateMax,
                rotateMinStep,
                rotateMaxStep,
                lockTicks,
                grim,
                rotateMode,
                packets,
                moveFix,
                moveFixMode,
                players,
                invisibles,
                teammates,
                friends,
                hostiles,
                passive,
                golems,
                otherMobs,
                clanManager,
                clanMode,
                targetClan,
                pauseIfEat,
                pauseIfMine,
                pauseIfBlink,
                pauseIfGui
        );
    }

    private Predicate<Entity> entityFilter;

    private double fallDistance = 0;
    private final Ticker delayTicker = new Ticker();
    private final Ticker deleteTravelTicker = new Ticker();
    private boolean travelCancelled = true;
    public Target targetedEntity;
    private float[] targetRotation;
    private float[] currentRotation;
    private int currentUpdateTargetDelay = 0;
    private final TravelChanger travelChanger = new TravelChanger(10000,
            () -> new Float[]{currentRotation[0], currentRotation[1]},
            () -> moveFixMode.getValue().equals("Strong")
    );
    private Vector2f targetVec;
    private Vector2f currentVec;
    private Entity prevAttackedEntity;

    private final Ticker updateRotTicker = new Ticker();


    @Override
    public void onEnable() {
        super.onEnable();
        delayTicker.reset();
        targetedEntity = null;
        entityFilter = KillAuraUtils.createEntityFilter(hostiles, passive, golems, otherMobs, ignoreWalls);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        delayTicker.reset();
        targetedEntity = null;
        Managers.TRAVEL_CHANGE_MANAGER.removeChanger(travelChanger);
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        if (!Module.nullCheck() && !mc.player.isOnGround()) {
            double yDelta = mc.player.getY() - mc.player.prevY;
            if (yDelta < 0)
                fallDistance -= yDelta;
            else fallDistance = 0;
        } else  fallDistance = 0;

        String killAuraMode = mode.getValue();

        arrayListInfo = killAuraMode + (!killAuraMode.equals("TriggerBot") ? "; " + range.getValue() : "");

        if (needPause()) return;

        switch (killAuraMode) {
            case "Aura" -> auraMode();
            case "TriggerBot" -> triggerBotMode();
        }

    }

    //aura mode code :3

    public void auraMode() {
        if (!mc.player.isAlive()) {
            targetedEntity = null;
            return;
        }
        if (targetedEntity != null) {
            if (mc.player.distanceTo(targetedEntity.entity) > range.getValue() || targetedEntity.entity.isDead() || mc.world.getEntityById(targetedEntity.entity.getId()) == null) {
                if (!travelCancelled) {
                    deleteTravelTicker.reset();
                    travelCancelled = true;
                }
                targetedEntity = null;
                prevAttackedEntity = null;
            }
        }

        if (targetedEntity == null || targetedEntity.lockTicks <= 0 && (delayPassed() && rotateMath.getValue().equals("Always")))
            targetSearchAction();

        attackTargetAction();
    }

    public void targetSearchAction() {
        LivingEntity target = null;
        if (players.getValue())
            target = KillAuraUtils.filterPlayers(range.getValue(), invisibles.getValue(), friends.getValue(), teammates.getValue(), clanManager.getValue(), clanMode.getValue(), targetClan.getValue(), entity -> !entity.isSpectator() && !((PlayerEntity) entity).isCreative());

        if (target == null)
            target = (LivingEntity) KillAuraUtils.filterEntity(range.getValue(), entityFilter);

        if (target != null) {
            targetedEntity = new Target(target, 0);
            currentUpdateTargetDelay = getUpdateRotateDelay();
            travelCancelled = false;
        } else {
            if (!travelCancelled) {
                deleteTravelTicker.reset();
                travelCancelled = true;
            }
            targetedEntity = null;
            prevAttackedEntity = null;
        }
    }

    public void attackTargetAction() {
        if (targetedEntity != null) {
            if (!instaRotate.getValue()) {
                if (!Managers.TRAVEL_CHANGE_MANAGER.containsChanger(travelChanger))
                    Managers.TRAVEL_CHANGE_MANAGER.addChanger(travelChanger);

                if (rotateMath.getValue().equals("Always") || updateRotTicker.passed(currentUpdateTargetDelay)) {
                    targetRotation = RotateUtils.rotations(targetedEntity.entity);
                    updateRotTicker.reset();
                    currentUpdateTargetDelay = getUpdateRotateDelay();
                }
                switch (rotateMath.getValue()) {
                    case "Old", "Always" -> currentRotation = targetRotation;
                    case "New" -> {
                        if (currentRotation != null) {
                            currentRotation[0] += (targetRotation[0] - currentRotation[0]) * getRotateStep();
                            currentRotation[1] += (targetRotation[1] - currentRotation[1]) * getRotateStep();
                        } else currentRotation = new float[]{RotateUtils.getCameraYaw(), RotateUtils.getCameraPitch()};
                    }
                }
            } else Managers.TRAVEL_CHANGE_MANAGER.removeChanger(travelChanger);

            if (!delayPassed()) return;
            if (instaRotate.getValue() || targetedEntity.lockTicks >= lockTicks.getValue()) {
                if (onlyCriticals.getValue() && !isCrit()) return;
                if (instaRotate.getValue()) {
                    RotateMode rotateMode = getRotateMode();
                    if (!circle.getValue()) {
                        KillAuraUtils.preAttackRotate(rotateMode, RotateUtils.rotations(targetedEntity.entity), packets.getValue().intValue());
                    } else {
                        if (Math.sqrt(Math.pow(Math.abs(currentRotation[0] - targetRotation[0]) + Math.abs(currentRotation[1] - targetRotation[1]), 2)) <= circleRange.getValue()){
                            KillAuraUtils.preAttackRotate(rotateMode, RotateUtils.rotations(targetedEntity.entity), packets.getValue().intValue());
                        }
                        else {
                            return;
                        }
                    }
                }
                KillAuraUtils.attackNoRotate(targetedEntity.entity);
                delayTicker.reset();
                prevAttackedEntity = targetedEntity.entity;
            } else {
                targetedEntity = new Target(targetedEntity.entity, prevAttackedEntity == targetedEntity.entity ? lockTicks.getValue().intValue() : targetedEntity.lockTicks + 1);
            }
        } else {
            if (travelCancelled && (deleteTravelTicker.passed(200) || !rotateMath.getValue().equals("New"))) {
                if (Managers.TRAVEL_CHANGE_MANAGER.containsChanger(travelChanger))
                    Managers.TRAVEL_CHANGE_MANAGER.removeChanger(travelChanger);
                currentRotation = null;
            } else {
                currentRotation[0] += (RotateUtils.getCameraYaw() - currentRotation[0]) * getRotateStep();
                currentRotation[1] += (RotateUtils.getCameraPitch() - currentRotation[1]) * getRotateStep();
            }
        }
    }

    public float getRotateStep() {
        return MathUtils.randomFloat(rotateMinStep.getValue().floatValue(), rotateMaxStep.getValue().floatValue());
    }

    public int getUpdateRotateDelay() {
        return MathUtils.randomInt(updateRotateMin.getValue().intValue(), updateRotateMax.getValue().intValue());
    }

    //trigger mode >.<

    public void triggerBotMode() {
        if (!delayPassed()) return;

        HitResult objectMouseOver = mc.crosshairTarget;

        if (onlyCriticals.getValue() && !isCrit()) return;

        if (objectMouseOver instanceof EntityHitResult entityHitResult) {
            Entity ent = entityHitResult.getEntity();

            if (players.getValue() && ent instanceof PlayerEntity player)
                if (KillAuraUtils.filterPlayer(player, invisibles.getValue(), friends.getValue(), teammates.getValue(), clanManager.getValue(), clanMode.getValue(), targetClan.getValue()))
                    ((IMinecraftClient) mc).attack();
            if (entityFilter.test(ent))
                ((IMinecraftClient) mc).attack();

            delayTicker.reset();
        }
    }

    //other utils >wu

    public RotateMode getRotateMode() {
        return switch (rotateMode.getValue()) {
            case "Packet" -> RotateMode.PACKET;
            case "Vanilla" -> RotateMode.VANILLA;
            default -> RotateMode.NONE;
        };
    }

    public boolean delayPassed() {
        return switch (attackMode.getValue()) {
            case "CoolDown" -> mc.player.getAttackCooldownProgress(0) >= 1.0;
            case "Delay" -> delayTicker.passed((int) (delay.getValue() * 1000));
            default -> true;
        };
    }

    public boolean needPause() {
        return (
                (pauseIfMine.getValue() && ItemUtils.isTool(mc.player.getActiveItem().getItem()) && mc.player.isUsingItem()) || (ModuleList.packetMine.isEnabled() && (ModuleList.packetMine.currentBreakingBlock != null || !ModuleList.packetMine.conveyorBlocks.isEmpty()))
                        || (pauseIfEat.getValue() && ItemUtils.isFood(mc.player.getActiveItem()) && mc.player.isUsingItem())
                        || (pauseIfBlink.getValue() && ModuleList.blink.isEnabled())
                        || (pauseIfGui.getValue() && mc.currentScreen != null)
        );
    }

    public double getFallDistance() {
        return fallDistance;
    }

    public boolean isCrit() {
        return mc.player.velocity.y < 0 && !mc.player.isOnGround() && getFallDistance() > 0 && getFallDistance() < 0.3;
    }

    public record Target(LivingEntity entity, int lockTicks) {}

}