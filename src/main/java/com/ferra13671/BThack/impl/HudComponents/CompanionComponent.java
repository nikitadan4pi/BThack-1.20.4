package com.ferra13671.BThack.impl.HudComponents;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.Module.HudComponent;
import com.ferra13671.TextureUtils.GLGif;
import net.minecraft.client.MinecraftClient;

import java.util.Arrays;


public class CompanionComponent extends HudComponent {

    public static final GLGif CAIPIRINHA = GLGif.fromInputStream(CompanionComponent.class.getClassLoader().getResourceAsStream("assets/bthack/gifs/caipirinha.gif"), GLGif.DecompileMode.DELTAS, 150);
    public static final GLGif CUTIE1 = GLGif.fromInputStream(CompanionComponent.class.getClassLoader().getResourceAsStream("assets/bthack/gifs/cutie.gif"), GLGif.DecompileMode.DELTAS, 50);
    public static final GLGif CUTIE2 = GLGif.fromInputStream(CompanionComponent.class.getClassLoader().getResourceAsStream("assets/bthack/gifs/cutie2.gif"), GLGif.DecompileMode.DELTAS, 100);
    public static ModeSetting mode;
    public static NumberSetting size;
    public static NumberSetting speed;


    public CompanionComponent() {
        super("Companion",
                MinecraftClient.getInstance().getWindow().getScaledWidth() / 1.8f,
                MinecraftClient.getInstance().getWindow().getScaledHeight() / 1.8f,
                false
        );

        mode = new ModeSetting("Mode", this, Arrays.asList("Caipirinha", "Cutie1", "Cutie2"));
        size = new NumberSetting("Size", this, 40, 20, 100, true);
        speed = new NumberSetting("Speed", this, 1, 0.5, 3, false);

        initSettings(
                mode,
                size,
                speed
        );
    }

    @Override
    public void onChangeSetting(Setting<?> setting) {
        CAIPIRINHA.setUpdateDelayMillis((int) (150 / speed.getValue()));
        CUTIE1.setUpdateDelayMillis((int) (50 / speed.getValue()));
    }

    @Override
    public void render() {
        switch (mode.getValue()) {
            case "Caipirinha" -> {
                CAIPIRINHA.update();
                float w = (float) ((CAIPIRINHA.getWidth() / 560d) * size.getValue());
                float h = (float) ((CAIPIRINHA.getHeight() / 560d) * size.getValue());
                float startX = getX() + ((size.getValue().floatValue() - w) / 2);
                float startY = getY() + ((size.getValue().floatValue() - h) / 2);
                BThackRender.drawTextureRect(
                        CAIPIRINHA,
                        startX,
                        startY,
                        startX + w,
                        startY + h
                );
            }
            case "Cutie1" -> {
                CUTIE1.update();
                BThackRender.drawTextureRect(CUTIE1, getX(), getY(), getX() + size.getValue().floatValue(), getY() + size.getValue().floatValue());
            }
            case "Cutie2" -> {
                CUTIE2.update();
                BThackRender.drawTextureRect(CUTIE2, getX(), getY(), getX() + size.getValue().floatValue(), getY() + size.getValue().floatValue());
            }
        }
    }

    @Override
    public void tick() {
        this.width = size.getValue().floatValue();
        this.height = size.getValue().floatValue();
    }
}
