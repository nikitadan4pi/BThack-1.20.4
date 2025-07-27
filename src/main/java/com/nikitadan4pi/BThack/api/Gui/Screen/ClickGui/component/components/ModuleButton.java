package com.nikitadan4pi.BThack.api.Gui.Screen.ClickGui.component.components;

import com.nikitadan4pi.BThack.Constants;
import com.nikitadan4pi.BThack.Core.Client.ModuleList;
import com.nikitadan4pi.BThack.Core.Render.BThackRender;
import com.nikitadan4pi.BThack.Core.Render.Utils.ColorUtils;
import com.nikitadan4pi.BThack.api.Animation.Animation;
import com.nikitadan4pi.BThack.api.Animation.Easing;
import com.nikitadan4pi.BThack.api.Gui.Screen.ClickGui.component.Component;
import com.nikitadan4pi.BThack.api.Gui.Screen.ClickGui.component.Frame;
import com.nikitadan4pi.BThack.api.Gui.Screen.ClickGui.component.components.setting.AbstractSetting;
import com.nikitadan4pi.BThack.api.Gui.Screen.ClickGui.component.components.setting.settings.Keybind;
import com.nikitadan4pi.BThack.api.Gui.Screen.ClickGui.component.components.setting.settings.Visible;
import com.nikitadan4pi.BThack.api.Interfaces.Mc;
import com.nikitadan4pi.BThack.api.Managers.Managers;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.Setting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.impl.Modules.CLIENT.ClickGui;

import java.awt.*;
import java.util.ArrayList;

public class ModuleButton extends Component implements Mc {
	public static final int BUTTON_HEIGHT = Constants.CLICKGUI_BUTTON_HEIGHT;

	public Module module;
	public Frame parent;
	public int offset;

	public boolean open = false;
	public boolean renderOpen = false;

	private boolean scInvert = false;
	private boolean isHovered;
	private final Animation settingColorAnimation = new Animation(Easing.LINEAR, 1300);
	private Animation settingAnimation = new Animation(Easing.CIRC_OUT, 500);
	private ArrayList<AbstractSetting> settings = new ArrayList<>();
	private float alphaDelta = 1;
	private boolean alphaDeltaInverse = true;
	private Animation animation = new Animation(Easing.CIRC_OUT, 500);
	private final Animation toggleAnimation = new Animation(Easing.LINEAR, 250);
	private double lastAnimFactor = 0;
	private int animatedSettingsHeight = 0;

	public ModuleButton(Module module, Frame parent, int offset) {
		this.module = module;
		this.parent = parent;
		this.offset = offset;
		int opYValue = offset + 14;

		for (Setting<?> s : Managers.SETTINGS_MANAGER.getSettingsByMod(module)) {
			if (s == null) continue;

			AbstractSetting<?> comp = s.asSettingButton(this, opYValue);
			if (comp != null) {
				settings.add(comp);
				opYValue += comp.getHeight();
			}
		}

		if (module.allowRemapVisible) {
			Visible visibleComp = new Visible(this, opYValue, module);
			if (visibleComp != null) {
				settings.add(visibleComp);
				opYValue += visibleComp.getHeight();
			}
		}

		if (module.allowRemapKeyCode) {
			Keybind keybindComp = new Keybind(this, opYValue);
			if (keybindComp != null) {
				settings.add(keybindComp);
			}
		}
	}

	@Override
	public void setOff(int newOff) {
		setOffInternal(newOff);
	}

	@Override
	public void updateDependencies(int offset) {
		for (AbstractSetting comp : settings) {
			comp.updateDependencies(0);
		}

		setOffInternal(offset);
	}

	private void setOffInternal(int offset) {
		this.offset = offset;
		int opY = offset + BUTTON_HEIGHT;
		for(AbstractSetting comp : settings) {
			comp.setOff(opY);
			if (comp.getVisible())
				opY += comp.getHeight();
		}
	}

