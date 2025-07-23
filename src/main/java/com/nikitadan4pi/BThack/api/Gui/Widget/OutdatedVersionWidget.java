package com.nikitadan4pi.BThack.api.Gui.Widget;

import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.ferra13671.TextureUtils.GLTexture;
import com.ferra13671.TextureUtils.PathMode;
import com.nikitadan4pi.BThack.BThack;
import com.nikitadan4pi.BThack.Core.Render.BThackRender;
import com.nikitadan4pi.BThack.api.GuiSystem.ScreenWidget;
import com.nikitadan4pi.BThack.api.GuiSystem.buttons.Button;
import com.nikitadan4pi.BThack.api.Utils.DesktopUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Formatting;

public class OutdatedVersionWidget extends ScreenWidget {

    public static final GLTexture CHIBI1 = GLTexture.fromPath("assets/bthack/chibi/chibi1.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA, false);

    public OutdatedVersionWidget() {
        super(310, 200, 1);
    }

    @Override
    public void init() {
        super.init();

        buttons.clear();
        buttons.add(Button.of(1, (int) xRight - 70, (int) yUp + 100, 65, 10, "lang.widget.OutdatedVersion.Yes"));
        buttons.add(Button.of(2, (int) xRight - 70, (int) yUp + 100 + 22, 65, 10, "lang.widget.OutdatedVersion.No"));
        buttons.add(Button.of(3, (int) xRight - 70, (int) yUp + 100 + 44, 65, 10, "lang.widget.OutdatedVersion.AlwaysNo"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        drawPlate();

        BThackRender.drawTextureRect(CHIBI1, xLeft, yUp + 50, xLeft + 150, yDown);

        float y = yUp + 5;
        final float x = xLeft + 5;
        BThackRender.drawString(LanguageSystem.translate("lang.widget.OutdatedVersion.helloMessage"), x, y, -1, true);
        y += mc.textRenderer.fontHeight + 15;

        String currentText = String.format(LanguageSystem.translate("lang.widget.OutdatedVersion.Current"), Formatting.GRAY + BThack.instance.VERSION);
        BThackRender.drawString(currentText, x, y, -1, true);
        y += mc.textRenderer.fontHeight + 5;

        String newText = String.format(LanguageSystem.translate("lang.widget.OutdatedVersion.New"), Formatting.GREEN + BThack.instance.versionInfo.getNewVersion());
        BThackRender.drawString(newText, x, y, -1, true);

        String askText = LanguageSystem.translate("lang.widget.OutdatedVersion.UpdateMessage");
        BThackRender.drawString(askText, xRight - 5 - mc.textRenderer.getWidth(askText), yUp + 40, -1, true);

        super.render(context, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        switch (activeButton.getId()) {
            case 1 -> {
                DesktopUtils.openURI("https://github.com/Ferra13671/BThack/releases/download/" + BThack.instance.versionInfo.getNewVersion() + "/BThack-" + BThack.instance.MC_VERSION + "-fabric" + BThack.instance.versionInfo.getNewVersion().replace(BThack.instance.MC_VERSION, "") + ".jar");
                mc.stop();
                close();
                //((UpdateWidget) BThackWidgets.UPDATE).startUpdate();
                //parent.widgetManage.addWidget(BThackWidgets.UPDATE);
            }
            case 2 -> {
                BThack.instance.versionInfo.setNeedShowAgainOneRelease(false);
                close();
            }
            case 3 -> {
                BThack.instance.versionInfo.setNeedShowAgainAllReleases(false);
                close();
            }
        }

        return false;
    }
}
