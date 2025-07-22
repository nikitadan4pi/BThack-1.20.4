package com.nikitadan4pi.BThack.impl.HudComponents.OneTextComponents;

import com.nikitadan4pi.BThack.impl.HudComponents.AbstractOneTextComponent;
import net.minecraft.util.Formatting;

public class FPSComponent extends AbstractOneTextComponent {

    public FPSComponent() {
        super("FPS",
                5,
                47,
                true
        );
    }

    @Override
    public String getText() {
        return "FPS: " + Formatting.WHITE + mc.getCurrentFps();
    }
}