	protected void drawEnabledBackground() {
		float _alpha = (int) (ClickGui.INT_OPACITY * (module.isEnabled() ? toggleAnimation.getEase() : 1 - toggleAnimation.getEase())) / 255f;
		if (ModuleList.clickGui.isShaderEnabled()) {
			ModuleList.clickGui.prepareCurrentShader(_alpha, isHovered ? 0.9f : 0.7f);
			BThackRender.drawShader(ModuleList.clickGui.getCurrentShader(), parent.getX(), parent.getY() + offset, parent.getX() + Constants.CLICKGUI_FRAME_WIDTH, parent.getY() + Constants.CLICKGUI_BUTTON_HEIGHT + offset);
		} else {
			BThackRender.drawRect(parent.getX(), parent.getY() + offset, parent.getX() + Constants.CLICKGUI_FRAME_WIDTH, parent.getY() + Constants.CLICKGUI_BUTTON_HEIGHT + offset,
					ColorUtils.integrateAlpha(
							isHovered ?
									new Color(ClickGui.getClickGuiColor(true)).hashCode() :
									new Color(ClickGui.getClickGuiColor(true)).darker().hashCode(),
							(int) (_alpha * 255)
					)
			);
		}
	}

	protected void drawNormalBackground() {
		BThackRender.drawRect(parent.getX(), parent.getY() + offset, parent.getX() + Constants.CLICKGUI_FRAME_WIDTH, parent.getY() + Constants.CLICKGUI_BUTTON_HEIGHT + offset,
				ColorUtils.integrateAlpha(
						isHovered ?
								ModuleList.clickGui.backgroundColor.getBrighterValue().hashCode() :
								ModuleList.clickGui.backgroundColor.getValue().hashCode(),
						ClickGui.INT_OPACITY
				)
		);
	}

	private int getModuleTextColor() {
		return ModuleList.clickGui.opacity.getValue() > 0.4 ? ModuleList.clickGui.textColor.getValue().hashCode() : (module.isEnabled() ? ClickGui.getClickGuiColor(true) : ModuleList.clickGui.textColor.getValue().hashCode());
	}

	@Override
	public void renderComponent() {

		if (!module.isEnabled() || toggleAnimation.getEase() < 1) drawNormalBackground();
		if (module.isEnabled() || toggleAnimation.getEase() < 1) drawEnabledBackground();
		if (ModuleList.clickGui.moduleOutline.getValue())
			BThackRender.drawOutlineRect(parent.getX(), parent.getY() + offset, parent.getX() + Constants.CLICKGUI_FRAME_WIDTH, parent.getY() + Constants.CLICKGUI_BUTTON_HEIGHT + offset, 1, Constants.CLICKGUI_BUTTON_OUTLINE_COLOR);
		if (!settings.isEmpty())
			BThackRender.drawString(open ? "-" : "+", (parent.getX() + parent.getWidth() - 10), (parent.getY() + offset + 2), ClickGui.textColor.getValue().getRGB());
		BThackRender.drawString(module.getName(), (parent.getX() + 5), (parent.getY() + offset + (Constants.CLICKGUI_BUTTON_HEIGHT / 2f) - (mc.textRenderer.fontHeight) / 2f), getModuleTextColor());
		getHeight();
		if(renderOpen || open || settingAnimation.getEase() < 1) {
			if(!settings.isEmpty()) {
				BThackRender.enableScissor(ClickGui.applyGuiScale(parent.getX()), ClickGui.applyGuiScale(parent.getY() + offset), ClickGui.applyGuiScale(Constants.CLICKGUI_FRAME_WIDTH), ClickGui.applyGuiScale(animatedSettingsHeight + Constants.CLICKGUI_BUTTON_HEIGHT));
				for(AbstractSetting<?> set : settings)
					if (set.getVisible())
						set.renderComponent();
				BThackRender.disableScissor();
				if (ModuleList.clickGui.settingsOutline.getValue())
					BThackRender.drawOutlineRect(parent.getX(), parent.getY() + offset, parent.getX() + Constants.CLICKGUI_FRAME_WIDTH, parent.getY() + offset + animatedSettingsHeight + Constants.CLICKGUI_BUTTON_HEIGHT, 1, ColorUtils.fastRGBA(255, 255, 255, Math.max(1, (int) ((scInvert ? 1 - settingColorAnimation.getEase() : settingColorAnimation.getEase()) * 255))));
			}
		}
	}

