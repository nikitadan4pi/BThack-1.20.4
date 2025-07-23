package com.nikitadan4pi.BThack.api.GuiSystem;

import com.nikitadan4pi.BTbot.api.Utils.Generate.NumberGenerator;
import com.nikitadan4pi.BThack.BThack;
import com.nikitadan4pi.BThack.Core.Render.BThackRender;
import com.nikitadan4pi.BThack.Core.Render.Utils.ColorUtils;
import com.nikitadan4pi.BThack.api.GuiSystem.Screen.BThackScreen;
import net.minecraft.text.Text;

public class ScreenWidget extends BThackScreen {
    public final float animationSpeed;
    private final float width;
    private final float height;

    protected BThackScreen parent;
    protected static float xLeft = 0;
    protected static float yUp = 0;
    protected static float xRight = 0;
    protected static float yDown = 0;

    public boolean needClose = false;

    public ScreenWidget(float width, float height, float animationSpeed) {
        super(Text.of("Widget-" + NumberGenerator.generateInt(10000, 99999)));
        this.width = width;
        this.height = height;
        this.animationSpeed = animationSpeed;
    }

    public void setParent(BThackScreen parent) {
        this.parent = parent;
    }

    @Override
    public void init() {
        super.init();
        xLeft = (mc.getWindow().getScaledWidth() / 2f) - (width / 2);
        yUp = (mc.getWindow().getScaledHeight() / 2f) - (height / 2);
        xRight = (mc.getWindow().getScaledWidth() / 2f) + (width / 2);
        yDown = (mc.getWindow().getScaledHeight() / 2f) + (height / 2);
    }

    @Override
    public void close() {
        needClose = true;
    }

    @Override
    public final boolean shouldCloseOnEsc() {
        return false;
    }

    public static void drawPlate() {
        BThackRender.drawRect(xLeft, yUp, xRight, yDown, ColorUtils.fastRGBA(0, 0, 0, 150));
        BThackRender.drawVerticalGradientOutlineRect(xLeft, yUp, xRight, yDown, 1.5f, ColorUtils.WHITE, ColorUtils.fastRGBA(150, 150, 150, 255));
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
