package com.nikitadan4pi.BThack.api.Gui.Widget;

import com.ferra13671.TextureUtils.GLTexture;
import com.ferra13671.TextureUtils.PathMode;
import com.nikitadan4pi.BThack.Core.Render.BThackRender;
import com.nikitadan4pi.BThack.api.GuiSystem.ScreenWidget;
import com.nikitadan4pi.BThack.api.GuiSystem.buttons.Button;
import com.nikitadan4pi.BThack.Core.Client.ModuleList;
import net.minecraft.client.gui.DrawContext;

public class LanguageSelectorWidget extends ScreenWidget {

    public static final GLTexture CHIBI2 = GLTexture.fromPath("assets/bthack/chibi/chibi2.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA, false);
    public static final GLTexture EN_FLAG = GLTexture.fromPath("assets/bthack/flags/en_flag.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA, false);
    public static final GLTexture RU_FLAG = GLTexture.fromPath("assets/bthack/flags/ru_flag.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA, false);
    public static final GLTexture PL_FLAG = GLTexture.fromPath("assets/bthack/flags/pl_flag.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA, false);

    public LanguageSelectorWidget() {
        super(310, 150, 1);
    }

    @Override
    public void init() {
        super.init();

        buttons.clear();

        buttons.add(Button.of(1, (int) xRight - 139, (int) yDown - 45, 20, 10, "EN"));
        buttons.add(Button.of(2, (int) xRight - 95, (int) yDown - 45, 20, 10, "RU"));
        buttons.add(Button.of(3, (int) xRight - 51, (int) yDown - 45, 20, 10, "PL"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        drawPlate();

        BThackRender.drawTextureRect(CHIBI2, xLeft, yUp, xLeft + 150, yDown);

        final float x = xRight - 95;
        float y = yUp + 5;

        BThackRender.drawCenteredString("Welcome!", x, y, -1);
        y += mc.textRenderer.fontHeight + 4;

        String text = "Select the language to be used";
        BThackRender.drawCenteredString(text, x, y, -1);

        float x2 = xRight - 95;
        float y2 = yDown - 70;

        BThackRender.drawTextureRect(EN_FLAG, x2 - 64, y2 - 11, x2 - 24, y2 + 11);
        BThackRender.drawTextureRect(RU_FLAG, x2 - 20, y2 - 11, x2 + 20, y2 + 11);
        BThackRender.drawTextureRect(PL_FLAG, x2 + 24, y2 - 11, x2 + 64, y2 + 11);

        super.render(context, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        switch (activeButton.getId()) {
            case 1 -> {
                ModuleList.clientSetting.language.setValue("PL");
                ModuleList.clientSetting.language.setIndex(2);
                close();
            }
            case 2 -> {
                ModuleList.clientSetting.language.setValue("RU");
                ModuleList.clientSetting.language.setIndex(1);
                close();
            }
            case 3 -> {
                ModuleList.clientSetting.language.setValue("PL");
                ModuleList.clientSetting.language.setIndex(2);
                close();
            }
        }

        return false;
    }
}
