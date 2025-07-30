package com.nikitadan4pi.BThack.api.Gui.Screen.ClickGui.component.components.setting.settings;

import com.nikitadan4pi.BThack.Constants;
import com.nikitadan4pi.BThack.Core.Client.ModuleList;
import com.nikitadan4pi.BThack.Core.Render.BThackRender;
import com.nikitadan4pi.BThack.api.Gui.Screen.ClickGui.component.components.ModuleButton;
import com.nikitadan4pi.BThack.api.Gui.Screen.ClickGui.component.components.setting.AbstractSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.Setting;
import com.nikitadan4pi.BThack.api.SoundSystem.SoundSystem;
import com.nikitadan4pi.BThack.api.SoundSystem.Sounds;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.nikitadan4pi.BThack.impl.Modules.CLIENT.ClickGui;

public class Keybind extends AbstractSetting<Setting<?>> {

	private boolean binding;

	public Keybind(ModuleButton button, int offset) {
		super(offset, button, null, null);
	}

	@Override
	public void renderComponent() {
		super.renderComponent();

		BThackRender.drawRect(getX(), getY(), getX() + Constants.CLICKGUI_FRAME_WIDTH, getY() + getHeight(), hovered ? ClickGui.BACKGROUND_HOVERED_COLOR : ClickGui.BACKGROUND_COLOR);

		BThackRender.drawString(binding ? "< PRESS KEY >" : ("Key: " + KeyboardUtils.getKeyName(parent.module.getKey())), getX() + 2, getY() + 4, ModuleList.clickGui.textColor.getValue().hashCode());
	}

	@Override
	public boolean updateComponent(int mouseX, int mouseY) {
		hovered = isMouseOnButton(mouseX, mouseY);

		return true;
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int button) {
		if(isMouseOnButton(mouseX, mouseY) && button == 0)
			binding = !binding;

		return isMouseOnButton(mouseX, mouseY);
	}

	@Override
	public void keyTyped(int key) {
		if (binding) {
			if (key == KeyboardUtils.KEY_DELETE) {
				parent.module.setKey(0);
				binding = false;
				SoundSystem.playSound(Sounds.GUI_TYPING);
			} else if (key != KeyboardUtils.KEY_ESCAPE) {
				parent.module.setKey(key);
				binding = false;
				SoundSystem.playSound(Sounds.GUI_TYPING);
			}
		}
	}
}