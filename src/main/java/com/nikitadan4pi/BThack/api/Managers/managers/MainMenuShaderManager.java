package com.nikitadan4pi.BThack.api.Managers.managers;

import com.nikitadan4pi.BThack.api.Events.DisconnectEvent;
import com.nikitadan4pi.BThack.api.Interfaces.Mc;
import com.nikitadan4pi.BThack.api.Shader.MainMenu.MainMenuShader;
import com.nikitadan4pi.BThack.api.Utils.Ticker;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

public class MainMenuShaderManager implements Mc {
    private final Ticker shaderTicker = new Ticker();
    private MainMenuShader shader;
    private Runnable postResetAction;

    public MainMenuShaderManager() {
        shaderTicker.reset();
    }

    @EventSubscriber
    public void onDisconnect(DisconnectEvent e) {
        if (mc.player == null || mc.world == null) return;
        resetShaderTime();
    }

    public void setMainMenuShader(MainMenuShader shader) {
        if (shader == null) return;
        if (this.shader != shader) {
            this.shader = shader;
            resetShaderTime();
        }
    }

    public void setPostResetAction(Runnable runnable) {
        postResetAction = runnable;
    }

    public MainMenuShader getMainMenuShader() {
        return shader;
    }

    public void resetShaderTime() {
        shaderTicker.reset();
        if (postResetAction != null) postResetAction.run();
    }

    public float getShaderTime() {
        return getShaderTime(1);
    }

    public float getShaderTime(float speed) {
        return (shaderTicker.getPassedTime() / 1000) * speed;
    }
}
