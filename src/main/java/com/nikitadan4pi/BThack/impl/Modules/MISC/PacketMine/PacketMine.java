package com.nikitadan4pi.BThack.impl.Modules.MISC.PacketMine;

import com.nikitadan4pi.BThack.Core.Client.ModuleList;
import com.nikitadan4pi.BThack.Core.Render.BThackRender;
import com.nikitadan4pi.BThack.Core.Render.Box.RenderBox;
import com.nikitadan4pi.BThack.api.Animation.Animation;
import com.nikitadan4pi.BThack.api.Animation.Easing;
import com.nikitadan4pi.BThack.api.Events.Block.AttackBlockEvent;
import com.nikitadan4pi.BThack.api.Events.Block.UseBlockEvent;
import com.nikitadan4pi.BThack.api.Events.ClientTickEvent;
import com.nikitadan4pi.BThack.api.Events.Render.RenderWorldEvent;
import com.nikitadan4pi.BThack.api.Managers.Managers;
import com.nikitadan4pi.BThack.api.Managers.managers.Destroy.DestroyManager;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.*;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Social.SocialManagers;
import com.nikitadan4pi.BThack.api.Utils.BlockUtils;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.nikitadan4pi.BThack.api.Utils.MathUtils;
import com.nikitadan4pi.BThack.api.Utils.Rotate.RotateUtils;
import com.nikitadan4pi.BThack.impl.Modules.PLAYER.AutoTool;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PacketMine extends Module {

    public static BooleanSetting swingHand;
    public static BooleanSetting conveyorMode;
    public static BooleanSetting removeIfUse;
    public static BooleanSetting doubleMine;
    public static NumberSetting doubleSpeed;
    public static BooleanSetting rebreak;
    public static BooleanSetting instaRebreak;
    public static BooleanSetting speedMine;
    public static NumberSetting mineSpeed;
    public static BooleanSetting autoCityMode;
    public static BooleanSetting friends;
    public static BooleanSetting inventoryMode;
    public static NumberSetting hotbarSlot;

    public static ModeSetting boxMode;
    public static ColorSetting boxColor;
    public static ColorSetting startBoxColor;
    public static ColorSetting endBoxColor;
    public static ModeSetting instaRebreakAnimationMode;
    public static NumberSetting instaRebreakBoxSize;
    public static ColorSetting instaRebreakColor;
    public static NumberSetting instaRebreakAnimTime;
    public static ModeSetting conveyorAnimationMode;
    public static NumberSetting conveyorBoxSize;
    public static ColorSetting conveyorColor;
    public static NumberSetting conveyorAnimTime;

    public PacketMine() {
        super("PacketMine",
                "lang.module." +
                        "PacketMine",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );

        swingHand = new BooleanSetting("Swing Hand", this, true);
        conveyorMode = new BooleanSetting("Conveyor Mode", this, false);
        removeIfUse = new BooleanSetting("Remove If Use", this, true, conveyorMode::getValue);
        doubleMine = new BooleanSetting("Double Mine", this, false);
        doubleSpeed = new NumberSetting("Double Speed", this, 1, 0.5, 1, false, doubleMine::getValue);
        rebreak = new BooleanSetting("Auto Rebreak", this, false);
        instaRebreak = new BooleanSetting("Insta Rebreak", this, false);
        speedMine = new BooleanSetting("Speed Mine", this, false, () -> !doubleMine.getValue());
        mineSpeed = new NumberSetting("Mine Speed", this, 1.2, 1, 10, false, () -> speedMine.getValue() && !doubleMine.getValue());
        autoCityMode = new BooleanSetting("Auto City", this, false);
        friends = new BooleanSetting("Friends", this, false, autoCityMode::getValue);
        inventoryMode = new BooleanSetting("Inventory Mode", this, false);
        hotbarSlot = new NumberSetting("Hotbar Slot", this, 1, 1, 9, true, inventoryMode::getValue);
        boxMode = new ModeSetting("Box Mode", this, Arrays.asList("Static Color", "Progress Color"));
        boxColor = new ColorSetting("Box Color", this, new Color(0, 255, 0), () -> boxMode.getValue().equals("Static Color")).withBlockedAlpha();
        startBoxColor = new ColorSetting("Start Color", this, new Color(255, 0, 0), () -> boxMode.getValue().equals("Progress Color")).withBlockedAlpha();
        endBoxColor = new ColorSetting("End Color", this, new Color(0, 255, 0), () -> boxMode.getValue().equals("Progress Color")).withBlockedAlpha();
        instaRebreakAnimationMode = new ModeSetting("Anim. Mode", this, Arrays.asList("Static", "Color", "Size")).defaultValue("Color");
        instaRebreakBoxSize = new NumberSetting("Box Size", this, 1, 0.1, 1, false);
        instaRebreakColor = new ColorSetting("Insta Rebreak Color", this, new Color(255, 0, 0)).withBlockedAlpha();
        instaRebreakAnimTime = new NumberSetting("Anim. Time", this, 1000, 500, 3000, true);
        conveyorAnimationMode = new ModeSetting("Anim. Mode", this, Arrays.asList("Static", "Color", "Size")).defaultValue("Color");
        conveyorBoxSize = new NumberSetting("Box Size", this, 0.2, 0.1, 1, false);
        conveyorColor = new ColorSetting("Conveyor Color", this, new Color(255, 255, 0, 255));
        conveyorAnimTime = new NumberSetting("Anim. Time", this, 1000, 500, 3000, true);

        initSettings(
                swingHand,
                conveyorMode,
                removeIfUse,
                doubleMine,
                doubleSpeed,
                rebreak,
                instaRebreak,
                speedMine,
                mineSpeed,
                autoCityMode,
                friends,
                inventoryMode,
                hotbarSlot,
                boxMode,
                boxColor,
                startBoxColor,
                endBoxColor,
                instaRebreakAnimTime,
                instaRebreakAnimationMode,
                conveyorAnimationMode,
                conveyorBoxSize,
                conveyorColor,
                conveyorAnimTime
        );

    }
    private final List<Vec3i> autoCityVectors = Arrays.asList(
            new Vec3i(1,0,0),
            new Vec3i(0,0,1),
            new Vec3i(-1,0,0),
            new Vec3i(0,0,-1)
    );

    //Basic variables
    public BreakingBlock currentBreakingBlock;
    private float destroyDelta = 0;
    private int breakDelay = 5;

    //Conveyor Mode
    public final ArrayList<BreakingBlock> conveyorBlocks = new ArrayList<>();

    //Conveyor Animation
    private final Animation conveyorAnimation = new Animation(Easing.LINEAR, 1000);
    private boolean conveyorAnimationInvert = true;

    //Double Mode
    private BreakingBlock doubleBreakingBlock;

    //Inventory mode
    private int currentSlot = -1;
    private int currentHotbarSlot = -1;

    //Insta Rebreak
    private BlockPos breakedPos;

    //Insta Rebreak Animation
    private final Animation instaRebreakAnimation = new Animation(Easing.LINEAR, 1000);
    private boolean instaRebreakAnimationInvert = true;

    @Override
    public void onChangeSetting(Setting<?> setting) {
        if (setting == conveyorAnimTime) conveyorAnimation.setMillis(conveyorAnimTime.getValue().intValue());
        if (setting == instaRebreakAnimTime) instaRebreakAnimation.setMillis(instaRebreakAnimTime.getValue().intValue());
    }

    @Override
    public void onEnable() {
        super.onEnable();

        clearBreakBlocks();
        conveyorBlocks.clear();
        currentSlot = -1;

        ModuleList.superInstaMine.setToggled(false);

        conveyorAnimation.reset();
        conveyorAnimation.setMillis(conveyorAnimTime.getValue().intValue());
        instaRebreakAnimation.setMillis(instaRebreakAnimTime.getValue().intValue());
        conveyorAnimationInvert = true;

        instaRebreakAnimation.reset();
        instaRebreakAnimationInvert = true;

        breakedPos = null;
    }

    @Override
    public void onDisable() {
        super.onDisable();

        clearBreakBlocks();
        conveyorBlocks.clear();
        if (!nullCheck())
            packetRemoveItem();

        //ModuleList.treeCutter.setToggled(false);
    }

    @EventSubscriber(priority = Integer.MAX_VALUE)
    public void onAttackBlock(AttackBlockEvent e) {
        if (nullCheck() || DestroyManager.isDestroying) return;
        if (e.getBlockPos() == null) return;

        e.setCancelled(true);
        if (!BlockUtils.canBreak(e.getBlockPos())) return;
        if (currentBreakingBlock != null && currentBreakingBlock.blockPos.equals(e.getBlockPos())) return;
        updateBlock(e.getBlockPos());
    }

    @EventSubscriber(priority = Integer.MAX_VALUE)
    public void onUseBlock(UseBlockEvent e) {
        if (e.blockHitResult.getBlockPos() == null) return;
        if (currentBreakingBlock == null) return;
        if (!removeIfUse.getValue()) return;
        if (conveyorMode.getValue() && conveyorContains(e.getBlockHitResult().getBlockPos())) {
            conveyorBlocks.removeIf(breakingBlock -> breakingBlock.blockPos.equals(e.getBlockHitResult().getBlockPos()));
            e.setCancelled(true);
        }
    }

    @EventSubscriber
    public void onRender(RenderWorldEvent.Last e) {
        ArrayList<RenderBox> renderBoxes = new ArrayList<>();

        if (instaRebreakAnimation.getEase() >= 1) {
            instaRebreakAnimation.reset();
            instaRebreakAnimationInvert = !instaRebreakAnimationInvert;
        }

        if (instaRebreak.getValue() && breakedPos != null) {
            float animStep = (float) (instaRebreakAnimationInvert ? 1 - instaRebreakAnimation.getEase() : instaRebreakAnimation.getEase());
            float[] color = new float[]{instaRebreakColor.getValue().getRed() / 255f, instaRebreakColor.getValue().getGreen() / 255f, instaRebreakColor.getValue().getBlue() / 255f, 1f};
            float boxSize = instaRebreakBoxSize.getValue().floatValue() / 2f;
            switch (instaRebreakAnimationMode.getValue()) {
                case "Size" -> boxSize *= animStep;
                case "Color" -> {
                    color[0] *= animStep;
                    color[1] *= animStep;
                    color[2] *= animStep;
                    color[3] *= animStep;
                }
            }
            renderBoxes.add(new RenderBox(
                    BlockUtils.createBox(breakedPos, boxSize, boxSize, boxSize, true),
                    color[0],
                    color[1],
                    color[2],
                    color[3],
                    color[0],
                    color[1],
                    color[2],
                    color[3] * 0.3f));
        }

        if (getAnyBreakingBlock() != null) {
            if (currentBreakingBlock != null)
                renderBoxes.add(getRenderBox(currentBreakingBlock));

            if (doubleMine.getValue() && doubleBreakingBlock != null)
                renderBoxes.add(getRenderBox(doubleBreakingBlock));

            if (conveyorMode.getValue()) {
                if (conveyorAnimation.getEase() >= 1) {
                    conveyorAnimation.reset();
                    conveyorAnimationInvert = !conveyorAnimationInvert;
                }

                float animStep = (float) (conveyorAnimationInvert ? 1 - conveyorAnimation.getEase() : conveyorAnimation.getEase());
                float[] convColor = new float[]{conveyorColor.getValue().getRed() / 255f, conveyorColor.getValue().getGreen() / 255f, conveyorColor.getValue().getBlue() / 255f, conveyorColor.getValue().getAlpha() / 255f};
                float convBoxSize = conveyorBoxSize.getValue().floatValue() / 2f;
                switch (conveyorAnimationMode.getValue()) {
                    case "Size" -> convBoxSize *= animStep;
                    case "Color" -> {
                        convColor[0] *= animStep;
                        convColor[1] *= animStep;
                        convColor[2] *= animStep;
                        convColor[3] *= animStep;
                    }
                }
                for (BreakingBlock pos : conveyorBlocks) {
                    renderBoxes.add(
                            new RenderBox(BlockUtils.createBox(
                                    pos.blockPos,
                                    convBoxSize,
                                    convBoxSize,
                                    convBoxSize,
                                    true
                            ),
                                    convColor[0],
                                    convColor[1],
                                    convColor[2],
                                    convColor[3],
                                    convColor[0],
                                    convColor[1],
                                    convColor[2],
                                    0.3f * convColor[3]
                            )
                    );
                }
            }
        }

        if (!renderBoxes.isEmpty()) {
            BThackRender.boxRender.prepareBoxRender();
            BThackRender.boxRender.renderBoxes(renderBoxes);
            BThackRender.boxRender.stopBoxRender();
        }
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck() || mc.isPaused()) return;
        if (rebreak.getValue() && breakedPos !=null ) updateBlock(breakedPos);

        if (instaRebreak.getValue() && breakedPos != null) {
            if (BlockUtils.canBreak(breakedPos) && currentBreakingBlock == null) {
                updateBlock(breakedPos);
            }
        }

        if (!conveyorMode.getValue() && !conveyorBlocks.isEmpty())
            conveyorBlocks.clear();

        if (autoCityMode.getValue())
            autoCityAction();


        if (getAnyBreakingBlock() == null) {
            destroyDelta = 0;
            return;
        }

        if (destroyDelta == 0)
            checkDestroyDelta();

        if (breakDelay > 0) {
            breakDelay--;
            return;
        }
        if (!updateBreakProgress()) {
            packetRemoveItem();

            clearBreakBlocks();
            if (conveyorMode.getValue()) {
                conveyorBlocks.removeIf(breakingBlock -> breakingBlock.equals(currentBreakingBlock) || breakingBlock.equals(doubleBreakingBlock));

                if (!conveyorBlocks.isEmpty()) {
                    updateBlock(conveyorBlocks.get(0).blockPos);
                    conveyorBlocks.remove(0);
                    if (doubleMine.getValue() && !conveyorBlocks.isEmpty()) {
                        updateBlock(conveyorBlocks.get(0).blockPos);
                        conveyorBlocks.remove(0);
                    }
                }
            }
        }
    }

    private RenderBox getRenderBox(BreakingBlock breakingBlock) {
        double blockSize = MathHelper.lerp(mc.getTickDelta(), breakingBlock.prevDestroyProgress, breakingBlock.currentDestroyProgress) / 2;
        float boxR = (float) (boxMode.getValue().equals("Static Color") ? boxColor.getValue().getRed() / 255f : MathHelper.lerp(breakingBlock.currentDestroyProgress, startBoxColor.getValue().getRed(), endBoxColor.getValue().getRed()) / 255f);
        float boxG = (float) (boxMode.getValue().equals("Static Color") ? boxColor.getValue().getGreen() / 255f : MathHelper.lerp(breakingBlock.currentDestroyProgress, startBoxColor.getValue().getGreen(), endBoxColor.getValue().getGreen()) / 255f);
        float boxB = (float) (boxMode.getValue().equals("Static Color") ? boxColor.getValue().getBlue() / 255f : MathHelper.lerp(breakingBlock.currentDestroyProgress, startBoxColor.getValue().getBlue(), endBoxColor.getValue().getBlue()) / 255f);
        return new RenderBox(
                BlockUtils.createBox(
                        breakingBlock.blockPos,
                        blockSize,
                        blockSize,
                        blockSize,
                        true
                ),
                boxR,
                boxG,
                boxB,
                1,
                boxR,
                boxG,
                boxB,
                0.3f
        );
    }

    public void updateBlock(BlockPos pos) {
        if (conveyorMode.getValue() && needAddToConveyor()) {
            if (!conveyorContains(pos))
                conveyorBlocks.add(new BreakingBlock(pos));
            return;
        }

        if (doubleMine.getValue() && currentBreakingBlock != null)
            doubleBreakingBlock = currentBreakingBlock;
        currentBreakingBlock = new BreakingBlock(pos);
    }

    private void checkDestroyDelta() {
        BreakingBlock breakingBlock = getAnyBreakingBlock();
        int bestSlot = AutoTool.getBestSlot(mc.world.getBlockState(breakingBlock.blockPos), inventoryMode.getValue() ? 36 : 9);
        ItemStack stack = mc.player.getInventory().getStack(mc.player.getInventory().selectedSlot);
        if (bestSlot != -1)
            stack = mc.player.getInventory().getStack(bestSlot);
        destroyDelta = mc.world.getBlockState(currentBreakingBlock.blockPos).calcBlockBreakingDelta(mc.player, mc.player.getWorld(), currentBreakingBlock.blockPos);
        //destroyDelta = ItemUtils.getMineSpeed(mc.world.getBlockState(breakingBlock.blockPos), breakingBlock.blockPos, stack);
    }

    public boolean updateBreakProgress() {
        if (getAnyBreakingBlock() == null) return false;
        if ((currentBreakingBlock == null || !currentBreakingBlock.canBreak()) && (doubleBreakingBlock == null || (!doubleMine.getValue() || !doubleBreakingBlock.canBreak()))) {
            packetRemoveItem();
            destroyDelta = 0;
            return false;
        }
        if (currentSlot == -1) packetEquipItem();

        boolean currentStop = checkStopAction(currentBreakingBlock);
        boolean doubleStop = !doubleMine.getValue() || checkStopAction(doubleBreakingBlock);
        if (currentStop && doubleStop) return false;

        updateBreakProgressInternal(currentBreakingBlock);
        if (doubleMine.getValue())
            updateBreakProgressInternal(doubleBreakingBlock);

        if (currentBreakingBlock != null && !currentBreakingBlock.canBreak()) {
            breakedPos = currentBreakingBlock.blockPos;
            currentBreakingBlock = null;
        }
        if (doubleBreakingBlock != null && !doubleBreakingBlock.canBreak())
            doubleBreakingBlock = null;
        return true;
    }

    public boolean checkStopAction(BreakingBlock breakingBlock) {
        if (breakingBlock == null) return true;
        if (breakingBlock.currentDestroyProgress == 1) {
            if (breakingBlock == currentBreakingBlock)
                if (instaRebreak.getValue() && breakedPos != null && breakedPos.equals(currentBreakingBlock.blockPos)) return false;
            destroyDelta = 0;
            if (swingHand.getValue()) mc.player.swingHand(Hand.MAIN_HAND);
            if (!doubleMine.getValue() || breakingBlock == currentBreakingBlock)
                stopDestroyBlock(currentBreakingBlock.blockPos);
            return true;
        }
        return false;
    }

    public void updateBreakProgressInternal(BreakingBlock breakingBlock) {
        if (breakingBlock == null) return;
        if (!breakingBlock.startDestroying) {
            if (breakingBlock == currentBreakingBlock)
                if (doubleMine.getValue() && doubleBreakingBlock != null && !doubleBreakingBlock.startDestroying) return;
            if (instaRebreak.getValue() && breakedPos != null && breakedPos.equals(breakingBlock.blockPos)){
                breakingBlock.currentDestroyProgress = 1;
                stopDestroyBlock(breakingBlock.blockPos);
            } else {
                startDestroyBlock(breakingBlock.blockPos);
            }
            if (swingHand.getValue()) mc.player.swingHand(Hand.MAIN_HAND);
            breakingBlock.startDestroying = true;
        } else {
            breakingBlock.prevDestroyProgress = breakingBlock.currentDestroyProgress;
            breakingBlock.currentDestroyProgress += getDestroyDelta(breakingBlock == currentBreakingBlock);
            if (breakingBlock.currentDestroyProgress >= 1) breakingBlock.currentDestroyProgress = 1;
        }
    }

    private double getDestroyDelta(boolean isDouble) {
        return (destroyDelta * (isDouble && doubleMine.getValue() ? doubleSpeed.getValue() : 1)) * (speedMine.getValue() ? mineSpeed.getValue() : 1);
    }

    private void startDestroyBlock(BlockPos blockPos) {
        Managers.NETWORK_MANAGER.sendSequencePacket(id -> new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, blockPos, RotateUtils.getInvertedFacingEntity(mc.player), id));
        Managers.NETWORK_MANAGER.sendSequencePacket(id -> new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, blockPos, RotateUtils.getInvertedFacingEntity(mc.player), id));
        if (doubleMine.getValue())
            Managers.NETWORK_MANAGER.sendSequencePacket(id -> new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, blockPos, RotateUtils.getInvertedFacingEntity(mc.player), id));
    }

    private void stopDestroyBlock(BlockPos blockPos) {
        Managers.NETWORK_MANAGER.sendSequencePacket(id -> new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, blockPos, RotateUtils.getInvertedFacingEntity(mc.player), id));
    }

    private void autoCityAction() {
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player ||
                    (!friends.getValue() && SocialManagers.FRIENDS.contains(player)) ||
                    player.distanceTo(mc.player) > 4
            ) continue;
            BlockPos blockPos = BlockPos.ofFloored(player.getX(), player.getY(),player.getZ());
            if (BlockUtils.canBreak(blockPos) && MathUtils.getDistance(mc.player.getPos(), blockPos.toCenterPos()) < 4.25)
                updateBlock(blockPos);

            BlockPos nearestPos = getNearestAutoCityBlockPos(player);
            if (nearestPos != null && BlockUtils.canBreak(nearestPos)) updateBlock(nearestPos);
        }
    }

    private BlockPos getNearestAutoCityBlockPos(PlayerEntity player) {
        BlockPos nearestPos = null;
        double nearestLength = -1;
        for (Vec3i vec : autoCityVectors) {
            BlockPos checkPos = BlockPos.ofFloored(player.getX() + vec.getX(), player.getY(), player.getZ() + vec.getZ());
            double length = MathUtils.getDistance(mc.player.getPos(), checkPos.toCenterPos());
            if (length > 4.25) continue;
            if (length < nearestLength || nearestLength < 0) {
                nearestLength = length;
                nearestPos = checkPos;
            }
        }
        return nearestPos;
    }

    private void packetEquipItem() {
        BreakingBlock breakingBlock = getAnyBreakingBlock();
        int bestSlot = AutoTool.getBestSlot(mc.world.getBlockState(breakingBlock.blockPos), inventoryMode.getValue() ? 36 : 9);
        if (bestSlot != -1) {
            if (bestSlot < 9) {
                Managers.NETWORK_MANAGER.sendPacket(new UpdateSelectedSlotC2SPacket(bestSlot));
            } else {
                currentHotbarSlot = hotbarSlot.getValue().intValue() - 1;
                pc.packetClickSlot(0, bestSlot, currentHotbarSlot, SlotActionType.SWAP);
                currentSlot = bestSlot;
                Managers.NETWORK_MANAGER.sendPacket(new UpdateSelectedSlotC2SPacket(currentHotbarSlot));
            }
        }
    }

    private void packetRemoveItem() {
        if (currentSlot != -1 && currentHotbarSlot != -1) {
            pc.packetClickSlot(0, currentSlot, currentHotbarSlot, SlotActionType.SWAP);
            currentSlot = -1;
            currentHotbarSlot = -1;
        }
        Managers.NETWORK_MANAGER.sendPacket(new UpdateSelectedSlotC2SPacket(mc.player.getInventory().selectedSlot));
    }

    public boolean conveyorContains(BlockPos pos) {
        for (BreakingBlock breakingBlock : conveyorBlocks)
            if (breakingBlock.blockPos.equals(pos))
                return true;
        return false;
    }

    public void clearBreakBlocks() {
        currentBreakingBlock = null;
        doubleBreakingBlock = null;
        breakDelay = 0;
    }

    public boolean needAddToConveyor() {
        return currentBreakingBlock != null && (!doubleMine.getValue() || doubleBreakingBlock != null);
    }

    public BreakingBlock getAnyBreakingBlock() {
        return (currentBreakingBlock == null && doubleMine.getValue()) ? doubleBreakingBlock : currentBreakingBlock;
    }

}