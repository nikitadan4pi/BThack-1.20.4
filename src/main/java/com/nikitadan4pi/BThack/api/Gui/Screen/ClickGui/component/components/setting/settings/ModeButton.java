package com.nikitadan4pi.BThack.api.Gui.Screen.ClickGui.component.components.setting.settings;


import com.nikitadan4pi.BThack.Constants;
import com.nikitadan4pi.BThack.Core.Client.ModuleList;
import com.nikitadan4pi.BThack.Core.Render.BThackMatrix;
import com.nikitadan4pi.BThack.Core.Render.BThackRender;
import com.nikitadan4pi.BThack.Core.Render.Utils.ColorUtils;
import com.nikitadan4pi.BThack.api.Animation.Animation;
import com.nikitadan4pi.BThack.api.Animation.Easing;
import com.nikitadan4pi.BThack.api.Gui.Screen.ClickGui.component.components.ModuleButton;
import com.nikitadan4pi.BThack.api.Gui.Screen.ClickGui.component.components.setting.AbstractSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.impl.Modules.CLIENT.ClickGui;

import static com.nikitadan4pi.BThack.api.Module.Module.mc;

public class ModeButton extends AbstractSetting<ModeSetting> {

	public boolean opened = false;
	private final Animation animation = new Animation(Easing.CIRC_OUT, 450);

	public ModeButton(ModeSetting setting, ModuleButton button, int offset, int modeIndex, Module module) {
		super(offset, button, module, setting);

		setting.setValue(setting.getOptions().get(modeIndex));
	}

	@Override
	public void renderComponent() {
		super.renderComponent();
		BThackRender.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + Constants.CLICKGUI_BUTTON_HEIGHT, this.hovered ? ClickGui.BACKGROUND_HOVERED_COLOR : ClickGui.BACKGROUND_COLOR);
		String text = getModeString();
		float scale = getTextScale(text);

		if (scale != 1) {
			BThackMatrix.push();
			BThackMatrix.scale(scale, scale, 1);
		}
		BThackRender.drawString(text, (getX() + 2) / scale, (getY() + 4) / scale, ClickGui.fontColor.getValue().getRGB());
		if (scale != 1)
			BThackMatrix.pop();
		if (opened || animation.getEase() < 1){
			BThackRender.enableScissor(ClickGui.applyGuiScale(getX()), ClickGui.applyGuiScale(getY()), ClickGui.applyGuiScale(Constants.CLICKGUI_FRAME_WIDTH), ClickGui.applyGuiScale(getHeight()));
			for (int i = 0; i < setting.getOptions().size(); i++){
				BThackRender.drawRect(parent.parent.getX(), parent.parent.getY() + offset + (Constants.CLICKGUI_BUTTON_HEIGHT * (i + 1)), parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + (Constants.CLICKGUI_BUTTON_HEIGHT * (i + 2)), this.setting.getIndex() == i ? ColorUtils.integrateAlpha(ModuleList.clickGui.color.getValue().hashCode(), (int) 255 * ModuleList.clickGui.opacity.getValue()) : ClickGui.BACKGROUND_COLOR);
				String textO = (setting.getOptions().get(i));
				BThackRender.drawString(textO, (getX() + 2), (getY() + ((Constants.CLICKGUI_BUTTON_HEIGHT - mc.textRenderer.fontHeight) / 2 ) + (Constants.CLICKGUI_BUTTON_HEIGHT * (i + 1))), ClickGui.fontColor.getValue().getRGB());
				if (ClickGui.moduleOutline.getValue()) BThackRender.drawOutlineRect(parent.parent.getX(), parent.parent.getY() + offset + (Constants.CLICKGUI_BUTTON_HEIGHT * (i + 1)), parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + (Constants.CLICKGUI_BUTTON_HEIGHT * (i + 2)), 1, Constants.CLICKGUI_BUTTON_OUTLINE_COLOR);
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
	public boolean updateComponent(int mouseX, int mouseY) {
		if (animation.getEase() < 1) parent.parent.refresh();

		if (!getVisible() || !parent.open) {
			opened = false;
			return true;
		}

		hovered = isMouseOnButton(mouseX, mouseY);

		y = parent.parent.getY() + offset;
		x = parent.parent.getX();

		return true;
	}

	public int getHeight(){
		return opened ? (int) ((Constants.CLICKGUI_BUTTON_HEIGHT * (setting.getOptions().size() + 1)) * animation.getEase()) : Constants.CLICKGUI_BUTTON_HEIGHT + (int) (Constants.CLICKGUI_BUTTON_HEIGHT * (setting.getOptions().size() * (1 - animation.getEase())));
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int button) {
		if (!getVisible() || !parent.open) return false;

		if (opened && button == 0 && this.parent.open){
			for (int x = 0; x < setting.getOptions().size(); x++){
				if (isMouseOnButton(mouseX, mouseY - ClickGui.applyGuiScale((Constants.CLICKGUI_BUTTON_HEIGHT) * (x + 1)))){
					setting.setIndex(x);
					setting.setValue(setting.getOptions().get(setting.getIndex()));
					setting.module.onChangeSetting(setting);
					setting.setValue(setting.getOptions().get(x));
					return false;
				}
			}
		}

		if (isMouseOnButton(mouseX, mouseY) && button == 1 && this.parent.open) {
			opened = !opened;
			parent.parent.refresh();
			animation.reset();
			return false;
		}

		if (isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
			int maxIndex = setting.getOptions().size();

			if (setting.getIndex() + 1 >= maxIndex) {
				setting.setIndex(0);
			} else {
				int currentIndex = setting.getIndex();
				setting.setIndex(currentIndex + 1);
			}

			setting.setValue(setting.getOptions().get(setting.getIndex()));
			setting.module.onChangeSetting(setting);
		}

		return isMouseOnButton(mouseX, mouseY);
	}
}
