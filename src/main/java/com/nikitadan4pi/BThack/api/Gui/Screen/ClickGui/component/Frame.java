package com.nikitadan4pi.BThack.api.Gui.Screen.ClickGui.component;

import com.nikitadan4pi.BThack.Constants;
import com.nikitadan4pi.BThack.Core.Client.Client;
import com.nikitadan4pi.BThack.Core.Client.ModuleList;
import com.nikitadan4pi.BThack.Core.Render.BThackMatrix;
import com.nikitadan4pi.BThack.Core.Render.BThackRender;
import com.nikitadan4pi.BThack.Core.Render.Utils.ColorUtils;
import com.nikitadan4pi.BThack.api.Animation.Animation;
import com.nikitadan4pi.BThack.api.Animation.Easing;
import com.nikitadan4pi.BThack.api.Category.Category;
import com.nikitadan4pi.BThack.api.Gui.Screen.ClickGui.component.components.ModuleButton;
import com.nikitadan4pi.BThack.api.Gui.Screen.ClickGui.component.components.setting.settings.Slider;
import com.nikitadan4pi.BThack.api.GuiSystem.BThackScreens;
import com.nikitadan4pi.BThack.api.Interfaces.Mc;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.Data;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.nikitadan4pi.BThack.impl.Modules.CLIENT.ClickGui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Frame implements Mc {
	public static final int BAR_HEIGHT = 12;
	public static final int BAR_OUTLINE_COLOR = ColorUtils.fastRGBA(0, 0, 0, 100);
	private static final List<Frame> globalFrames = new ArrayList<>();

	public int id;
	public final ArrayList<ModuleButton> buttons;
	public final String frameName;
	private boolean open;
	private final int width = 100;
	private int y;
	private int x;
	private boolean isDragging;
	public int dragX;
	public int dragY;
	public int height;
	public boolean buttonHovered = false;
	private final Animation frameAnimation = new Animation(Easing.CUBIC_OUT, 500);
	public float renderHeight;
	public final CopyOnWriteArrayList<ModuleButton> visibleButtons = new CopyOnWriteArrayList<>();

	public final Data<Slider> writingSlider;

	public Frame(String name, List<Module> modules, Data<Slider> writingSlider) {
		globalFrames.add(this);

		this.writingSlider = writingSlider;

		buttons = new ArrayList<>();
		frameName = name;
		x = 0;
		y = 60;
		dragX = 0;
		open = true;
		isDragging = false;
		int tY = BAR_HEIGHT;

		for(Module mod : modules) {
			ModuleButton button = new ModuleButton(mod, this, tY);
			buttons.add(button);
			tY += BAR_HEIGHT;
		}
	}
	
	public Frame(Category cat, Data<Slider> writingSlider) {
		this(cat.name(), Client.getModulesInCategory(cat), writingSlider);
	}
	
	public ArrayList<ModuleButton> getButtons() {
		return buttons;
	}
	
	public void setX(int newX) {
		this.x = newX;
	}
	
	public void setY(int newY) {
		this.y = newY;
	}

	public void setPosition(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void setDrag(boolean drag) {
		isDragging = drag;
	}
	
	public boolean isOpen() {
		return open;
	}
	
	public void setOpen(boolean open) {
		this.open = open;
	}

	public String getFrameName() {
		return this.frameName;
	}

	public void updateButtons(int mouseX, int mouseY) {
		for (ModuleButton button : getButtons()) {
			button.updateComponent(mouseX, mouseY);
		}
	}

	public void resetHovered() {
		for (ModuleButton button : getButtons()) {
			button.resetHovered();
		}
		buttonHovered = false;
	}

	public void resetAnimation() {
		if (isOpen())
			frameAnimation.reset();
	}

	public CopyOnWriteArrayList<ModuleButton> getVisibleButtons() {
		return visibleButtons;
	}

	public void close() {
		for (Component comp : getVisibleButtons())
			comp.mouseReleased(0, 0, 0);
	}

	/**
	 * @return - whether to continue the cycle
	 */
	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	public boolean updateClick(double mouseX, double mouseY, int mouseButton) {
		if(isWithinHeader((int) mouseX, (int) mouseY) && mouseButton == 0) {
			setDrag(true);
			dragX = (int) (mouseX / ClickGui.guiScale.getValue()) - getX();
			dragY = (int) (mouseY / ClickGui.guiScale.getValue()) - getY();
			return false;
		}
		if(isOpen()) {
			if(!getButtons().isEmpty()) {
				for(Component component : getButtons()) {
					component.mouseClicked((int) mouseX, (int) mouseY, mouseButton);
				}
			}
		}
		if(isWithinHeader((int) mouseX, (int) mouseY) && mouseButton == 1) {
			setOpen(!isOpen());
			if (isOpen()) refresh();
			else height = BAR_HEIGHT;
			frameAnimation.reset();
			return false;
		}
        return !isMouseOnFrame((int) mouseX, (int) mouseY);
    }

	public void updateRelease(int mouseX, int mouseY, int mouseButton) {
		if(isOpen()) {
			if(!getButtons().isEmpty()) {
				for(Component component : getButtons()) {
					component.mouseReleased(mouseX, mouseY, mouseButton);
				}
			}
		}
	}

	public Module getDescriptionModule(double mouseX, double mouseY) {
		if (isOpen()) {
			for (ModuleButton button : buttons) {
				if (button.isMouseOnButton((int) mouseX, (int) mouseY)) return button.module;
			}
		}
		return null;
	}
	public void renderFrame() {
		BThackMatrix.translate(0,0, 1);

		boolean needScissor = frameAnimation.getEase() < 1;
		renderHeight = needScissor ? (float) (height * (open ? frameAnimation.getEase() : 1 - frameAnimation.getEase())) : open ? height : 0;

		if (ModuleList.clickGui.isShaderEnabled()) {
			ModuleList.clickGui.prepareCurrentShader(1, 1);
			BThackRender.drawShader(ModuleList.clickGui.getCurrentShader(), getX(), getY(), getX() + Constants.CLICKGUI_FRAME_WIDTH, getY() + Constants.CLICKGUI_BAR_HEIGHT);
		} else
			BThackRender.drawRect(getX(), getY(), getX() + Constants.CLICKGUI_FRAME_WIDTH, getY() + Constants.CLICKGUI_BAR_HEIGHT, ModuleList.clickGui.color.getValue().hashCode());
		if (ModuleList.clickGui.frameOutline.getValue()) {
			if (ModuleList.clickGui.isShaderEnabled()) {
				ModuleList.clickGui.prepareCurrentShader(1, 1);
				BThackRender.drawShaderOutlineRect(ModuleList.clickGui.getCurrentShader(), getX() - 1, getY() - 1, getX() + Constants.CLICKGUI_FRAME_WIDTH + 1, getY() + renderHeight + Constants.CLICKGUI_BAR_HEIGHT + 1, 1);
			} else
				BThackRender.drawOutlineRect(getX() - 1, getY() - 1, getX() + Constants.CLICKGUI_FRAME_WIDTH + 1, getY() + renderHeight + Constants.CLICKGUI_BAR_HEIGHT + 1, 1, ClickGui.getClickGuiColor(true));
		}

		BThackRender.drawString(frameName, getX() + (Constants.CLICKGUI_FRAME_WIDTH / 2f) - (mc.textRenderer.getWidth(frameName) / 2f), getY() + (Constants.CLICKGUI_BAR_HEIGHT / 2f) - (mc.textRenderer.fontHeight / 2f), ModuleList.clickGui.textColor.getValue().hashCode(), true);

		if((open || frameAnimation.getEase() < 1) && !getVisibleButtons().isEmpty()) {
			if (needScissor)
				BThackRender.enableScissor(ClickGui.applyGuiScale(getX()), ClickGui.applyGuiScale(getY() + Constants.CLICKGUI_BAR_HEIGHT), ClickGui.applyGuiScale(Constants.CLICKGUI_FRAME_WIDTH), (int) ClickGui.applyGuiScale(renderHeight));
			for(Component component : getVisibleButtons())
				component.renderComponent();
			BThackMatrix.translate(0, 0, -1);
			if (needScissor)
				BThackRender.disableScissor();
		}
	}


	public void refresh() {
		visibleButtons.clear();
		int off = Constants.CLICKGUI_BAR_HEIGHT;
		for(ModuleButton moduleButton : buttons) {
			moduleButton.refresh(off);
			off += moduleButton.getHeight();
			visibleButtons.add(moduleButton);
		}
		height = off - Constants.CLICKGUI_BAR_HEIGHT;
	}

	public void updateDependencies() {
		int off = BAR_HEIGHT;
		for(Component comp : buttons) {
			comp.updateDependencies(off);
			off += comp.getHeight();
		}
		height = off;
	}

	public void tick() {
		for (ModuleButton button : buttons) {
			button.tick();
		}
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void updatePosition(int mouseX, int mouseY) {
		if(isDragging) {
			setX(mouseX - dragX);
			setY(mouseY - dragY);
		}
	}

	public void moveFrame(int keyCode) {
		switch (keyCode) {
			case KeyboardUtils.KEY_LEFT:
				x -= 5;
				break;
			case KeyboardUtils.KEY_RIGHT:
				x += 5;
				break;
			case KeyboardUtils.KEY_UP:
				y -= 5;
				break;
			case KeyboardUtils.KEY_DOWN:
				y += 5;
		}
	}

	public void moveFrame(double deltaX, double deltaY) {
		x += (int) (deltaX * 7);
		y += (int) (deltaY * 7);
	}
	
	public boolean isWithinHeader(int mouseX, int mouseY) {
        return mouseX >= ClickGui.applyGuiScale(x) && mouseX <= ClickGui.applyGuiScale(x + width) &&
				mouseY >= ClickGui.applyGuiScale(y) && mouseY <= ClickGui.applyGuiScale(y + BAR_HEIGHT);
    }

	public boolean isMouseOnFrame(int mouseX, int mouseY) {
		return mouseX >= ClickGui.applyGuiScale(x) && mouseX <= ClickGui.applyGuiScale(x + width) &&
				mouseY >= ClickGui.applyGuiScale(y) && mouseY <= ClickGui.applyGuiScale(y + height);
	}

	public static List<Frame> getGlobalFrames() {
		return new ArrayList<>(globalFrames);
	}
	
}
