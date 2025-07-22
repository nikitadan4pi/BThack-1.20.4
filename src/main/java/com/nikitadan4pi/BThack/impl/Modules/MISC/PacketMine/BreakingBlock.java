package com.nikitadan4pi.BThack.impl.Modules.MISC.PacketMine;

import com.nikitadan4pi.BThack.api.Interfaces.Mc;
import com.nikitadan4pi.BThack.api.Utils.BlockUtils;
import net.minecraft.util.math.BlockPos;

public class BreakingBlock implements Mc {

    public final BlockPos blockPos;
    public double currentDestroyProgress;
    public double prevDestroyProgress;
    public boolean startDestroying = false;

    public BreakingBlock(BlockPos blockPos) {
        this.blockPos = blockPos;
        currentDestroyProgress = 0;
        prevDestroyProgress = 0;
    }

    public boolean canBreak() {
        return !mc.world.isAir(blockPos) && BlockUtils.canBreak(blockPos);
    }
}
