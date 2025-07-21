package com.nikitadan4pi.BThack.impl.Modules.COMBAT;

import com.nikitadan4pi.BThack.api.Events.ClientTickEvent;
import com.nikitadan4pi.BThack.api.Events.PacketEvent;
import com.nikitadan4pi.BThack.api.Managers.Managers;
import com.nikitadan4pi.BThack.api.Managers.managers.Build.BuildManager;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.InventoryUtils;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.nikitadan4pi.BThack.api.Utils.Rotate.RotateMode;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.nikitadan4pi.BThack.api.Utils.RotateUtils;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.nikitadan4pi.BThack.api.Managers.managers.Build.BuildManager.getHitResult;

public class Surround extends Module {

    public static NumberSetting blocksPerTick;
    public static BooleanSetting extraBlocks;
    public static BooleanSetting silentSwap;

    public static BooleanSetting autoDisable;
    public static BooleanSetting disableOnY;
    public static BooleanSetting disableOnTp;
    public static BooleanSetting disableOnDeath;
    public static BooleanSetting disableIfNoBlocks;

    public Surround() {
        super("Surround",
                "lang.module.Surround",
                KeyboardUtils.RELEASE,
                MCategory.COMBAT,
                false
        );

        blocksPerTick = new NumberSetting("Blocks Per Tick", this, 4, 1, 8, true);
        extraBlocks = new BooleanSetting("Extra Blocks", this, false);
        silentSwap = new BooleanSetting("Silent Swap", this, true);
        autoDisable = new BooleanSetting("Auto Disable", this, true);
        disableOnY = new BooleanSetting("Disable On Y", this, true, () -> autoDisable.getValue());
        disableOnTp = new BooleanSetting("Disable On Tp", this, true, () -> autoDisable.getValue());
        disableOnDeath = new BooleanSetting("Disable On Death", this, true, () -> autoDisable.getValue());
        disableIfNoBlocks = new BooleanSetting("Disable If No Blocks", this, true, () -> autoDisable.getValue());

        initSettings(
                blocksPerTick,
                extraBlocks,
                silentSwap,
                autoDisable,
                disableOnY,
                disableOnTp,
                disableOnDeath,
                disableIfNoBlocks
        );
    }

    private List<Block> obsidians = Arrays.asList(Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN);

    private double prevY;
    @Override
    public void onEnable() {
        if (nullCheck()) toggle();

        prevY = mc.player.getY();

        if (!mc.player.verticalCollision) {
            sendNotification(Formatting.RED + LanguageSystem.translate("lang.module.AutoBuilder.notStandingOnGround"));
            toggle();
        }

        super.onEnable();
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) {
            toggle();
            return;
        }

        if (autoDisable.getValue()) {
            if (disableOnY.getValue()) {
                if (Math.abs(prevY - mc.player.getY()) > 0.3) {
                    toggle();
                    return;
                }
            }
            if (disableOnDeath.getValue()) {
                if (mc.player.isDead()) {
                    toggle();
                    return;
                }
            }
            if (disableIfNoBlocks.getValue()) {
                if (!pickUpPlaceBlocks(false, obsidians)) {
                    sendNotification(Formatting.RED + LanguageSystem.translate("lang.module.Scaffold.noBlocks"));
                    toggle();
                    return;
                }
            }
        }

        prevY = mc.player.getY();

        if (!pickUpPlaceBlocks(false, obsidians)) {
            if (autoDisable.getValue() && disableIfNoBlocks.getValue()) {
                toggle();
            }
            return;
        }

