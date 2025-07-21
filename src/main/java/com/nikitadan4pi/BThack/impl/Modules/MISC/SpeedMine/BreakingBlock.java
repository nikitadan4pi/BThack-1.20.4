package com.nikitadan4pi.BThack.impl.Modules.MISC.SpeedMine;

import com.nikitadan4pi.BThack.api.Interfaces.Mc;
import net.minecraft.util.math.BlockPos;

public class BreakingBlock implements Mc {

    public final BlockPos blockPos;
    public double currentDestroyProgress;
    public boolean startDestroying = false;

    public BreakingBlock(BlockPos blockPos) {
        this.blockPos = blockPos;
        currentDestroyProgress = 0;
    }
}
