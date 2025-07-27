package com.nikitadan4pi.BThack.api.Gui.Screen.ClickGui.component.components.setting.settings;

import com.nikitadan4pi.BThack.Constants;
import com.nikitadan4pi.BThack.Core.Client.ModuleList;
import com.nikitadan4pi.BThack.Core.Render.BThackRender;
import com.nikitadan4pi.BThack.Core.Render.Utils.ColorUtils;
import com.nikitadan4pi.BThack.api.Animation.Animation;
import com.nikitadan4pi.BThack.api.Animation.Easing;
import com.nikitadan4pi.BThack.api.Gui.Screen.ClickGui.component.components.ModuleButton;
import com.nikitadan4pi.BThack.api.Gui.Screen.ClickGui.component.components.setting.AbstractSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.impl.Modules.CLIENT.ClickGui;

import java.awt.*;

import static com.nikitadan4pi.BThack.api.Module.Module.mc;

public class Checkbox extends AbstractSetting<BooleanSetting> {

	private final BooleanSetting set;

	protected final Animation animation = new Animation(Easing.LINEAR, 250);

	public Checkbox(BooleanSetting option, ModuleButton button, int offset, Module module) {
		super(offset, button, module, option);
		set = option;
		position.x = button.parent.getX() + button.parent.getWidth();
		position.y = button.parent.getY() + button.offset;
		animation.reset();
	}

	@Override
	public void renderComponent() {
		super.renderComponent();

		BThackRender.drawRect(getX(), getY(), getX() + Constants.CLICKGUI_FRAME_WIDTH, getY() + getHeight(), hovered ? ClickGui.BACKGROUND_HOVERED_COLOR : ClickGui.BACKGROUND_COLOR);

		if (needRenderPlate()) {
			int alpha = getAlpha();
			if (ModuleList.clickGui.isShaderEnabled()) {
				ModuleList.clickGui.prepareCurrentShader(alpha / 255f, 0.7f);
				BThackRender.drawShader(ModuleList.clickGui.getCurrentShader(), getX() + 1, getY(), getX() + Constants.CLICKGUI_FRAME_WIDTH - 1, getY() + getHeight());
			} else
				BThackRender.drawRect(getX() + 1, getY(), getX() + Constants.CLICKGUI_FRAME_WIDTH - 1, getY() + getHeight(), ColorUtils.integrateAlpha(ClickGui.getClickGuiColor(true), alpha));

			BThackRender.drawOutlineRect(getX() + 1, getY(), getX() + Constants.CLICKGUI_FRAME_WIDTH - 1, getY() + getHeight(), 1, Constants.CLICKGUI_BUTTON_OUTLINE_COLOR);
		}

		BThackRender.drawString(getText(), getX() + 7, getY() + 4, ModuleList.clickGui.textColor.getValue().hashCode());
	}

	protected int getAlpha() {
		return (int) (255 * (setting.getValue() ? animation.getEase() : 1 - animation.getEase()));
	}

	protected boolean needRenderPlate() {
		return setting.getValue() || animation.getEase() < 1;
	}

	@Override
	public boolean updateComponent(int mouseX, int mouseY) {

		if (!getVisible()) return true;

		hovered = isMouseOnButton(mouseX, mouseY);
		//position.y = parent.parent.getY() + offset;
		//position.x = parent.parent.getX();
		return true;
	}

	protected String getText(){
		return setting.getName();
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int button) {
		if (!getVisible()) return false;

		if (isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
			animation.reset();
			set.setValue(!set.getValue());
			set.module.onChangeSetting(set);
		}

		return isMouseOnButton(mouseX, mouseY);
	}
}
