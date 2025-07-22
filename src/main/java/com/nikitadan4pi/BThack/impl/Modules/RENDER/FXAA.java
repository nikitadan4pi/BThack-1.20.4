package com.nikitadan4pi.BThack.impl.Modules.RENDER;

import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.minecraft.util.Identifier;

import java.util.Arrays;

public class FXAA extends Module {

    public static ModeSetting level;

    public FXAA() {
        super("FXAA",
                "lang.module.FXAA",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        level = new ModeSetting("Level", this, Arrays.asList("1x", "2x", "3x", "4x", "5x"));

        initSettings(
                level
        );

        ShaderEffectRenderCallback.EVENT.register(tickDelta -> {
            if (this.isEnabled()) {
                int _level = Integer.parseInt(level.getValue().replace("x", ""));
                for (int i = 0; i < _level; i++)
                    fxaa.render(tickDelta);
            }
        });
    }

    ManagedShaderEffect fxaa = ShaderEffectManager.getInstance().manage(new Identifier("shaders/post/fxaa.json"));

}
