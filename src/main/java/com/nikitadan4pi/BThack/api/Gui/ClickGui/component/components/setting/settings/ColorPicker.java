package com.nikitadan4pi.BThack.api.Gui.ClickGui.component.components.setting.settings;

import com.nikitadan4pi.BThack.Core.Render.BThackRender;
import com.nikitadan4pi.BThack.Core.Render.Utils.ColorUtils;
import com.nikitadan4pi.BThack.api.Animation.Animation;
import com.nikitadan4pi.BThack.api.Animation.Easing;
import com.nikitadan4pi.BThack.api.Gui.ClickGui.component.components.ModuleButton;
import com.nikitadan4pi.BThack.api.Gui.ClickGui.component.components.setting.AbstractSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.ColorSetting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.Constants;
import com.nikitadan4pi.BThack.api.Utils.MathUtils;
import com.nikitadan4pi.BThack.impl.Modules.CLIENT.ClickGui;

import java.awt.*;

public class ColorPicker extends AbstractSetting<ColorSetting> {

    private Color rgbColor;
    private float[] hsbColor;
    private float hue;
    private float alpha;

    private boolean opened = false;
    private final Animation animation = new Animation(Easing.CIRC_OUT, 300);

    private final ColorObject colorRect = new ColorObject() {
        @Override
        protected float getStartX() {
            return getX() + 5;
        }

        @Override
        protected float getStartY() {
            return getY() + 15;
        }

        @Override
        protected float getEndX() {
            return getX() + 5 + getWidth();
        }

        @Override
        protected float getEndY() {
            return getY() + 15 + getHeight();
        }

        @Override
        protected float getWidth() {
            return 51;
        }

        @Override
        protected float getHeight() {
            return 51;
        }
    };
    private final ColorObject hueRect = new ColorObject() {
        @Override
        protected float getStartX() {
            return getX() + 60;
        }

        @Override
        protected float getStartY() {
            return getY() + 15;
        }

        @Override
        protected float getEndX() {
            return getX() + 60 + getWidth();
        }

        @Override
        protected float getEndY() {
            return getY() + 15 + getHeight();
        }

        @Override
        protected float getWidth() {
            return 10;
        }

        @Override
        protected float getHeight() {
            return 51;
        }
    };
    private final ColorObject alphaRect = new ColorObject() {
        @Override
        protected float getStartX() {
            return getX() + 74;
        }

        @Override
        protected float getStartY() {
            return getY() + 15;
        }

        @Override
        protected float getEndX() {
            return getX() + 74 + getWidth();
        }

        @Override
        protected float getEndY() {
            return getY() + 15 + getHeight();
        }

        @Override
        protected float getWidth() {
            return 10;
        }

        @Override
        protected float getHeight() {
            return 51;
        }
    };

    public ColorPicker(ColorSetting setting, ModuleButton button , int offset, Module module) {
        super(offset, button, module, setting);
        updateColors();
    }

    private void updateColors() {
        rgbColor = setting.getValue();
        alpha = rgbColor.getAlpha() / 255f;
        hsbColor = Color.RGBtoHSB(rgbColor.getRed(), rgbColor.getGreen(), rgbColor.getBlue(), null);
        hue = hsbColor[0];
        colorRect.crosshairX = hsbColor[1] * colorRect.getWidth();
        colorRect.crosshairY = (1 - hsbColor[2]) * colorRect.getHeight();
        hueRect.crosshairY = hue * hueRect.getHeight();
        alphaRect.crosshairY = (1 - alpha) * alphaRect.getHeight();
    }

    @Override
    public int getHeight() {
        return opened ? (int) (80 * animation.getEase()) : Constants.CLICKGUI_BUTTON_HEIGHT + (int) (66 * (1 - animation.getEase()));
    }

    public boolean isMouseOnButton(int x, int y) {
        return x >= ClickGui.applyGuiScale(getX()) && x <= ClickGui.applyGuiScale(getX() + 100) &&
                y >= ClickGui.applyGuiScale(getY()) && y <= ClickGui.applyGuiScale(getY() + 14);
    }

