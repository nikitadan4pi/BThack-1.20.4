package com.nikitadan4pi.BThack.api.Gui.Screen.MainMenu.SelectWallpaper;

import com.nikitadan4pi.BThack.api.GuiSystem.buttons.ButtonWithOffset;

public class WallpaperImageButton extends ButtonWithOffset {
    public final Wallpaper wallpaper;

    public WallpaperImageButton(int id, int x, int y, int width, int height, Wallpaper wallpaper) {
        super(id, x, y, width, height, wallpaper.filename());

        this.wallpaper = wallpaper;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0)
            selected = true;
    }
}
