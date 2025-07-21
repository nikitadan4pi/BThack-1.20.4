package com.nikitadan4pi.BThack.impl.HudComponents.OneTextComponents;

import com.nikitadan4pi.BThack.Core.Client.Client;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.nikitadan4pi.BThack.impl.HudComponents.AbstractOneTextComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Formatting;

import java.util.Arrays;

public class RealTimeComponent extends AbstractOneTextComponent {
    private final ModeSetting mode;

    public RealTimeComponent() {
        super("RealTime",
                MinecraftClient.getInstance().getWindow().getScaledWidth() / 2f,
                10,
                true
        );

        mode = new ModeSetting("Hour Mode", this, Arrays.asList("24", "12"));

        initSettings(
                mode
        );
    }

    @Override
    public String getText() {
        return "Real Time " + Formatting.WHITE + Client.getRealTime(mode.getValue());
    }
}