        int count = 0;
        for (BlockPos blockPos : getBlockPoses()) {
            if (mc.world.isAir(blockPos)) {
                if (silentSwap.getValue()) {
                    if (pickUpPlaceBlocks(false, obsidians)) {                        int slot = findSlot();
                        if (slot != -1) {
                            int oldSlot = mc.player.getInventory().selectedSlot;
                            InventoryUtils.swapAction(oldSlot, slot, false, "Client");
                            placeBlock(blockPos, RotateMode.GRIM);
                            InventoryUtils.swapAction(oldSlot, slot, true, "Client");
                        }
                    }
                } else if (pickUpPlaceBlocks(true, obsidians)) placeBlock(blockPos, RotateMode.GRIM);
                count++;
                if (count >= blocksPerTick.getValue()) break;
            }
        }
    }

    @EventSubscriber
    public void onPacketReceive(PacketEvent.Receive e) {
        if (nullCheck()) return;

        if (autoDisable.getValue() && disableOnTp.getValue() && e.getPacket() instanceof PlayerPositionLookS2CPacket)
            toggle();
    }

    public List<BlockPos> getBlockPoses() {
        List<BlockPos> result = new ArrayList<>();
        BlockPos playerPos = BlockPos.ofFloored(mc.player.getX(), mc.player.getY(), mc.player.getZ());
        result.add(playerPos.add(-1, 0, 0));
        result.add(playerPos.add(1, 0, 0));
        result.add(playerPos.add(0, 0, 1));
        result.add(playerPos.add(0, 0, -1));
        if (extraBlocks.getValue()) {
            result.add(playerPos.add(-1, -1, 0));
            result.add(playerPos.add(1, -1, 0));
            result.add(playerPos.add(0, -1, 1));
            result.add(playerPos.add(0, -1, -1));
        }
        return result;
    }

    public int findSlot() {
        if (mc.player.getMainHandStack().getItem() instanceof BlockItem item)
            if (isNeedBlock(item.getBlock(), obsidians)) return mc.player.getInventory().selectedSlot;

        for (int i = 0; i < 36; i++)
            if (mc.player.getInventory().getStack(i).getItem() instanceof BlockItem blockItem &&
                    isNeedBlock(blockItem.getBlock(), obsidians))
                return i;
        return -1;
    }

    public static boolean pickUpPlaceBlocks(boolean swap, List<Block> needBlocks) {
        if (mc.player.getMainHandStack().getItem() instanceof BlockItem item)
            if (isNeedBlock(item.getBlock(), needBlocks)) return true;

        for (int i = 0; i < 36; i++) {
            if (mc.player.getInventory().getStack(i).getItem() instanceof BlockItem blockItem) {
                if (isNeedBlock(blockItem.getBlock(), needBlocks)) {
                    if (swap) {
                        if (i < 9) {
                            InventoryUtils.swapItem(i);
                        } else {
                            for (int a = 0; a < 9; a++) {
                                if (mc.player.getInventory().getStack(a).getItem() == Items.AIR) {
                                    InventoryUtils.swapItemOnInventory(a, i);
                                    mc.interactionManager.tick();
                                    InventoryUtils.swapItem(a);
                                    return true;
                                }
                            }
                            InventoryUtils.swapItemOnInventory(mc.player.getInventory().selectedSlot, i);
                            mc.interactionManager.tick();
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isNeedBlock(Block block, List<Block> needBlocks) {
        BlockState state = block.getDefaultState();
        if (shiftBlocks.contains(block)) return false;
        if (block instanceof AbstractChestBlock<?> || block instanceof ShulkerBoxBlock) return false;
        if (needBlocks.isEmpty())
            return state.isSolid();
        else
            return state.isSolid() && needBlocks.contains(block);

    }

    public static final List<Block> shiftBlocks = Arrays.asList(
            Blocks.ENDER_CHEST, Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.CRAFTING_TABLE,
            Blocks.BIRCH_TRAPDOOR, Blocks.BAMBOO_TRAPDOOR, Blocks.DARK_OAK_TRAPDOOR, Blocks.CHERRY_TRAPDOOR,
            Blocks.ANVIL, Blocks.BREWING_STAND, Blocks.HOPPER, Blocks.DROPPER, Blocks.DISPENSER,
            Blocks.ACACIA_TRAPDOOR, Blocks.ENCHANTING_TABLE, Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX,
            Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX,
            Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX,
            Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX
    );

    public static void placeBlock(BlockPos pos, RotateMode rotateMode) {
        try {
            if (!mc.world.getBlockState(pos).isReplaceable()) return;
            BlockState block1 = mc.world.getBlockState(pos);
            if ((!mc.world.isAir(pos) && !block1.isLiquid()) || block1.isSolid()) return;

            BlockHitResult bhr = getHitResult(pos, true, Direction.UP);
            if (bhr == null) return;

            float[] rotations = RotateUtils.rotations(bhr.getPos());
            boolean sneak = BuildManager.needSneak(mc.world.getBlockState(bhr.getBlockPos()).getBlock()) && !mc.player.isSneaking();

            if (sneak)
                Managers.NETWORK_MANAGER.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY));

            rotateMode.preRotate(rotations[0], rotations[1]);
            mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, bhr);
            Managers.NETWORK_MANAGER.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));

            if (sneak)
                Managers.NETWORK_MANAGER.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY));
            rotateMode.postRotate();
        } catch (Exception ignored) {}
    }

}
