package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Box.RenderBox;
import com.ferra13671.BThack.Core.Render.Line.RenderLine;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Events.Render.RenderWorldEvent;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ColorSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
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
