package com.nikitadan4pi.BThack.Core.FileSystem;

import com.nikitadan4pi.BThack.BThack;
import com.nikitadan4pi.BThack.Core.FileSystem.ConfigSystem.ConfigUtils;
import com.google.gson.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class FileSystem {
    private static final Gson gson = (new GsonBuilder()).setPrettyPrinting().create();

    public static void start() throws IOException {

        registerFolder("HudComponents", "");
        registerFolder("Social", "");
        registerFolder("Friends", "/Social");
        registerFolder("Enemies", "/Social");
        registerFolder("Spammer", "");
        registerFolder("Modules", "");
        registerFolder("ActionBot", "");
        registerFolder("DefaultConfig", "/ActionBot");
        registerFolder("Wallpapers", "");
        registerFolder("Configs", "");
        registerFile("AutoAuthPasswords", "", "json");
        registerFile("VersionInfo", "", "json");
        registerFile("Prefix", "", "txt");
        registerFile("Friends", "Social/Friends", "txt");
        registerFile("Enemies", "Social/Enemies", "txt");
        registerFile("Spammer", "Spammer", "txt");
        registerFile("ConfigInfo", "ActionBot", "txt");
        registerFile("WallpaperInfo", "Wallpapers", "txt");
        registerFile("CurrentConfig", "Modules", "txt");
        registerFile("Frames", "", "json");
    }

    public static void registerFile(String name, String path, String typeFile) throws IOException {
        Path path1 = Paths.get(Paths.get("BThack/" + path + "/" + name + "." + typeFile).toUri());
        if (!Files.exists(path1)) {
            Files.createFile(path1);
            BThack.log(name + "." + typeFile + " file was created successfully");
            if (typeFile.equals("json")) {
                BufferedWriter writer = Files.newBufferedWriter(path1, StandardCharsets.UTF_8);
                writer.write("{}");
                writer.close();
            }
        } else {
            BThack.log(name + "." + typeFile + " file already exists");
        }

    }

    public static void registerFolder(String name, String path) throws IOException {
        Path path1 = Paths.get(Paths.get("BThack" + path + "/" + name).toUri());
        if (!Files.exists(path1)) {
            Files.createDirectories(path1);
            BThack.log(name + " folder created successfully");
        } else {
            BThack.log(name + " folder already exists");
        }
    }

    public static void deleteDirectory(File directory) {
        File[] contents = directory.listFiles();
        if (contents != null) {
            for (File file : contents) {
                deleteDirectory(file);
            }
        }
        directory.delete();
    }
}
