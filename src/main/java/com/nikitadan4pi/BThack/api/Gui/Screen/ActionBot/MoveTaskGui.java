package com.nikitadan4pi.BThack.api.Gui.Screen.ActionBot;

import com.nikitadan4pi.BThack.Core.Render.BThackRender;
import com.nikitadan4pi.BThack.Core.Render.Utils.ColorUtils;
import com.nikitadan4pi.BThack.api.GuiSystem.Screen.BThackScreen;
import com.nikitadan4pi.BThack.api.GuiSystem.buttons.Button;
import com.nikitadan4pi.BThack.api.GuiSystem.buttons.NumberFrameButton;
import com.nikitadan4pi.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotConfig;
import com.nikitadan4pi.BThack.impl.Modules.PLAYER.ActionBot.Config.Utils.TaskButton;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import static com.nikitadan4pi.BThack.api.Gui.Screen.ActionBot.ActionBotConfigGui.*;

public class MoveTaskGui extends BThackScreen {
    private final TaskButton taskButton;

    public MoveTaskGui(TaskButton taskButton) {
        super(Text.of("MoveTask"));
        this.taskButton = taskButton;
    }

    @Override
    public void init() {
        this.buttons.clear();

        this.buttons.add(new NumberFrameButton(1, (scaledResolution.getScaledWidth() / 2), (int) ((scaledResolution.getScaledHeight() / 2) - (heightFactor * 2.5)), (int) (widthFactor * 8.5), (int) heightFactor));
        this.buttons.add(Button.of(-1, (scaledResolution.getScaledWidth() / 2), ((scaledResolution.getScaledHeight() / 2)), (int) (widthFactor * 8.5), (int) heightFactor, "Confirm"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        drawBackGround();

        BThackRender.drawString("New task number", (int) ((scaledResolution.getScaledWidth() / 2) - (mc.textRenderer.getWidth("New task number") / 2)), (int) ((scaledResolution.getScaledHeight() / 2) - (heightFactor * 4.5)), ColorUtils.WHITE);

        super.render(context, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (activeButton.getId() == -1) {
            NumberFrameButton numberButton = (NumberFrameButton) getButtonFromId(1);

            ActionBotConfig.tasks.remove(this.taskButton.getId());
            ActionBotConfig.tasks.add((int) Math.min(ActionBotConfig.tasks.size() - 2, numberButton.getNumber()), taskButton.task);

            mc.setScreen(new ActionBotConfigGui());
        }

        return false;
    }
}
