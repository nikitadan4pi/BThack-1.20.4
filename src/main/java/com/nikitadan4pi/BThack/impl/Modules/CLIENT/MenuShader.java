package com.nikitadan4pi.BThack.impl.Modules.CLIENT;

import com.nikitadan4pi.BTbot.api.Utils.Generate.GenerateNumber;
import com.nikitadan4pi.BThack.api.Managers.Managers;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.Setting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Shader.MainMenu.MainMenuShader;
import com.nikitadan4pi.BThack.api.Shader.MainMenu.MainMenuShaders;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;

import java.util.ArrayList;
import java.util.List;

public class MenuShader extends Module {

    public static BooleanSetting random;
    public static ModeSetting shader;

    public MenuShader() {
        super("MenuShader",
                "lang.module.MenuShader",
                KeyboardUtils.RELEASE,
                MCategory.CLIENT,
                false
        );

        random = new BooleanSetting("Random", this, false);
        List<String> shaderNames = new ArrayList<>();
        MainMenuShaders.getShaders().forEach((name, shader) -> shaderNames.add(name.toLowerCase()));

        shader = new ModeSetting("Sh", this, shaderNames, () -> !random.getValue());


        initSettings(
                random,
                shader
        );

        Managers.MAIN_MENU_SHADER_MANAGER.setPostResetAction(() -> {
            if (this.isEnabled())
                Managers.MAIN_MENU_SHADER_MANAGER.setMainMenuShader(getShader());
        });
    }

    @Override
    public void onEnable() {
        Managers.MAIN_MENU_SHADER_MANAGER.setMainMenuShader(getShader());
    }

    @Override
    public void onChangeSetting(Setting<?> setting) {
        if (this.isEnabled())
            Managers.MAIN_MENU_SHADER_MANAGER.setMainMenuShader(getShader());
    }

    public MainMenuShader getShader() {
        if (!random.getValue()) {
            return MainMenuShaders.getShaders().get(shader.getValue().toUpperCase());
        } else {
            int randomIShader = GenerateNumber.generateInt(0, MainMenuShaders.getShaders().size() - 1);
            return MainMenuShaders.getShaders().get(shader.getOptions().get(randomIShader).toUpperCase());
        }
    }
}
