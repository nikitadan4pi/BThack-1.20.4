package com.nikitadan4pi.BThack.impl.Modules.PLAYER.ActionBot;

import com.nikitadan4pi.BThack.Core.Client.ModuleList;
import com.nikitadan4pi.BThack.api.Utils.ChatUtils;
import com.nikitadan4pi.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotConfig;
import com.nikitadan4pi.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTask;
import net.minecraft.util.Formatting;

public class ActionBotRunTimeThread extends Thread {
    public static boolean isPlaying = false;

    @Override
    public void run() {
        ChatUtils.sendMessage("[ActionBot] "  + Formatting.AQUA + "Starting to reproduce the task chain.");

        if (ActionBot.repeat.getValue()) {
            while (ModuleList.actionBot.isEnabled() && ActionBot.repeat.getValue()) {
                play();
            }

            ModuleList.actionBot.setToggled(false);
        } else {
            play();

            ModuleList.actionBot.setToggled(false);
        }
    }




    public void play() {
        isPlaying = true;

        for (ActionBotTask task : ActionBotConfig.tasks) {
            if (!task.isStartOrEndTask()) {
                task.thread = this;

                task.play();
            }
        }

        isPlaying = false;
    }
}
