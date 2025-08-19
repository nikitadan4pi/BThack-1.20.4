package com.nikitadan4pi.BThack.impl.HudComponents;

import com.ferra13671.TextureUtils.GLGif;
import com.nikitadan4pi.BThack.BThack;
import com.nikitadan4pi.BThack.Core.FileSystem.ConfigSystem.ConfigUtils;
import com.nikitadan4pi.BThack.Core.FileSystem.FileSystem;
import com.nikitadan4pi.BThack.Core.Render.BThackRender;
import com.nikitadan4pi.BThack.api.HudComponent.HudComponent;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.ModeSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.Setting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.GlAllocationUtils;
import org.apache.commons.io.FilenameUtils;
import org.lwjgl.opengl.GL;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class CompanionComponent extends HudComponent {


    private final ModeSetting mode;
    private final NumberSetting size;
    private final NumberSetting speed;
    private static boolean inited = true;
    public static List<GLGif> gifs = Arrays.asList(GLGif.fromInputStream(CompanionComponent.class.getClassLoader().getResourceAsStream("assets/bthack/gifs/caipirinha.gif"), GLGif.DecompileMode.DELTAS, 150), GLGif.fromInputStream(CompanionComponent.class.getClassLoader().getResourceAsStream("assets/bthack/gifs/cutie.gif"), GLGif.DecompileMode.DELTAS, 50), GLGif.fromInputStream(CompanionComponent.class.getClassLoader().getResourceAsStream("assets/bthack/gifs/cutie2.gif"), GLGif.DecompileMode.DELTAS, 100));
    public static List<String> names = Arrays.asList("Caipirinha", "Cutie1", "Cutie2");

    public static void init() {
        if (inited) return;
        File folder = Paths.get("BThack/customGifs").toFile();

        File[] files = folder.listFiles();
        if (files == null) return;
        BThack.log("try to find gifs");
        names.add("velikiy_sup");
        for (File file : files) {
            if (Objects.equals(FilenameUtils.getExtension(file.getName()), "gif")) {
                gifs.add(GLGif.fromInputStream(CompanionComponent.class.getClassLoader().getResourceAsStream(file.getAbsolutePath() + file.getName()), GLGif.DecompileMode.DELTAS, 100));
                names.add(file.getName().replace(".gif", ""));
                BThack.initLog(file.getName());
            }
        }

        inited = true;

    }

    public CompanionComponent(){
        super("Companion",
                MinecraftClient.getInstance().getWindow().getScaledWidth() / 1.8f,
                MinecraftClient.getInstance().getWindow().getScaledHeight() / 1.8f,
                false
        );
        mode = new ModeSetting("Mode", this, names);
        size = new NumberSetting("Size", this, 40, 20, 100, true);
        speed = new NumberSetting("Speed", this, 1, 0.5, 3, false);

        initSettings(
                mode,
                size,
                speed
        );
    }

    @Override
    public void render(){
        switch (mode.getValue()) {
            case "Caipirinha" -> {
                gifs.get(0).update();
                float w = (float) ((gifs.get(0).getWidth() / 560d) * size.getValue());
                float h = (float) ((gifs.get(0).getHeight() / 560d) * size.getValue());
                float startX = getX() + ((size.getValue().floatValue() - w) / 2);
                float startY = getY() + ((size.getValue().floatValue() - h) / 2);
                BThackRender.drawTextureRect(
                        gifs.get(0),
                        startX,
                        startY,
                        startX + w,
                        startY + h
                );
            }
            case "Cutie1" -> {
                gifs.get(1).update();
                BThackRender.drawTextureRect(gifs.get(1), getX(), getY(), getX() + size.getValue().floatValue(), getY() + size.getValue().floatValue());
            }
            case "Cutie2" -> {
                gifs.get(2).update();
                BThackRender.drawTextureRect(gifs.get(2), getX(), getY(), getX() + size.getValue().floatValue(), getY() + size.getValue().floatValue());
            }
        }
        if (mode.getIndex() > 2){
            gifs.get(mode.getIndex()).update();
            BThackRender.drawTextureRect(gifs.get(mode.getIndex()), getX(), getY(), getX() + size.getValue().floatValue(), getY() + size.getValue().floatValue());
        }
    }
    @Override
    public void onChangeSetting(Setting<?> setting) {
        gifs.get(0).setUpdateDelayMillis((int) (150 / speed.getValue()));
        gifs.get(1).setUpdateDelayMillis((int) (50 / speed.getValue()));
    }

    @Override
    public void tick() {
        this.width = size.getValue().floatValue();
        this.height = size.getValue().floatValue();
    }
}
