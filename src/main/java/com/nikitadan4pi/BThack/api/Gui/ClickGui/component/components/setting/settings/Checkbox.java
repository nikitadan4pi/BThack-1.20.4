package com.nikitadan4pi.BThack.api.Gui.ClickGui.component.components.setting.settings;

import com.nikitadan4pi.BThack.Constants;
import com.nikitadan4pi.BThack.Core.Client.ModuleList;
import com.nikitadan4pi.BThack.Core.Render.BThackRender;
import com.nikitadan4pi.BThack.Core.Render.Utils.ColorUtils;
import com.nikitadan4pi.BThack.api.Gui.ClickGui.component.components.ModuleButton;
import com.nikitadan4pi.BThack.api.Gui.ClickGui.component.components.setting.AbstractSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.impl.Modules.CLIENT.ClickGui;

import java.awt.*;

import static com.nikitadan4pi.BThack.api.Module.Module.mc;

public class Checkbox extends AbstractSetting<BooleanSetting> {

	private final BooleanSetting set;
	
	public Checkbox(BooleanSetting option, ModuleButton button, int offset, Module module) {
		super(offset, button, module, option);
		set = option;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
	}

	@Override
	public void renderComponent() {
		int color = ColorUtils.integrateAlpha(this.hovered ? new Color(ClickGui.getClickGuiColor(true)).hashCode() : new Color(ClickGui.getClickGuiColor(true)).darker().hashCode(),  255 * ModuleList.clickGui.opacity.getValue());
		BThackRender.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth()), parent.parent.getY() + offset + Constants.CLICKGUI_BUTTON_HEIGHT, this.hovered ? ClickGui.BACKGROUND_HOVERED_COLOR : ClickGui.BACKGROUND_COLOR);
		if (set.getValue()) {
		if (ModuleList.clickGui.oldStyle.getValue()) BThackRender.drawHorizontalGradientRect(parent.parent.getX() + 1, parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth() / 2f), parent.parent.getY() + offset + 15, ClickGui.getClickGuiColor(true), ColorUtils.TRANSPARENT);
		else BThackRender.drawRect(parent.parent.getX() + 1, parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth()), parent.parent.getY() + offset + Constants.CLICKGUI_BUTTON_HEIGHT, color);
		}
		BThackRender.drawString(setting.getName(), parent.parent.getX() + 7, parent.parent.getY() + offset + ((Constants.CLICKGUI_BUTTON_HEIGHT - mc.textRenderer.fontHeight) / 2 ), ClickGui.fontColor.getValue().getRGB());
		if (ClickGui.moduleOutline.getValue()) BThackRender.drawOutlineRect(parent.parent.getX() + 1, parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth()), parent.parent.getY() + offset + Constants.CLICKGUI_BUTTON_HEIGHT, 1, Constants.CLICKGUI_BUTTON_OUTLINE_COLOR);
	}
	
	@Override
	public boolean updateComponent(int mouseX, int mouseY) {

		if (!getVisible()) return true;

		hovered = isMouseOnButton(mouseX, mouseY);
		y = parent.parent.getY() + offset;
		x = parent.parent.getX();
		return true;
	}
	
	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int button) {
		if (!getVisible()) return false;

		if (isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
			set.setValue(!set.getValue());
			set.module.onChangeSetting(set);
		}

		return isMouseOnButton(mouseX, mouseY);
	}
}
