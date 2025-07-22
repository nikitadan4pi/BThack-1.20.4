package com.nikitadan4pi.BTbot.impl.AntiAFK.Doing;

import com.nikitadan4pi.BTbot.api.Utils.Generate.GenerateNumber;
import com.nikitadan4pi.BTbot.impl.AntiAFK.Start.StartAntiAFK;
import com.nikitadan4pi.BTbot.impl.AntiAFK.Start.StartAntiAFKThread;
import com.nikitadan4pi.BThack.api.Interfaces.Mc;
import com.nikitadan4pi.BThack.api.Utils.ChatUtils;

public class SendToChat extends Thread implements Mc {
    public static long number = 0;

    public void run() {
        if (StartAntiAFKThread.startDoing) return;
        StartAntiAFKThread.startDoing = true;
        int s = StartAntiAFK.messageSize;
        for (int i = 0; i < s; i++) {
            if (number == 0) {
                number = GenerateNumber.generateInt(0,9);
            } else {
                number = (number * 10) + GenerateNumber.generateInt(0,9);
            }
        }
        ChatUtils.sendChatMessage(String.valueOf(number));
        double d = StartAntiAFK.delay * 1000;
        long delay = (long) d;
        try {
            sleep(delay);
        } catch (InterruptedException ignored) {}
        StartAntiAFKThread.startDoing = false;
        new StartAntiAFKThread().start();
    }
}
