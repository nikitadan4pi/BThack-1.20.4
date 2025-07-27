package com.nikitadan4pi.BThack.api.Gui.Screen.ClickGui.component.components.setting.settings;


import com.nikitadan4pi.BThack.Constants;
import com.nikitadan4pi.BThack.Core.Render.BThackRender;
import com.nikitadan4pi.BThack.api.Gui.Screen.ClickGui.component.components.ModuleButton;
import com.nikitadan4pi.BThack.api.Gui.Screen.ClickGui.component.components.setting.AbstractSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.KeyCodeSetting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.nikitadan4pi.BThack.impl.Modules.CLIENT.ClickGui;

import static com.nikitadan4pi.BThack.api.Module.Module.mc;

public class KeyCode extends AbstractSetting<KeyCodeSetting> {

    private final KeyCodeSetting set;

    private boolean binding;

    public KeyCode(ModuleButton button, int offset, KeyCodeSetting option, Module module) {
        super(offset, button, module, option);
        set = option;
        position.x = button.parent.getX() + button.parent.getWidth();
        position.y = button.parent.getY() + button.offset;
    }

    @Override
    public void renderComponent() {
        BThackRender.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + 15, this.hovered ? ClickGui.BACKGROUND_HOVERED_COLOR : ClickGui.BACKGROUND_COLOR);

        BThackRender.drawString(binding ? "< PRESS KEY >" : (setting.getName() + ": " + KeyboardUtils.getKeyName(set.getValue())), parent.parent.getX() + 2, parent.parent.getY() + offset + ((Constants.CLICKGUI_BUTTON_HEIGHT - mc.textRenderer.fontHeight) / 2 ), ClickGui.textColor.getValue().hashCode());
    }

    @Override
    public boolean updateComponent(int mouseX, int mouseY) {
        if (!getVisible() || !parent.open) return true;
        hovered = isMouseOnButton(mouseX, mouseY);
        return true;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (!getVisible() || !parent.open) return false;

        if(isMouseOnButton(mouseX, mouseY) && button == 0 && parent.open) {
            binding = !binding;
        }

        return isMouseOnButton(mouseX, mouseY);
    }

    @Override
    public void keyTyped(int key) {
        if (!getVisible()) return;

        if (binding) {
            if (key == KeyboardUtils.KEY_DELETE) {
                set.setValue(0);
                set.module.onChangeSetting(set);
                binding = false;
            } else if (key != KeyboardUtils.KEY_ESCAPE) {
                set.setValue(key);
                set.module.onChangeSetting(set);
                binding = false;
            }
        }
    }
}
