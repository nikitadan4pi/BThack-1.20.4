package com.nikitadan4pi.BTbot.impl.AntiAFK.Check;

import com.nikitadan4pi.BThack.api.Interfaces.Mc;

public class NullCheck implements Mc {
    public static boolean nullCheck() {
        return mc.player == null || mc.world == null;
    }
}
