package com.nikitadan4pi.BThack.impl.HudComponents.OneTextComponents;

import com.nikitadan4pi.BThack.api.Utils.SpeedMathThread;
import com.nikitadan4pi.BThack.impl.HudComponents.AbstractOneTextComponent;
import net.minecraft.util.Formatting;

public class SpeedComponent extends AbstractOneTextComponent {

    public SpeedComponent() {
        super("Speed",
                5,
                105,
                true
        );
    }

    @Override
    public String getText() {
        return "Speed: " + Formatting.WHITE + decimal.format(SpeedMathThread.speed) + "b/s";
    }
}