    @Override
    public void renderComponent() {
        super.renderComponent();

        BThackRender.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth()), parent.parent.getY() + offset + getHeight(), ClickGui.BACKGROUND_COLOR);
        if (opened || animation.getEase() < 1) {
            BThackRender.enableScissor(ClickGui.applyGuiScale(getX()), ClickGui.applyGuiScale(getY()), ClickGui.applyGuiScale(Constants.CLICKGUI_FRAME_WIDTH), ClickGui.applyGuiScale(getHeight()));
            //colorRect
            BThackRender.draw4ColorRect(colorRect.getStartX(), colorRect.getStartY(), colorRect.getEndX(), colorRect.getEndY(), ColorUtils.WHITE, new Color(Color.HSBtoRGB(hue, 1f, 1f)).getRGB(), ColorUtils.WHITE, new Color(Color.HSBtoRGB(hue, 1f, 1f)).getRGB());
            BThackRender.draw4ColorRect(colorRect.getStartX(), colorRect.getStartY(), colorRect.getEndX(), colorRect.getEndY(), ColorUtils.TRANSPARENT, ColorUtils.TRANSPARENT, ColorUtils.BLACK, ColorUtils.BLACK);

            //alphaRect
            if(!setting.isBlockedAlpha())BThackRender.draw4ColorRect(alphaRect.getStartX(), alphaRect.getStartY(), alphaRect.getEndX(), alphaRect.getEndY(), new Color(rgbColor.getRed(), rgbColor.getGreen(), rgbColor.getBlue()).hashCode(), new Color(rgbColor.getRed(), rgbColor.getGreen(), rgbColor.getBlue()).hashCode(), ColorUtils.TRANSPARENT, ColorUtils.TRANSPARENT);

            //hueRect
            float hue = 0;
            float hueFactor = 1 / 52f;
            float hueY = 0;
            for (int i = 0; i < 52; i++) {
                Color hueColor = Color.getHSBColor(hue, 1f, 1f);
                BThackRender.drawRect(hueRect.getStartX(), hueRect.getStartY() + hueY, hueRect.getEndX(), hueRect.getStartY() + (hueY + 1), hueColor.hashCode());
                hue += hueFactor;
                hueY += 1;
            }
            drawHueCrosshair();
            drawColorCrosshair();
            if (!setting.isBlockedAlpha())drawAlphaCrosshair();
            BThackRender.drawString("R:" + rgbColor.getRed() + " G:" + rgbColor.getGreen() + " B:" + rgbColor.getBlue() + " A:" + rgbColor.getAlpha(), getX() + 2, colorRect.getEndY() + 4, -1, true);
            BThackRender.disableScissor();
        }
        BThackRender.drawString(setting.getName(), getX() + 2, getY() + 2, ColorUtils.WHITE);
        BThackRender.drawRect(getX() + Constants.CLICKGUI_FRAME_WIDTH - 12, getY() + 2, getX() + Constants.CLICKGUI_FRAME_WIDTH - 2, getY() + 12, rgbColor.hashCode());

    }

    private void drawColorCrosshair() {
        BThackRender.drawRect(colorRect.getStartX() + (colorRect.crosshairX - 1.5f), colorRect.getStartY() + (colorRect.crosshairY - 1.5f), colorRect.getStartX() + (colorRect.crosshairX + 1.5f), colorRect.getStartY() + (colorRect.crosshairY + 1.5f), ColorUtils.WHITE);
        BThackRender.drawOutlineRect(colorRect.getStartX() + (colorRect.crosshairX - 1.5f), colorRect.getStartY() + (colorRect.crosshairY - 1.5f), colorRect.getStartX() + (colorRect.crosshairX + 1.5f), colorRect.getStartY() + (colorRect.crosshairY + 1.5f), 1, ColorUtils.fastRGBA(0, 0, 0, 100));
    }

    private void drawHueCrosshair() {
        BThackRender.drawRect(hueRect.getStartX() - 1, hueRect.getStartY() + hueRect.crosshairY - 1, hueRect.getEndX() + 1, hueRect.getStartY() + hueRect.crosshairY + 2, ColorUtils.WHITE);
        BThackRender.drawOutlineRect(hueRect.getStartX() - 1, hueRect.getStartY() + hueRect.crosshairY - 1, hueRect.getEndX() + 1, hueRect.getStartY() + hueRect.crosshairY + 2, 1, ColorUtils.fastRGBA(0, 0, 0, 100));
    }

    private void drawAlphaCrosshair() {
        BThackRender.drawRect(alphaRect.getStartX() - 1, alphaRect.getStartY() + alphaRect.crosshairY - 1, alphaRect.getEndX() + 1, alphaRect.getStartY() + alphaRect.crosshairY + 2, ColorUtils.WHITE);
        BThackRender.drawOutlineRect(alphaRect.getStartX() - 1, alphaRect.getStartY() + alphaRect.crosshairY - 1, alphaRect.getEndX() + 1, alphaRect.getStartY() + alphaRect.crosshairY + 2, 1, ColorUtils.fastRGBA(0, 0, 0, 100));
    }

    @Override
    public boolean updateComponent(int mouseX, int mouseY) {
        if (!getVisible()) return true;
        if (animation.getEase() < 1) parent.parent.refresh();

        if (!setting.getValue().equals(rgbColor)) updateColors();

        if (colorRect.hovered) {
            float width = ClickGui.applyGuiScale(colorRect.getWidth());
            float height = ClickGui.applyGuiScale(colorRect.getHeight());
            float xF = MathUtils.applyRange(mouseX - ClickGui.applyGuiScale(colorRect.getStartX()), 0, width);
            float yF = MathUtils.applyRange(mouseY - ClickGui.applyGuiScale(colorRect.getStartY()), 0, height);
            xF = (xF / width) * colorRect.getWidth();
            yF = (yF / height) * colorRect.getHeight();

            Color color = new Color(Color.HSBtoRGB(hue, xF / colorRect.getWidth(), 1f - (yF / colorRect.getHeight())));
            setting.setValue(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (alpha * 255)));
            updateColors();
        }
        if (hueRect.hovered) {
            float height = ClickGui.applyGuiScale(hueRect.getHeight());
            float yF = MathUtils.applyRange(mouseY - ClickGui.applyGuiScale(hueRect.getStartY()), 0, height);
            yF = (yF / height) * hueRect.getHeight();

            hue = yF / colorRect.getHeight();
            Color color = new Color(Color.HSBtoRGB(hue, hsbColor[1], hsbColor[2]));
            setting.setValue(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (alpha * 255)));
            updateColors();
        }
        if (!setting.isBlockedAlpha()) {
            if (alphaRect.hovered) {
                float height = ClickGui.applyGuiScale(alphaRect.getHeight());
                float yF = MathUtils.applyRange(mouseY - ClickGui.applyGuiScale(alphaRect.getStartY()), 0, height);
                yF = (yF / height) * alphaRect.getHeight();

                Color color = new Color(Color.HSBtoRGB(hue, hsbColor[1], hsbColor[2]));
                setting.setValue(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) ((1 - (yF / alphaRect.getHeight())) * 255)));
                updateColors();
            }
        }

        return false;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (!this.getVisible()) return false;

        if (isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
            opened = !opened;
            parent.parent.refresh();
            animation.reset();
            return false;
        }
        if (opened && button == 0) {
            if (colorRect.isMouseOnObject(mouseX, mouseY)) {
                colorRect.hovered = true;
                return false;
            }
            if (hueRect.isMouseOnObject(mouseX, mouseY)) {
                hueRect.hovered = true;
                return false;
            }
            if (!setting.isBlockedAlpha()) {
                if (alphaRect.isMouseOnObject(mouseX, mouseY)) {
                    alphaRect.hovered = true;
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        colorRect.hovered = false;
        hueRect.hovered = false;
        alphaRect.hovered = false;
    }


    private static abstract class ColorObject {
        protected boolean hovered = false;
        protected float crosshairX = 0;
        protected float crosshairY = 0;

        protected abstract float getStartX();

        protected abstract float getStartY();

        protected abstract float getEndX();

        protected abstract float getEndY();

        protected abstract float getWidth();

        protected abstract float getHeight();

        protected boolean isMouseOnObject(double mouseX, double mouseY) {
            return mouseX > ClickGui.applyGuiScale(getStartX()) && mouseX < ClickGui.applyGuiScale(getEndX()) &&
                    mouseY > ClickGui.applyGuiScale(getStartY()) && mouseY < ClickGui.applyGuiScale(getEndY());
        }
    }
}
