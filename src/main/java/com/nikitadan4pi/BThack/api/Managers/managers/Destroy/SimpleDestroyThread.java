package com.nikitadan4pi.BThack.api.Managers.managers.Destroy;

import com.nikitadan4pi.BThack.Core.Client.ModuleList;
import com.nikitadan4pi.BThack.api.Managers.managers.Build.BuildManager;
import com.nikitadan4pi.BThack.api.Utils.BlockUtils;
import com.nikitadan4pi.BThack.api.Utils.Modules.AimBotUtils;
import com.nikitadan4pi.BThack.impl.Modules.PLAYER.AutoTool;
import net.minecraft.util.math.BlockPos;

public class SimpleDestroyThread extends AbstractDestroyThread {
    BlockPos pos;

    public SimpleDestroyThread(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    protected void destroyAction() {
        if (mc.player == null || mc.world == null) {
            DestroyManager.isDestroying = false;
            return;
        }

        DestroyManager.isDestroying = true;

        if (ModuleList.packetMine.isEnabled()) {
            pc.startBlockBreaking(pos, AimBotUtils.getInvertedFacingEntity(mc.player));
            Thread.yield();
        }

        if (BlockUtils.canBreak(pos)) {
            if (ModuleList.packetMine.isEnabled()) {
                ModuleList.packetMine.updateBlock(pos);
                Thread.yield();
            }

            while (BlockUtils.canBreak(pos)) {
                if (ModuleList.packetMine.isEnabled()) {
                    if (!BuildManager.isPossibleRich(pos)) break;
                    if (ModuleList.packetMine.currentBreakingBlock == null)
                        ModuleList.packetMine.updateBlock(pos);
                    DestroyManager.delay(50, this);
                } else {
                    if (BuildManager.isPossibleRich(pos)) {
                        //mc.player.yaw = AimBotUtils.rotations(pos)[0];
                        //mc.player.pitch = AimBotUtils.rotations(pos)[1];
                        AutoTool.equipBestSlot(mc.world.getBlockState(pos));

                        DestroyManager.currentBlockPos = pos;

                        DestroyManager.delay(50, this);
                    } else {
                        break;
                    }
                }
            }
        }
    }
}
