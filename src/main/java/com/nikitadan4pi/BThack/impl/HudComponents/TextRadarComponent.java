package com.nikitadan4pi.BThack.impl.HudComponents;

import com.nikitadan4pi.BThack.Constants;
import com.nikitadan4pi.BThack.Core.Render.BThackRender;
import com.nikitadan4pi.BThack.api.HudComponent.HudComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;

public class TextRadarComponent extends HudComponent {

    public TextRadarComponent() {
        super("TextRadar",
                250,
                5,
                true
        );
    }

    @Override
    public void render() {
        float y = 0;
        float maxWidth = 0;
        int count = 0;

        if (width > 0 && height > 0)
            BThackRender.drawHudPlate(getX(), getY(), getX() + width, getY() + height);

        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player.getDisplayName().getString().equals(mc.player.getDisplayName().getString())) continue;
            String text = player.getDisplayName().getString() + " " + Formatting.GRAY + "[" + Formatting.WHITE + Constants.DECIMAL_FORMAT.format(player.distanceTo(mc.player)) + "m." + Formatting.GRAY + "]";

            BThackRender.drawString(text, (int) getX() + 3, (int) (getY() + y + 3), ArrayListComponent.INSTANCE.getArrayColor(count), true);


            float textWidth = mc.textRenderer.getWidth(text);
            if (textWidth > maxWidth) {
                maxWidth = textWidth;
            }
            y += mc.textRenderer.fontHeight + 5;
            count++;
        }

        width = maxWidth > 0 ? (maxWidth + 6) : 0;
        height = y;
    }
}
