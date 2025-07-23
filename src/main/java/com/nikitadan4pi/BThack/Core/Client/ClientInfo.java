package com.nikitadan4pi.BThack.Core.Client;

import com.nikitadan4pi.BThack.BThack;
import com.nikitadan4pi.BThack.api.Interfaces.Mc;
import com.ferra13671.TextureUtils.GLTexture;
import com.ferra13671.TextureUtils.PathMode;

public final class ClientInfo implements Mc {
    private String name = "BThack " + BThack.instance.VERSION + " | " + mc.getSession().getUsername();
    private final String cName = "BThack " + BThack.instance.VERSION;
    private final GLTexture defaultMainMenuImage = GLTexture.fromPath("assets/bthack/bthack_mainmenu.jpg", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA, false);
    private String chatPrefix = "$";
    private String font = "default";
    private String currentConfigName = "";

    ClientInfo() {}

    public void updateName() {
        name = "BThack " + BThack.instance.VERSION + " | " + mc.getSession().getUsername();
    }

    public String getName() {
        return name;
    }

    public String getCName() {
        return cName;
    }

    public String getFont() {
        return font;
    }

    public GLTexture getDefaultMainMenuImage() {
        return defaultMainMenuImage;
    }

    public void setChatPrefix(String chatPrefix) {
        this.chatPrefix = chatPrefix;
    }

    public String getChatPrefix() {
        return chatPrefix;
    }

    public void setCurrentConfigName(String currentConfigName) {
        this.currentConfigName = currentConfigName;
    }

    public String getCurrentConfigName() {
        return currentConfigName;
    }
}
