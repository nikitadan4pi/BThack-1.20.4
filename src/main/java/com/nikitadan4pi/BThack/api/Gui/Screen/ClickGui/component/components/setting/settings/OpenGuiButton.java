package com.nikitadan4pi.BThack.api.Gui.Screen.ClickGui.component.components.setting.settings;


import com.nikitadan4pi.BThack.Constants;
import com.nikitadan4pi.BThack.Core.Render.BThackRender;
import com.nikitadan4pi.BThack.Core.Render.Utils.ColorUtils;
import com.nikitadan4pi.BThack.api.Gui.Screen.ClickGui.component.components.ModuleButton;
import com.nikitadan4pi.BThack.api.Gui.Screen.ClickGui.component.components.setting.AbstractSetting;
import com.nikitadan4pi.BThack.api.Interfaces.Mc;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.GuiButtonSetting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.GuiSystem.Screen.BThackScreen;
import com.nikitadan4pi.BThack.impl.Modules.CLIENT.ClickGui;

public class OpenGuiButton extends AbstractSetting<GuiButtonSetting> implements Mc {

    public OpenGuiButton(GuiButtonSetting setting, ModuleButton button, int offset, Module module) {
        super(offset, button, module, setting);
    }

    @Override
    public void renderComponent() {
        super.renderComponent();
        BThackRender.drawRect(getX(), getY(), getX() + 100, getY() + getHeight(), hovered ? ClickGui.BACKGROUND_HOVERED_COLOR : ClickGui.BACKGROUND_COLOR);
        BThackRender.drawString(setting.getName() + " ... ", getX() + 2, getY() + ((Constants.CLICKGUI_BUTTON_HEIGHT - mc.textRenderer.fontHeight) / 2 ), ColorUtils.WHITE);
        if (ClickGui.moduleOutline.getValue()) BThackRender.drawOutlineRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset, 1, Constants.CLICKGUI_BUTTON_OUTLINE_COLOR);
    }

    @Override
    public boolean updateComponent(int mouseX, int mouseY) {
        if (!getVisible()) return true;

        hovered = isMouseOnButton(mouseX, mouseY);

        return true;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (!getVisible()) return false;

        if (isMouseOnButton(mouseX, mouseY) && button == 0 && parent.open)
            ((BThackScreen) mc.currentScreen).closeAfterClicking(() -> mc.setScreen(setting.getValue().get()));

        return isMouseOnButton(mouseX, mouseY);
    }
}
