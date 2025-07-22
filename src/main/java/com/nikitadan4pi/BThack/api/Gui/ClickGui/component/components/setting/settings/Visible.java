package com.nikitadan4pi.BThack.api.Gui.ClickGui.component.components.setting.settings;


import com.nikitadan4pi.BThack.Constants;
import com.nikitadan4pi.BThack.Core.Client.ModuleList;
import com.nikitadan4pi.BThack.Core.Render.BThackRender;
import com.nikitadan4pi.BThack.Core.Render.Utils.ColorUtils;
import com.nikitadan4pi.BThack.api.Gui.ClickGui.component.components.ModuleButton;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.impl.Modules.CLIENT.ClickGui;

import java.awt.*;

import static com.nikitadan4pi.BThack.api.Module.Module.mc;

public class Visible extends Checkbox {

    public Visible(ModuleButton parent, int offset, Module module) {
        super(null, parent, offset, module);

        x = parent.parent.getX() + parent.parent.getWidth();
        y = parent.parent.getY() + parent.offset;
    }

    @Override
    public void renderComponent() {
        BThackRender.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth()), parent.parent.getY() + offset + 15, this.hovered ? ClickGui.BACKGROUND_HOVERED_COLOR : ClickGui.BACKGROUND_COLOR);
        if (module.visible) {
            if (ModuleList.clickGui.oldStyle.getValue())
            BThackRender.drawHorizontalGradientRect(parent.parent.getX() + 1, parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth() / 2f), parent.parent.getY() + offset + 15, ClickGui.getClickGuiColor(true), ColorUtils.TRANSPARENT);
            else BThackRender.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth()), parent.parent.getY() + offset + 15, ColorUtils.integrateAlpha(this.hovered ? new Color(ClickGui.getClickGuiColor(true)).hashCode() : new Color(ClickGui.getClickGuiColor(true)).darker().hashCode(),  255 * ModuleList.clickGui.opacity.getValue()));

        }
        if (ClickGui.moduleOutline.getValue()) BThackRender.drawOutlineRect(parent.parent.getX() + 1, parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth()), parent.parent.getY() + offset + 15, 1, Constants.CLICKGUI_BUTTON_OUTLINE_COLOR);
        BThackRender.drawString("Visible", parent.parent.getX() + 7, parent.parent.getY() + offset + ((Constants.CLICKGUI_BUTTON_HEIGHT - mc.textRenderer.fontHeight) / 2 ), ClickGui.fontColor.getValue().hashCode());
    }

    @Override
    public void updateDependencies(int offset) {
        //no action
    }

    @Override
    public boolean updateComponent(int mouseX, int mouseY) {
        hovered = isMouseOnButton(mouseX, mouseY);
        y = parent.parent.getY() + offset;
        x = parent.parent.getX();

        return true;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (isMouseOnButton(mouseX, mouseY) && button == 0 && parent.open) {
            module.visible = !module.visible;
        }
        return isMouseOnButton(mouseX, mouseY);
    }
}