	@Override
	public int getHeight() {
		//if(renderOpen || open) {
			int height = 0;
			for (AbstractSetting<?> component : settings)
				if (component.getVisible())
					height += component.getHeight();
			height = open ? (int) (lastAnimFactor * height) : (int) (height - (lastAnimFactor * height));
			animatedSettingsHeight = height;
			height += Constants.CLICKGUI_BUTTON_HEIGHT;
			return height;
		//}
		//return Constants.CLICKGUI_BUTTON_HEIGHT;
	}

	@Override
	public boolean updateComponent(int mouseX, int mouseY) {
		if (isMouseOnButton(mouseX, mouseY) && !isHovered) {
			if (parent.buttonHovered) parent.resetHovered();
			isHovered = true;
			parent.buttonHovered = true;
		}
		if(!settings.isEmpty()) {
			for(Component comp : settings) {
				comp.updateComponent(mouseX, mouseY);
			}
		}
		return false;
	}

	public void tick() {
		if (animation.getPassedMillis() > animation.getMillis() + 50) if (renderOpen != open) renderOpen = open;
		if (renderOpen) {
			if (alphaDelta > 1) alphaDeltaInverse = true;
			if (alphaDelta <= 0.3) alphaDeltaInverse = false;
			alphaDelta += alphaDeltaInverse ? -0.03f : 0.03f;
		}
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int button) {
		if (isMouseOnButton(mouseX, mouseY)) {
			if (button == 0) {
				module.toggle();
				toggleAnimation.reset();
			}
			if (button == 1) {
				if (renderOpen == open) {
					settingAnimation = new Animation(ClickGui.getCurrentEasing(), ModuleList.clickGui.animationTime.getValue().intValue());
					open = !open;
					settingAnimation.reset();
					lastAnimFactor = 0;
					parent.refresh();
				}
			}
		}
		if (open) {
			for (Component comp : settings)
				comp.mouseClicked(mouseX, mouseY, button);
			parent.refresh();
		}

		return isMouseOnButton(mouseX, mouseY);
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		for(Component comp : settings) {
			comp.mouseReleased(mouseX, mouseY, mouseButton);
		}
		if (open)
			parent.updateDependencies();
	}

	@Override
	public void keyTyped(int key) {
		for(Component comp : this.settings) {
			comp.keyTyped(key);
		}
	}

	public boolean isMouseOnButton(int x, int y) {
        return x > ClickGui.applyGuiScale(parent.getX()) && x < ClickGui.applyGuiScale(parent.getX() + parent.getWidth()) &&
				y > ClickGui.applyGuiScale(parent.getY() + offset) && y < ClickGui.applyGuiScale(parent.getY() + BUTTON_HEIGHT + offset);
    }

	public void updateAnim() {
		if (settingAnimation.getPassedMillis() <= settingAnimation.getMillis() + 50) {
			lastAnimFactor = settingAnimation.getEase();
			parent.refresh();
		} else if (lastAnimFactor != 1) {
			lastAnimFactor = 1;
			parent.refresh();
		}
	}

	public void refresh(int newOff) {
		this.offset = newOff;
		int opY = offset + Constants.CLICKGUI_BUTTON_HEIGHT;
		for(AbstractSetting<?> comp : settings) {
			comp.refresh(opY);
			if (comp.getVisible())
				opY += comp.getHeight();
		}
	}

	public void resetHovered() {
		isHovered = false;
	}
}
