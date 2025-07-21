package com.nikitadan4pi.BThack.api.Utils.Modules;


import com.nikitadan4pi.BThack.api.Interfaces.Mc;
import net.minecraft.item.SwordItem;

public final class AutoSwordUtils implements Mc {
    public static int getSword() {
        for(int i = 0; i < 9; ++i) {
            if (mc.player.getInventory().getStack(i).getItem() instanceof SwordItem) {
                return i;
            }
        }

        return -1;
    }
}
