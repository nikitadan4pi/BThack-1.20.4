package com.nikitadan4pi.BThack.api.Gui.Screen;

import com.nikitadan4pi.BThack.Core.Client.ModuleList;
import com.nikitadan4pi.BThack.Core.Render.BThackRender;
import com.nikitadan4pi.BThack.Core.Render.Utils.ColorUtils;
import com.nikitadan4pi.BThack.api.Animation.Animation;
import com.nikitadan4pi.BThack.api.Animation.Easing;
import com.nikitadan4pi.BThack.api.GuiSystem.Screen.BThackScreen;
import com.nikitadan4pi.BThack.mixins.accessor.IScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.function.Supplier;

public class TransitionScreen extends BThackScreen {
    public static final Animation STANDARD_FLIP_ANIMATION = new Animation(Easing.LINEAR, 500);

    private Supplier<Screen> newScreen;
    //private final Animation flipAnimation;

    private Supplier<Screen> currentScreen;
    private boolean invert = false;

    public TransitionScreen(Supplier<Screen> oldScreen, Supplier<Screen> newScreen, Animation flipAnimation) {
        super(Text.of("Transition"));
        currentScreen = oldScreen;
        this.newScreen = newScreen;
        //this.flipAnimation = flipAnimation.clone();
        //this.flipAnimation.setMillis((int) (this.flipAnimation.getMillis() / ModuleList.bthackMainMenu.animationSpeed.getValue()));
        //this.flipAnimation.reset();
    }

    public void changeNewScreen(Supplier<Screen> newScreen) {
        if (currentScreen == this.newScreen) {
            currentScreen = newScreen;
            currentScreen.get().init(mc, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight());
            ((IScreen) currentScreen.get())._init();
            currentScreen.get().onDisplayed();
        }
        this.newScreen = newScreen;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    protected void init() {
        super.init();
        ((IScreen) currentScreen.get())._init();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        currentScreen.get().render(context, mouseX, mouseY, partialTicks);

        //double animationDelta = !invert ? flipAnimation.getEase() : 1 - flipAnimation.getEase();

        BThackRender.guiGraphics.getMatrices().push();
        BThackRender.guiGraphics.getMatrices().translate(0, 0, 9000);
        float animationDelta = 0;
        BThackRender.drawRect(0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), ColorUtils.integrateAlpha(ColorUtils.BLACK, (int) (animationDelta * 255)));
        BThackRender.guiGraphics.getMatrices().pop();
    }

    @Override
    public void tick() {
        //if (flipAnimation.getEase() >= 1) {
        //    if (!invert) {
        //        invert = true;
        //        flipAnimation.reset();
        //        currentScreen = newScreen;
        //        currentScreen.get().init(mc, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight());
        //        ((IScreen) currentScreen.get())._init();
        //        currentScreen.get().onDisplayed();
        //    } else mc.setScreen(currentScreen.get());
        //}
    }
}
