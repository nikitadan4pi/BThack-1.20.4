package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Box.RenderBox;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Events.Render.RenderWorldEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ColorSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.BlockUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.util.math.Box;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class BlockHighlight extends Module {

    public static ColorSetting renderColor;
    public static NumberSetting renderAlpha;
    public static NumberSetting linesAlpha;

    public BlockHighlight() {
        super("BlockHighlight",
                "lang.module.BlockHighlight",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );


        renderColor = new ColorSetting("Color", this, new Color(ColorUtils.fastRGBA(0, 255, 255, 255))).withBlockedAlpha();
        renderAlpha = new NumberSetting("Render Alpha", this, 220, 0, 255, true);
        linesAlpha = new NumberSetting("Lines Alpha", this, 255, 0, 255, true);

        initSettings(
                renderColor,
                renderAlpha,
                linesAlpha
        );
    }

    @EventSubscriber
    public void onBlockOutlineRender(RenderWorldEvent.BlockOutline e) {
        e.setCancelled(true);

        float red = renderColor.getValue().getRed() / 255f;
        float green = renderColor.getValue().getGreen() / 255f;
        float blue = renderColor.getValue().getBlue() / 255f;
        float alpha = renderAlpha.getValue().floatValue() / 255f;
        float lAlpha = linesAlpha.getValue().floatValue() / 255f;

        Box box = BlockUtils.getBoundingBox(e.getBlockOutlineContext().blockPos());
        if (box == null) {
            return;
        }

        BThackRender.boxRender.prepareBoxRender();
        BThackRender.boxRender.renderBoxes(new ArrayList<>(Arrays.asList(new RenderBox(box, red, green, blue, lAlpha, red, green, blue, alpha))));
        BThackRender.boxRender.stopBoxRender();
    }
}
