package com.nikitadan4pi.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTasks;

import com.nikitadan4pi.BThack.Core.FileSystem.JsonUtils;
import com.nikitadan4pi.BThack.api.Managers.managers.Destroy.DestroyManager;
import com.nikitadan4pi.BThack.api.Managers.managers.Destroy.SimpleDestroyThread;
import com.nikitadan4pi.BThack.api.Utils.ModifyBlockPos;
import com.nikitadan4pi.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotConfig;
import com.nikitadan4pi.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTask;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.Arrays;

public class BreakTask extends ActionBotTask {

    private final int x;
    private final int y;
    private final int z;

    public BreakTask(int x, int y, int z) {
        super("Break");
        mode = "Break";

        this.x = x;
        this.y = y;
        this.z = z;

        this.taskDescription = Arrays.asList(
                "When enabled, it breaks the block at the selected coordinates.",
                "Coordinates should be entered by the ratio of the player's coordinates."
        );
    }

    @Override
    public void play() {
        SimpleDestroyThread simpleDestroyThread = new SimpleDestroyThread(new ModifyBlockPos(mc.player.getX() + x, mc.player.getY() + y, mc.player.getZ() + z));
        simpleDestroyThread.start();
        while (DestroyManager.isDestroying) {
            sleepThread(50);
        }
    }

    @Override
    public String getButtonName() {
        return getName() + ":  X: " + x + "  Y: " + y + "  Z: " + z;
    }

    @Override
    public void save(JsonObject jsonObject) {
        jsonObject.add("X", new JsonPrimitive(x));
        jsonObject.add("Y", new JsonPrimitive(y));
        jsonObject.add("Z", new JsonPrimitive(z));
    }

    @Override
    public void load(JsonObject jsonObject) {
        if (JsonUtils.equalsNull(jsonObject, "X", "Y", "Z")) return;

        ActionBotConfig.tasks.add(new BreakTask(jsonObject.get("X").getAsInt(), jsonObject.get("Y").getAsInt(), jsonObject.get("Z").getAsInt()));
    }
}
