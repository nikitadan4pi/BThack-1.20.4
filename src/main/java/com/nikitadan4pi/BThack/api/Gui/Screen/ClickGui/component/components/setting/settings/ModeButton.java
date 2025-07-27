package com.nikitadan4pi.BThack.api.Gui.Screen.ClickGui.component.components.setting.settings;


import com.nikitadan4pi.BThack.Constants;
import com.nikitadan4pi.BThack.Core.Client.ModuleList;
import com.nikitadan4pi.BThack.Core.Render.BThackMatrix;
import com.nikitadan4pi.BThack.Core.Render.BThackRender;
import com.nikitadan4pi.BThack.api.Animation.Animation;
import com.nikitadan4pi.BThack.api.Animation.Easing;
import com.nikitadan4pi.BThack.api.Gui.Screen.ClickGui.component.components.ModuleButton;
import com.nikitadan4pi.BThack.api.Gui.Screen.ClickGui.component.components.setting.AbstractSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.SoundSystem.SoundSystem;
import com.nikitadan4pi.BThack.api.SoundSystem.Sounds;
import com.nikitadan4pi.BThack.impl.Modules.CLIENT.ClickGui;

import static com.nikitadan4pi.BThack.api.Module.Module.mc;

public class ModeButton extends AbstractSetting<ModeSetting> {

	private boolean open = false;
	private Animation animation = new Animation(Easing.CIRC_IN_OUT, 500);
	private boolean[] hoveredModes = new boolean[this.setting.getOptions().size()];

	public ModeButton(ModeSetting setting, ModuleButton button, int offset, int modeIndex, Module module) {
		super(offset, button, module, setting);

		setting.setValue(setting.getOptions().get(modeIndex));
	}

	@Override
	public void renderComponent() {
		super.renderComponent();

		BThackRender.drawRect(getX(), getY(), getX() + Constants.CLICKGUI_FRAME_WIDTH, getY() + getHeight(), hovered ? ClickGui.BACKGROUND_HOVERED_COLOR : ClickGui.BACKGROUND_COLOR);

		String text = getModeString();
		float scale = getTextScale(text);

		if (scale != 1) {
			BThackMatrix.push();
			BThackMatrix.scale(scale, scale, 1);
		}
		BThackRender.drawString(text, (getX() + 2) / scale, (getY() + 4) / scale, ModuleList.clickGui.textColor.getValue().hashCode());
		if (scale != 1)
			BThackMatrix.pop();
		BThackRender.drawOutlineRect(getX() + 1, getY(), getX() + Constants.CLICKGUI_FRAME_WIDTH - 1, getY() + Constants.CLICKGUI_BUTTON_HEIGHT, 1, Constants.CLICKGUI_BUTTON_OUTLINE_COLOR);
		if (open || animation.getEase() < 1) {
			BThackRender.enableScissor(getX(), getY(), Constants.CLICKGUI_FRAME_WIDTH, getHeight());
			for (int i = 0; i < setting.getOptions().size(); i++) {
				if(this.setting.getValue().equals(this.setting.getOptions().get(i))) {
					if (ModuleList.clickGui.isShaderEnabled()) {
						ModuleList.clickGui.prepareCurrentShader(255f, 0.7f);
						BThackRender.drawShader(ModuleList.clickGui.getCurrentShader(), getX() + 1, getY() + (Constants.CLICKGUI_BUTTON_HEIGHT * (i + 1)), getX() + Constants.CLICKGUI_FRAME_WIDTH - 1, getY() + (Constants.CLICKGUI_BUTTON_HEIGHT * (i + 2)));
					} else
						BThackRender.drawRect(getX() + 1, getY() + (Constants.CLICKGUI_BUTTON_HEIGHT * (i + 1)), getX() + Constants.CLICKGUI_FRAME_WIDTH - 1, getY() + (Constants.CLICKGUI_BUTTON_HEIGHT * (i + 2)), ClickGui.getClickGuiColor(false));
				} else {
					BThackRender.drawRect(getX() + 1, getY() + (Constants.CLICKGUI_BUTTON_HEIGHT * (i + 1)), getX() + Constants.CLICKGUI_FRAME_WIDTH - 1, getY() + (Constants.CLICKGUI_BUTTON_HEIGHT * (i + 2)), hoveredModes[i] ? ClickGui.BACKGROUND_HOVERED_COLOR : ClickGui.BACKGROUND_COLOR);
				}
				BThackRender.drawOutlineRect(getX() + 1, getY() + (Constants.CLICKGUI_BUTTON_HEIGHT * (i + 1)), getX() + Constants.CLICKGUI_FRAME_WIDTH - 1, getY() + (Constants.CLICKGUI_BUTTON_HEIGHT * (i + 2)), 1, Constants.CLICKGUI_BUTTON_OUTLINE_COLOR);
				BThackRender.drawString(setting.getOptions().get(i), (getX() + 2), getY() + (Constants.CLICKGUI_BUTTON_HEIGHT * (i + 1)) + ((Constants.CLICKGUI_BUTTON_HEIGHT - mc.textRenderer.fontHeight) / 2f), ModuleList.clickGui.textColor.getValue().hashCode());
			}
			BThackRender.disableScissor();
		}
	}

