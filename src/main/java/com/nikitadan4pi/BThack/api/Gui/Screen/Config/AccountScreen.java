package com.nikitadan4pi.BThack.api.Gui.Screen.Config;

import com.nikitadan4pi.BThack.api.GuiSystem.Screen.BThackScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class AccountScreen extends BThackScreen {
    public AccountScreen() {
        super(Text.of(""));
    }

    @Override
    protected void init() {
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        super.render(context, mouseX, mouseY, partialTicks);
    }
}
