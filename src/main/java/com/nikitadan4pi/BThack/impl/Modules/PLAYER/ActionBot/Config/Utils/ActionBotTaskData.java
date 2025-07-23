package com.nikitadan4pi.BThack.impl.Modules.PLAYER.ActionBot.Config.Utils;

import com.nikitadan4pi.BThack.api.GuiSystem.Screen.BThackScreen;
import com.nikitadan4pi.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTask;

public interface ActionBotTaskData {

    ActionBotTask getTask();

    BThackScreen getTaskScreen(TaskButton instance, boolean edit);
}
