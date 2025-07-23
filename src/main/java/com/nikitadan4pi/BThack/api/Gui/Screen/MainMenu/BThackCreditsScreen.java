package com.nikitadan4pi.BThack.api.Gui.Screen.MainMenu;

import com.nikitadan4pi.BThack.Core.Render.BThackRender;
import com.nikitadan4pi.BThack.Core.Render.Utils.ColorUtils;
import com.nikitadan4pi.BThack.api.Utils.DesktopUtils;
import com.nikitadan4pi.BThack.api.GuiSystem.Screen.BThackScreen;
import com.nikitadan4pi.BThack.api.GuiSystem.buttons.Button;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class BThackCreditsScreen extends BThackScreen {

    private final Screen parent;

    public BThackCreditsScreen(Screen parent) {
        super(Text.of("CreditsScreen"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        this.buttons.clear();

        this.buttons.add(Button.of(1, mc.getWindow().getScaledWidth() / 2, mc.getWindow().getScaledHeight() / 2,
                100, 10, "Youtube Channel"));
        this.buttons.add(Button.of(2, mc.getWindow().getScaledWidth() / 2, (mc.getWindow().getScaledHeight() / 2) + 22,
                100, 10, "Discord Channel"));
        this.buttons.add(Button.of(3, mc.getWindow().getScaledWidth() / 2, (mc.getWindow().getScaledHeight() / 2) + 44,
                100, 10, "My GitHub"));


        this.buttons.add(Button.of(10, mc.getWindow().getScaledWidth() / 2, mc.getWindow().getScaledHeight() - 22,
                100, 10, "Back"));


        this.buttons.forEach(button -> button.outline = true);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        BThackMainMenuScreen.drawWallpaper(mouseX, mouseY);

        BThackRender.drawVerticalGradientRect(0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), ColorUtils.TRANSPARENT, ColorUtils.fastRGBA(0,0,0, 240));

        super.render(context, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        switch (activeButton.getId()) {
            case 1:
                DesktopUtils.openURI("https://www.youtube.com/@nikitadan4pi");
                break;
            case 2:
                DesktopUtils.openURI("https://discord.gg/kbjb5Wu8ZQ");
                break;
            case 3:
                DesktopUtils.openURI("https://github.com/nikitadan4pi");
                break;
            case 10:
                mc.setScreen(parent);
                break;
        }

        return false;
    }
}
