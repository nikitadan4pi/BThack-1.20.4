package com.nikitadan4pi.BThack.impl.HudComponents.OneTextComponents;

import com.nikitadan4pi.BThack.impl.HudComponents.AbstractOneTextComponent;
import net.minecraft.util.Formatting;

public class WelcomerComponent extends AbstractOneTextComponent {

    public WelcomerComponent() {
        super("Welcomerw",
                6,
                10,
                true
        );
    }

    @Override
    public String getText() {
        return "Welcome " + Formatting.WHITE + mc.getSession().getUsername() + " :)";
    }
}