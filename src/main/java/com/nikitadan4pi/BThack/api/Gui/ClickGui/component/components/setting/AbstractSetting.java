package com.nikitadan4pi.BThack.api.Gui.ClickGui.component.components.setting;

import com.nikitadan4pi.BThack.Constants;
import com.nikitadan4pi.BThack.api.Gui.ClickGui.component.Component;
import com.nikitadan4pi.BThack.api.Gui.ClickGui.component.components.ModuleButton;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.Setting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.impl.Modules.CLIENT.ClickGui;
import org.joml.Vector2i;

public abstract class AbstractSetting<T extends Setting<?>> extends Component {

    public int x;
    public int y;
    public int offset;
    public final Vector2i position = new Vector2i(0, 0);


    public boolean hovered;

    private boolean visible = true;

    public T setting;


    public final ModuleButton parent;
    public final Module module;

    public AbstractSetting(int offset, ModuleButton button, Module module, T setting) {
        this.offset = offset;
        this.parent = button;
        this.module = module;
        this.setting = setting;
    }

    @Override
    public void renderComponent() {
        position.set(parent.parent.getX(), parent.parent.getY() + offset);
    }

    @Override
    public void setOff(int newOff) {
        offset = newOff;
    }

    @Override
    public void updateDependencies(int offset) {
        if (setting.dependence != null)
            setVisible(setting.dependence.get());
    }

    public void refresh(int newOff) {
        offset = newOff;
        if (setting != null && setting.dependence != null)
            setVisible(setting.dependence.get());
    }

    @Override
    public int getHeight() {
        return Constants.CLICKGUI_BUTTON_HEIGHT;
    }

    public int getX() {
        return position.x;
    }

    public int getY() {
        return position.y;
    }


    @Override
    public abstract boolean updateComponent(int mouseX, int mouseY);

    @Override
    public abstract boolean mouseClicked(int mouseX, int mouseY, int button);

    public boolean isMouseOnButton(int x, int y) {
        return x >= ClickGui.applyGuiScale(this.x) && x <= ClickGui.applyGuiScale(this.x + Constants.CLICKGUI_FRAME_WIDTH) &&
                y >= ClickGui.applyGuiScale(this.y) && y <= ClickGui.applyGuiScale(this.y + Constants.CLICKGUI_BUTTON_HEIGHT);
    }

    public boolean getVisible() {
        return this.visible;
    }

    public void setVisible(boolean in) {
        this.visible = in;
    }
}
