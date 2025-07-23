package com.nikitadan4pi.BThack.impl.Modules.PLAYER.ActionBot.Config.Utils;

import com.nikitadan4pi.BThack.Core.Render.BThackRender;
import com.nikitadan4pi.BThack.api.Interfaces.Mc;
import com.nikitadan4pi.BThack.api.GuiSystem.buttons.Button;

public class TaskSettingButton implements Mc {

    public final Button button;
    public final String name;

    public TaskSettingButton(Button button, String name) {
        this.button = button;
        this.name = name;
    }

    public void render() {
        button.renderButton();
        BThackRender.drawString(name, button.getCenterX() + button.getWidth() + 3, button.getCenterY() - (mc.textRenderer.fontHeight / 2), -1);
    }
}