	private float getTextScale(String text) {
		if (text.length() > 17) return 0.9f;
		else return 1;
	}

	private String getModeString() {
		return setting.getName() + ": " + (!setting.getOptions().contains(setting.getValue()) ? "NULL" : (setting.getOptions().size() < setting.getIndex() ? setting.getValue() : setting.getOptions().get(setting.getIndex())));
	}

	@Override
	public int getHeight() {
		return open ? (int) (Constants.CLICKGUI_BUTTON_HEIGHT * (setting.getOptions().size() + 1) * animation.getEase()) : (int) (Constants.CLICKGUI_BUTTON_HEIGHT + (Constants.CLICKGUI_BUTTON_HEIGHT * (setting.getOptions().size() + 1) * (1 - animation.getEase())));
	}

	@Override
	public boolean updateComponent(int mouseX, int mouseY) {
		if (!getVisible() || !parent.open){
			open = false;
			return true;
		}

		hovered = isMouseOnButton(mouseX, mouseY);
		if (open){
			for (int i = 0; i < setting.getOptions().size(); i++) {
				if (isMouseOnButton(mouseX, mouseY - (Constants.CLICKGUI_BUTTON_HEIGHT * (i + 1)))) this.hoveredModes[i] = true;
				else hoveredModes[i] = false;
			}
		}
		if (animation.getEase() < 1) {
			parent.parent.refresh();
		}

		return true;
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int button) {
		if (!getVisible()) return false;

		if (isMouseOnButton(mouseX, mouseY) && button == 0) {
			int maxIndex = setting.getOptions().size();

			if (setting.getIndex() + 1 >= maxIndex) {
				setting.setIndex(0);
			} else {
				int currentIndex = setting.getIndex();
				setting.setIndex(currentIndex + 1);
			}

			setting.setValue(setting.getOptions().get(setting.getIndex()));
			SoundSystem.playSound(Sounds.GUI_CHECKBOX_ENABLE);
		}

		if (isMouseOnButton(mouseX, mouseY) && button == 1) {
			open = !open;
			animation.reset();
			parent.parent.refresh();
		}

		if (open){
			for (int i = 0; i < setting.getOptions().size(); i++){
				if (isMouseOnButton(mouseX, mouseY - (Constants.CLICKGUI_BUTTON_HEIGHT * (i + 1))) && button == 0) {
					setting.setIndex(i);
					setting.setValue(setting.getOptions().get(i));
					SoundSystem.playSound(Sounds.GUI_CHECKBOX_ENABLE);
				}
			}
		}
		return isMouseOnButton(mouseX, mouseY);
	}
}