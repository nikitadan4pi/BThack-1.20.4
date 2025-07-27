package com.nikitadan4pi.BThack.api.Gui.Screen.ClickGui;

import com.nikitadan4pi.BThack.Core.Client.ModuleList;
import com.nikitadan4pi.BThack.Core.Render.BThackMatrix;
import com.nikitadan4pi.BThack.Core.Render.BThackRender;
import com.nikitadan4pi.BThack.Core.Render.Utils.ColorUtils;
import com.nikitadan4pi.BThack.api.Animation.Animation;
import com.nikitadan4pi.BThack.api.Animation.Easing;
import com.nikitadan4pi.BThack.api.Interfaces.Mc;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Module.PluginModule;

import java.io.Closeable;

public class DescriptionBar implements Closeable, Mc {
    private final Animation moveAnimation = new Animation(Easing.SINE_OUT, 400);
    private final Animation alphaAnimation = new Animation(Easing.LINEAR, 250);
    private final Module module;

    private boolean closing = false;

    public DescriptionBar(Module module) {
        this.module = module;
    }

    public boolean isClosing() {
        return closing;
    }

    @Override
    public void close() {
        closing = true;
        alphaAnimation.reset();
    }

    public boolean needRemove() {
        return closing && alphaAnimation.getEase() >= 1;
    }

    public Module getModule() {
        return module;
    }

    public void render() {
        int alpha = closing ? (int) ((1 - alphaAnimation.getEase()) * 255) : (int) (alphaAnimation.getEase() * 255);
        ModuleList.clickGui.prepareCurrentShader(alpha / 255f, 1);

        int scaledHeight = (int) (mc.getWindow().getScaledHeight() / ModuleList.clickGui.guiScale.getValue());

        BThackMatrix.push();
        if (module instanceof PluginModule pluginMod) {
            float pluginNameLength = mc.textRenderer.getWidth("Plugin: " + pluginMod.plugin.pluginName) + 10;
            float descriptionLength = mc.textRenderer.getWidth(module.getDescription()) + 10;
            float length = Math.max(pluginNameLength, descriptionLength);

            BThackMatrix.translate((float) -((1 - moveAnimation.getEase()) * length), 0, 0);

            BThackRender.drawRect(1, scaledHeight - 30, length, scaledHeight -1, ColorUtils.integrateAlpha(ModuleList.clickGui.backgroundColor.getValue().hashCode(), alpha));
            BThackRender.drawShaderOutlineRect(ModuleList.clickGui.getCurrentShader(), 1, scaledHeight - 30, length, scaledHeight -1, 1);
            BThackRender.drawString(module.getDescription(), 6, scaledHeight - 7 - (mc.textRenderer.fontHeight) / 2f, ColorUtils.integrateAlpha(ModuleList.clickGui.textColor.getValue().hashCode(), alpha));
            BThackRender.drawString("Plugin: " + pluginMod.plugin.pluginName, 6, scaledHeight - 19 - (mc.textRenderer.fontHeight / 2f), ColorUtils.integrateAlpha(ModuleList.clickGui.textColor.getValue().hashCode(), alpha));
        } else {
            float length = mc.textRenderer.getWidth(module.getDescription()) + 10;

            BThackMatrix.translate((float) -((1 - moveAnimation.getEase()) * length), 0, 0);

            BThackRender.drawRect(1, scaledHeight - 18, length, scaledHeight -1, ColorUtils.integrateAlpha(ModuleList.clickGui.backgroundColor.getValue().hashCode(), alpha));
            BThackRender.drawShaderOutlineRect(ModuleList.clickGui.getCurrentShader(), 1, scaledHeight - 18, length, scaledHeight -1, 1);
            BThackRender.drawString(module.getDescription(), 6, scaledHeight - 9.5f - (mc.textRenderer.fontHeight / 2f), ColorUtils.integrateAlpha(ModuleList.clickGui.textColor.getValue().hashCode(), alpha));
        }
        BThackMatrix.pop();
    }
}
