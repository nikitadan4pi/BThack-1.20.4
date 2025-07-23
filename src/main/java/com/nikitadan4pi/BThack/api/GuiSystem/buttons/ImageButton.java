package com.nikitadan4pi.BThack.api.GuiSystem.buttons;

import com.ferra13671.TextureUtils.GLTexture;
import com.nikitadan4pi.BThack.Core.Render.BThackRender;

import static com.nikitadan4pi.BThack.api.GuiSystem.ScreenWidget.drawPlate;

public class ImageButton extends Button {
    private final GLTexture texture;

    public ImageButton(int id, int x, int y, int width, int height, GLTexture texture) {
        super(id, x, y, width, height, "");
        this.texture = texture;
    }

    @Override
    public void renderButton() {
        drawPlate();
        BThackRender.drawTextureRect(texture, getCenterX() - getWidth() + 2, getCenterY() - getHeight() + 2, getCenterX() + getWidth() - 2, getCenterY() + getHeight() - 2);
    }
}
