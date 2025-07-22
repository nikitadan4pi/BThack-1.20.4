package com.nikitadan4pi.BTbot.impl.AntiAFK.Doing;

import com.nikitadan4pi.BTbot.impl.AntiAFK.Start.StartAntiAFK;
import com.nikitadan4pi.BTbot.impl.AntiAFK.Start.StartAntiAFKThread;
import com.nikitadan4pi.BThack.api.Interfaces.Mc;
import com.nikitadan4pi.BThack.api.Utils.ItemUtils;
import net.minecraft.util.Hand;

public class ActivateHand extends Thread implements Mc {
    public void run() {
        if (StartAntiAFKThread.startDoing) return;
        StartAntiAFKThread.startDoing = true;
        ItemUtils.useItem(Hand.MAIN_HAND, true);
        double d = StartAntiAFK.delay * 1000;
        long delay = (long) d;
        try {
            sleep(delay);
        } catch (InterruptedException ignored) {}
        StartAntiAFKThread.startDoing = false;
        new StartAntiAFKThread().start();
    }
}
