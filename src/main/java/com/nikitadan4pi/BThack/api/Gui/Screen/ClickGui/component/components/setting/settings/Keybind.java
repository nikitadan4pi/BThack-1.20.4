package com.nikitadan4pi.BThack.api.Gui.Screen.ClickGui.component.components.setting.settings;


import com.nikitadan4pi.BThack.Constants;
import com.nikitadan4pi.BThack.Core.Render.BThackRender;
import com.nikitadan4pi.BThack.api.Gui.Screen.ClickGui.component.components.ModuleButton;
import com.nikitadan4pi.BThack.api.Gui.Screen.ClickGui.component.components.setting.AbstractSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.Setting;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.nikitadan4pi.BThack.impl.Modules.CLIENT.ClickGui;

import static com.nikitadan4pi.BThack.api.Module.Module.mc;

public class Keybind extends AbstractSetting<Setting<?>> {

	private boolean binding;
	
	public Keybind(ModuleButton button, int offset) {
		super(offset, button, null, null);


		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
	}
	
	@Override
	public void renderComponent() {
		BThackRender.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + 15, this.hovered ? ClickGui.BACKGROUND_HOVERED_COLOR : ClickGui.BACKGROUND_COLOR);
		BThackRender.drawString(binding ? "< PRESS KEY >" : ("Key: " + KeyboardUtils.getKeyName(this.parent.module.getKey())), parent.parent.getX() + 2, parent.parent.getY() + offset + ((Constants.CLICKGUI_BUTTON_HEIGHT - mc.textRenderer.fontHeight) / 2 ), ClickGui.fontColor.getValue().hashCode());
		if (ClickGui.moduleOutline.getValue()) BThackRender.drawOutlineRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset, 1, Constants.CLICKGUI_BUTTON_OUTLINE_COLOR);
	}

	@Override
	public void updateDependencies(int offset) {
		//no action
	}
	
	@Override
	public boolean updateComponent(int mouseX, int mouseY) {
		this.hovered = isMouseOnButton(mouseX, mouseY);
		this.y = parent.parent.getY() + offset;
		this.x = parent.parent.getX();

		return true;
	}
	
	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int button) {
		if(isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
			this.binding = !this.binding;
		}

		return isMouseOnButton(mouseX, mouseY);
	}
	
	@Override
	public void keyTyped(int key) {
		if (this.binding) {
			if (key == KeyboardUtils.KEY_DELETE) {
				this.parent.module.setKey(0);
				this.binding = false;
			} else if (key != KeyboardUtils.KEY_ESCAPE) {
				this.parent.module.setKey(key);
				this.binding = false;
			}
		}
	}
}
