package com.nikitadan4pi.BThack.api.Gui.Widget.Account;

import com.nikitadan4pi.BThack.Constants;
import com.nikitadan4pi.BThack.Core.Render.BThackRender;
import com.nikitadan4pi.BThack.Core.Render.Utils.ColorUtils;
import com.nikitadan4pi.BThack.api.SoundSystem.Sounds;
import com.nikitadan4pi.BThack.api.Utils.Account.Account;
import com.nikitadan4pi.BThack.api.GuiSystem.buttons.Button;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AccountButton extends Button {
    private final Account account;
    private final List<Button> subButtons = new ArrayList<>();
    private final AccountsWidget widget;


    public AccountButton(int id, int x, int y, Account account, AccountsWidget widget) {
        super(id, x, y, 110, 15, "");
        this.account = account;
        this.widget = widget;
        initSubButtons();
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        initSubButtons();
    }

    @Override
    public void setCenterX(int centerX) {
        super.setCenterX(centerX);
        initSubButtons();
    }

    @Override
    public void setCenterY(int centerY) {
        super.setCenterY(centerY);
        initSubButtons();
    }

    @Override
    public void setHovered(boolean hovered) {
        super.setHovered(hovered);
        subButtons.forEach(button -> button.setHovered(false));
    }

    @Override
    public String getText() {
        return account.getUsername();
    }

    @Override
    public void renderButton() {
        float animationDelta = getAnimationDelta();
        drawPlate(animationDelta);
        BThackRender.drawString(getText(), getCenterX() - getWidth() + 24, (getCenterY() - (mc.textRenderer.fontHeight / 2f)), -1, true);
        subButtons.forEach(Button::renderButton);
    }

    @Override
    public void updateButton(int mouseX, int mouseY) {
        subButtons.forEach(button -> {
            button.updateButton(mouseX, mouseY);
        });
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        subButtons.forEach(button -> {
            if (button.isMouseOnButton(mouseX, mouseY))
                button.mouseClicked(mouseX, mouseY, mouseButton);
        });
    }

    private void initSubButtons() {
        subButtons.clear();

        subButtons.add(new SubButton(1, getCenterX() + getWidth() - 20, getCenterY(), 15, 10, "Delete", Color.RED.hashCode()).withAction(
                buttonClickInfo -> widget.actionAfterClicking(() -> widget.deleteAccount(account))
        ));
        subButtons.add(new SubButton(1, getCenterX() + getWidth() - 55, getCenterY(), 15, 10, "Edit", Color.ORANGE.hashCode()).withAction(
                buttonClickInfo -> widget.actionAfterClicking(() -> {
                    widget.close();
                    widget.getParent().widgetManage.addWidget(new AddAccountWidget(account));
                })
        ));
        Button loadButton = new SubButton(3, getCenterX() + getWidth() - 90, getCenterY(), 15, 10, "Load", Color.GREEN.hashCode()).withAction(
                buttonClickInfo -> widget.actionAfterClicking(account::login)
        );
        loadButton.setClickSound(Sounds.CONFIG_SAVED_OR_LOADED);
        subButtons.add(loadButton);
    }

    private static class SubButton extends Button {
        private final int color;

        public SubButton(int id, int x, int y, int width, int height, String text, int color) {
            super(id, x, y, width, height, text);
            this.color = color;
        }

        @Override
        public void renderButton() {
            float animationDelta = getAnimationDelta();
            animationDelta *= 2;
            if (!hovered && hoveredAnimation.getEase() >= 1) {
                BThackRender.drawRoundedRectWithOutline(getCenterX() - getWidth(), getCenterY() - getHeight(), getCenterX() + getWidth(), getCenterY() + getHeight(), 10f, Constants.GUISYSTEM_BUTTON_RECT_COLOR, color, 1);
            } else {
                BThackRender.drawRoundedRectWithOutline(getCenterX() - getWidth() - animationDelta, getCenterY() - getHeight() - animationDelta, getCenterX() + getWidth() + animationDelta, getCenterY() + getHeight() + animationDelta, 10f, Constants.GUISYSTEM_BUTTON_RECT_COLOR, color, 1);

                animationDelta /= 2;
                BThackRender.drawHorizontalGradientRect((int)(getCenterX() - (getWidth() * 0.8 * animationDelta)), getCenterY() + getHeight() - 4, getCenterX(), getCenterY() + getHeight() - 2, ColorUtils.TRANSPARENT, ColorUtils.integrateAlpha(color, (int) (animationDelta * 255)));
                BThackRender.drawHorizontalGradientRect(getCenterX(), getCenterY() + getHeight() - 4, (int)(getCenterX() + (getWidth() * 0.8 * animationDelta)), getCenterY() + getHeight() - 2, ColorUtils.integrateAlpha(color, (int) (animationDelta * 255)), ColorUtils.TRANSPARENT);
            }
            BThackRender.drawString(getText(), (getCenterX() - (mc.textRenderer.getWidth(getText()) / 2f)), (getCenterY() - (mc.textRenderer.fontHeight / 2f)), color, true);
        }

        @Override
        public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
            super.mouseClicked(mouseX, mouseY, mouseButton);
            clickAction(mouseX, mouseY, mouseButton);
        }
    }
}
