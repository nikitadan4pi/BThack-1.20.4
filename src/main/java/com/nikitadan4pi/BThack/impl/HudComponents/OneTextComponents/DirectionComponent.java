package com.nikitadan4pi.BThack.impl.HudComponents.OneTextComponents;

import com.nikitadan4pi.BThack.api.Utils.Modules.AimBotUtils;
import com.nikitadan4pi.BThack.impl.HudComponents.AbstractOneTextComponent;
import net.minecraft.util.Formatting;

public class DirectionComponent extends AbstractOneTextComponent {

    public DirectionComponent() {
        super("Direction",
                5,
                85,
                true
        );
    }

    @Override
    public String getText() {
        return "Direction: " + Formatting.WHITE + AimBotUtils.getDirection(mc.player);
    }
}
