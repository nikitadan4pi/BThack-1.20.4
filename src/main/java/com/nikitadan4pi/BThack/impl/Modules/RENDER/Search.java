package com.nikitadan4pi.BThack.impl.Modules.RENDER;

import com.nikitadan4pi.BThack.Core.Render.BThackRender;
import com.nikitadan4pi.BThack.Core.Render.Box.RenderBox;
import com.nikitadan4pi.BThack.Core.Render.Line.RenderLine;
import com.nikitadan4pi.BThack.api.Events.Render.RenderWorldEvent;
import com.nikitadan4pi.BThack.api.Managers.Managers;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.ColorSetting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.awt.*;
import java.util.ArrayList;

public class Search extends Module {

    public static ColorSetting searchColor;
    public static BooleanSetting tracers;

    public Search() {
        super("Search",
                "lang.module.Search",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        searchColor = new ColorSetting("Search Color", this, Color.WHITE).withBlockedAlpha();

        tracers = new BooleanSetting("Tracers", this, false);

        initSettings(
                searchColor,
                tracers
        );
    }

    @Override
    public void onEnable() {
        super.onEnable();
        Managers.BLOCK_SEARCH_MANAGER.start();
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onRender(RenderWorldEvent.Last e) {

        ArrayList<RenderBox> boxes = new ArrayList<>();
        ArrayList<RenderLine> lines = new ArrayList<>();

        float red = (searchColor.getValue().getRed()) / 255f;
        float green = (searchColor.getValue().getGreen()) / 255f;
        float blue = (searchColor.getValue().getBlue()) / 255f;

        for (BlockPos pos : Managers.BLOCK_SEARCH_MANAGER.getResults()) {
            Box box = new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);

            boxes.add(new RenderBox(box, red, green, blue, 0.6f, red, green, blue, 0.4f));
            lines.add(new RenderLine(box.getCenter(), red, green, blue, 1f));
        }

        BThackRender.boxRender.prepareBoxRender();
        BThackRender.boxRender.renderBoxes(boxes);
        BThackRender.boxRender.stopBoxRender();

        if (tracers.getValue()) {
            BThackRender.lineRender.prepareLineRenderer();
            BThackRender.lineRender.renderLines(lines);
            BThackRender.lineRender.stopLineRenderer();
        }
    }
}
