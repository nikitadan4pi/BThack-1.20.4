package com.nikitadan4pi.BThack.Core.FileSystem.ConfigSystem;

import com.nikitadan4pi.BThack.BThack;
import com.nikitadan4pi.BThack.Core.Client.Client;
import com.nikitadan4pi.BThack.Core.FileSystem.FileSystem;
import com.nikitadan4pi.BThack.api.Category.Categories;
import com.nikitadan4pi.BThack.api.Gui.ClickGui.component.Frame;
import com.nikitadan4pi.BThack.api.Gui.MainMenu.BThackMainMenuScreen;
import com.nikitadan4pi.BThack.api.Gui.MainMenu.SelectWallpaper.SelectWallpaperScreen;
import com.nikitadan4pi.BThack.api.Gui.MainMenu.SelectWallpaper.Wallpaper;
import com.nikitadan4pi.BThack.api.HudComponent.HudComponent;
import com.nikitadan4pi.BThack.api.Managers.Managers;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.Setting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Plugin.Plugin;
import com.nikitadan4pi.BThack.api.Plugin.PluginSystem;
import com.nikitadan4pi.BThack.api.Social.Clans.Ally;
import com.nikitadan4pi.BThack.api.Social.Clans.Clan;
import com.nikitadan4pi.BThack.api.Social.Clans.ClansUtils;
import com.nikitadan4pi.BThack.api.Utils.List.BlockList.BlockLists;
import com.nikitadan4pi.BThack.api.Utils.List.ItemList.ItemLists;
import com.nikitadan4pi.BThack.impl.Modules.MISC.AutoAuth;
import com.nikitadan4pi.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotConfig;
import com.nikitadan4pi.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTask;
import com.nikitadan4pi.BThack.impl.Modules.PLAYER.ActionBot.Config.Utils.ActionBotTaskData;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.ferra13671.TextureUtils.GLTexture;
import com.ferra13671.TextureUtils.PathMode;
import com.google.gson.*;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.nikitadan4pi.BThack.Core.FileSystem.ConfigSystem.ConfigUtils.*;
import static com.nikitadan4pi.BThack.Core.FileSystem.JsonUtils.*;

public final class ConfigSystem {
    static final Gson gson = (new GsonBuilder()).setPrettyPrinting().create();

    public static void saveConfig() {
        try {
            saveModules();
            saveFrames();
            saveClans();
            BlockLists.forEach(blockList -> {
                try {
                    blockList.saveInFile();
                } catch (IOException e) {
                    BThack.error(e.getMessage());
                }
            });
            ItemLists.forEach(itemList -> {
                try {
                    itemList.saveInFile();
                } catch (IOException e) {
                    BThack.error(e.getMessage());
                }
            });
            saveActionBotTasks();
            savePrefix();
            saveAutoAuthPasswords();
        } catch (IOException e) {
            BThack.error(e.getMessage());
        }
    }

    public static void loadConfig() {
        try {
            loadModules();
            loadClans();
            BlockLists.forEach(blockList -> {
                try {
                    blockList.loadFromFile();
                } catch (IOException e) {
                    BThack.error(e.getMessage());
                }
            });
            ItemLists.forEach(itemList -> {
                try {
                    itemList.loadFromFile();
                } catch (IOException e) {
                    BThack.error(e.getMessage());
                }
            });
            loadWallpaper();
            loadFrames();
            loadActionBotTasks();
            loadPrefix();
            loadAutoAuthPasswords();
        } catch (IOException e) {
            BThack.error(e.getMessage());
        }
        try {
            ConfigUtils.loadFromTxt("CurrentConfig", "Modules", Client.clientInfo::setCurrentConfigName);
        } catch (IOException ignored) {}
    }

    public static void saveModules() throws IOException {
        for (Module module : Client.getAllModules()) {
            ConfigUtils.saveInJson(module.getName(), "Modules", jsonObject -> {
                JsonObject settingObject = new JsonObject();


                add(jsonObject, "Name", module.getName());
                add(jsonObject, "Enabled", module.isEnabled());
                add(jsonObject, "Bind", module.getKey());
                add(jsonObject, "Visible", module.visible);

                if (Managers.SETTINGS_MANAGER.getSettingsByMod(module) != null) {
                    for (Setting<?> s : Managers.SETTINGS_MANAGER.getSettingsByMod(module)) {
                        s.save(settingObject);
                    }
                }
                add(jsonObject, "Settings", settingObject);
            });
        }
    }

    public static void loadModules() throws IOException {
        for (Module module : Client.getAllModules()) {
            ConfigUtils.loadFromJson(module.getName(), "Modules", jsonObject -> {
                if (equalsNull(jsonObject, "Name", "Enabled", "Bind", "Visible")) return;

                JsonObject settingObject = jsonObject.get("Settings").getAsJsonObject();

                if (Managers.SETTINGS_MANAGER.getSettingsByMod(module) != null) {
                    for (Setting<?> s : Managers.SETTINGS_MANAGER.getSettingsByMod(module)) {
                        JsonElement settingValueObject;

                        settingValueObject = settingObject.get(s.getName());

                        if (settingValueObject != null) {
                            s.load(settingObject, settingValueObject);
                        }
                    }
                }
                module.setToggled(jsonObject.get("Enabled").getAsBoolean());
                module.setKey(jsonObject.get("Bind").getAsInt());
                module.visible = jsonObject.get("Visible").getAsBoolean();
            }, () -> {
                if (module.isAutoEnabled()) {
                    module.setToggled(true);
                }
            });
        }
    }

    public static List<String> getAllConfigs() {
        List<String> results = new ArrayList<>();
        File folder = Paths.get("BThack/Configs").toFile();
        File[] files = folder.listFiles();
        if (files == null) return results;

        for (File file : files) {
            if (Objects.equals(FilenameUtils.getExtension(file.getName()), "json")) {
                results.add(file.getName().replace(".json", ""));
            }
        }

        return results;
    }

    public static void saveConfigFile(String fileName) throws IOException {
        ConfigUtils.saveInJson(fileName, "Configs", jsonObject -> {
            for (Module module : Client.getAllModules()) {
                JsonObject moduleObject = new JsonObject();
                JsonObject settingObject = new JsonObject();

                add(moduleObject, "Name", module.getName());
                add(moduleObject, "Enabled", module.isEnabled());
                add(moduleObject, "Bind", module.getKey());
                add(moduleObject, "Visible", module.visible);

                if (Managers.SETTINGS_MANAGER.getSettingsByMod(module) != null) {
                    for (Setting<?> s : Managers.SETTINGS_MANAGER.getSettingsByMod(module)) {
                        s.save(settingObject);
                    }
                }
                add(moduleObject, "Settings", settingObject);

                add(jsonObject, module.getName(), moduleObject);
            }
        });
    }

    public static void loadConfigFile(String fileName) throws IOException {
        ConfigUtils.loadFromJson(fileName, "Configs",
                jsonObject -> {
                    try {
                        saveConfigFile(Client.clientInfo.getCurrentConfigName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    for (Module module : Client.getAllModules()) {
                        if (jsonObject.get(module.getName()) != null) {
                            JsonObject moduleObject = jsonObject.get(module.getName()).getAsJsonObject();
                            if (equalsNull(moduleObject, "Name", "Enabled", "Bind", "Visible")) continue;

                            JsonObject settingObject = moduleObject.get("Settings").getAsJsonObject();

                            if (Managers.SETTINGS_MANAGER.getSettingsByMod(module) != null) {
                                for (Setting<?> s : Managers.SETTINGS_MANAGER.getSettingsByMod(module)) {
                                    JsonElement settingValueObject;

                                    settingValueObject = settingObject.get(s.getName());

                                    if (settingValueObject != null) {
                                        s.load(settingObject, settingValueObject);
                                    }
                                }
                            }

                            module.setQuietlyToggled(moduleObject.get("Enabled").getAsBoolean());
                            module.setKey(moduleObject.get("Bind").getAsInt());
                            module.visible = moduleObject.get("Visible").getAsBoolean();
                        }
                    }

                    try {
                        ConfigUtils.saveInTxt("CurrentConfig", "Modules", writer -> {
                            try {
                                writer.write(fileName);
                            } catch (IOException ignored) {}
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                },
                () -> {}
        );
    }

    public static void saveFrames() throws IOException {
        ConfigUtils.saveInJson("Frames", "", jsonObject -> {
            for (Frame frame : Frame.getGlobalFrames()) {
                JsonObject frameObject = new JsonObject();

                add(frameObject, "x", frame.getX());
                add(frameObject, "y", frame.getY());
                add(frameObject, "opened", frame.isOpen());

                add(jsonObject, frame.getFrameName(), frameObject);
            }
        });
    }

    public static void loadFrames() throws IOException {
        ConfigUtils.loadFromJson("Frames", "", jsonObject -> {
            for (Frame frame : Frame.getGlobalFrames()) {
                JsonElement jsonElement = jsonObject.get(frame.getFrameName());
                if (jsonElement == null) return;
                JsonObject settingObject = jsonElement.getAsJsonObject();
                if (settingObject == null) return;

                if (equalsNull(settingObject, "x", "y", "opened")) return;

                frame.setX(settingObject.get("x").getAsInt());
                frame.setY(settingObject.get("y").getAsInt());
                frame.setOpen(settingObject.get("opened").getAsBoolean());
            }
        }, () -> {});
    }

    public static void saveHudComponents() throws IOException {
        for (Module module : Client.getModulesInCategory(Categories.HUD)) {
            HudComponent hudComponent = (HudComponent) module;
            ConfigUtils.saveInJson(hudComponent.getName(), "HudComponents", jsonObject -> {
                JsonObject settingObject = new JsonObject();

                add(jsonObject, "Name", hudComponent.getName());
                add(jsonObject, "X", hudComponent.getNoScaledX());
                add(jsonObject, "Y", hudComponent.getNoScaledY());
                add(jsonObject, "ScaledWidth", hudComponent.getScaledWidth());
                add(jsonObject, "ScaledHeight", hudComponent.getScaledHeight());
                add(jsonObject, "Enabled", hudComponent.isEnabled());

                if (Managers.SETTINGS_MANAGER.getSettingsByMod(module) != null) {
                    for (Setting<?> s : Managers.SETTINGS_MANAGER.getSettingsByMod(module)) {
                        s.save(settingObject);
                    }
                }
                add(jsonObject, "Settings", settingObject);
            });
        }
    }

    public static void loadHudComponents() throws IOException {
        File folder = Paths.get("BThack/HudComponents").toFile();
        File[] files = folder.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (Objects.equals(FilenameUtils.getExtension(file.getName()), "json")) {
                ConfigUtils.loadFromJson(file.getName().replace(".json", ""), "HudComponents", jsonObject -> {
                    if (equalsNull(jsonObject, "Name", "X", "Y", "ScaledWidth", "ScaledHeight")) return;

                    String name = jsonObject.get("Name").getAsString();
                    for (Module module : Client.getModulesInCategory(Categories.HUD)) {
                        HudComponent hudComponent = (HudComponent) module;
                        if (hudComponent.getName().equals(name)) {
                            hudComponent.setX(jsonObject.get("X").getAsFloat(), jsonObject.get("ScaledWidth").getAsInt());
                            hudComponent.setY(jsonObject.get("Y").getAsFloat(), jsonObject.get("ScaledHeight").getAsInt());
                            if (jsonObject.get("Enabled") != null) {
                                hudComponent.setToggled(jsonObject.get("Enabled").getAsBoolean());
                            }

                            if (!_null(jsonObject, "Settings")) {
                                JsonObject settingObject = jsonObject.get("Settings").getAsJsonObject();

                                if (Managers.SETTINGS_MANAGER.getSettingsByMod(module) != null) {
                                    for (Setting<?> s : Managers.SETTINGS_MANAGER.getSettingsByMod(module)) {
                                        JsonElement settingValueObject;

                                        settingValueObject = settingObject.get(s.getName());

                                        if (settingValueObject != null) {
                                            s.load(settingObject, settingValueObject);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }, () -> {});
            }
        }

        loadOldHudInfo();
    }

    //TODO:
    /** Will need to remove this in the next versions */
    @Deprecated
    private static void loadOldHudInfo() throws IOException {
        ConfigUtils.loadFromJson("HUD", "Modules", jsonObject -> {
            for (Module module : Client.getModulesInCategory(Categories.HUD)) {
                HudComponent hudComponent = (HudComponent) module;
                JsonObject settingsObject = jsonObject.get("Settings").getAsJsonObject();
                if (!_null(settingsObject, hudComponent.getName()))
                    hudComponent.setToggled(settingsObject.get(hudComponent.getName()).getAsBoolean());
            }
        }, () -> {});
    }

    public static void saveClans() throws IOException {
        FileSystem.deleteDirectory(new File("BThack/Social/Clans"));
        FileSystem.registerFolder("Clans", "/Social");
        for (Clan clan : ClansUtils.clans) {
            ConfigUtils.saveInJson(clan.getName(), "Social/Clans", clansObject -> {
                add(clansObject, "ClanName", clan.getName());
                add(clansObject, "R", clan.getR());
                add(clansObject, "G", clan.getG());
                add(clansObject, "B", clan.getB());
            });

            FileSystem.registerFolder(clan.getName() + "_Members", "/Social/Clans");

            for (Ally ally : clan.members) {
                ConfigUtils.saveInJson(ally.name() + "_Member", "Social/Clans/" + clan.getName() + "_Members", allyObject -> {
                    add(allyObject, "name", ally.name());
                });
            }
        }

        ClansUtils.reloadClanListOnModules();
    }

    public static void loadClans() throws IOException {
        ClansUtils.clans.clear();
        File folder = new File(Paths.get("BThack/Social/Clans").toUri());
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    if (Objects.equals(FilenameUtils.getExtension(file.getName()), "json")) {
                        String name = file.getName();
                        InputStream inputStream = Files.newInputStream(Paths.get("BThack/Social/Clans/" + name));
                        JsonObject clanOject = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();

                        if (!equalsNull(clanOject, "ClanName", "R", "G", "B")) {
                            String clanName = clanOject.get("ClanName").getAsString();
                            float r = clanOject.get("R").getAsFloat();
                            float g = clanOject.get("G").getAsFloat();
                            float b = clanOject.get("B").getAsFloat();

                            Clan clan = new Clan(clanName, r, g, b);

                            File clanMembersFolder = new File(Paths.get("BThack/Social/Clans/" + clan.getName() + "_Members").toUri());
                            File[] clanMembers = clanMembersFolder.listFiles();

                            if (clanMembers != null) {
                                for (File memberFile : clanMembers) {
                                    if (memberFile.isFile()) {
                                        if (Objects.equals(FilenameUtils.getExtension(memberFile.getName()), "json")) {
                                            String memberName = memberFile.getName();
                                            InputStream memberInputStream = Files.newInputStream(Paths.get("BThack/Social/Clans/" + clan.getName() + "_Members/" + memberName));
                                            JsonObject memberObject = JsonParser.parseReader(new InputStreamReader(memberInputStream)).getAsJsonObject();

                                            if (memberObject.get("name") != null) {
                                                String member = memberObject.get("name").getAsString();
                                                clan.members.add(new Ally(member));
                                            }

                                            memberInputStream.close();
                                        }
                                    }
                                }
                            }

                            ClansUtils.clans.add(clan);
                        }
                        inputStream.close();
                    }
                }
            }
        }

        ClansUtils.reloadClanListOnModules();
    }


    public static void saveActionBotTasks() throws IOException {
        Path configInfoFile = Paths.get("BThack/ActionBot/ConfigInfo.txt");

        BufferedWriter writer = Files.newBufferedWriter(configInfoFile, StandardCharsets.UTF_8);
        ArrayList<ActionBotTask> tasks = new ArrayList<>(ActionBotConfig.tasks);
        tasks.remove(ActionBotConfig.startTask);
        tasks.remove(ActionBotConfig.endTask);

        int taskNumber = 1;

        boolean m = false;

        for (ActionBotTask task : tasks) {
            String fileName = taskNumber + ". " + task.getName();

            registerFiles(fileName, "ActionBot/DefaultConfig");

            OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(Files.newOutputStream(Paths.get("BThack/ActionBot/DefaultConfig/" + fileName + ".json")), StandardCharsets.UTF_8);

            JsonObject taskObject = new JsonObject();


            add(taskObject, "Mode", task.mode);
            task.save(taskObject);


            if (!m) {
                writer.write(fileName);
                m = true;
            } else {
                writer.write(System.lineSeparator() + fileName);
            }

            taskNumber++;

            String jsonString = gson.toJson(JsonParser.parseString(taskObject.toString()));
            fileOutputStreamWriter.write(jsonString);
            fileOutputStreamWriter.close();
        }

        writer.close();
    }

    public static void loadActionBotTasks() throws IOException {
        Path configInfoFile = Paths.get("BThack/ActionBot/ConfigInfo.txt");

        ActionBotConfig.tasks.add(ActionBotConfig.startTask);
        if (Files.exists(configInfoFile)) {

            BufferedReader readerConfigInfo = Files.newBufferedReader(configInfoFile, StandardCharsets.UTF_8);
            String line = readerConfigInfo.readLine();

            ArrayList<String> tasksNames = new ArrayList<>();

            while (line != null) {
                tasksNames.add(line);
                line = readerConfigInfo.readLine();
            }

            readerConfigInfo.close();

            for (String taskName : tasksNames) {
                Path taskPath = Paths.get("BThack/ActionBot/DefaultConfig/" + taskName + ".json");

                if (Files.exists(taskPath)) {
                    InputStream inputStream = Files.newInputStream(taskPath);

                    JsonObject taskObject = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();

                    if (!_null(taskObject, "Mode")) {
                        for (ActionBotTaskData data : ActionBotConfig.getFullActionBotTasks()) {
                            if (data.getTask().mode.equals(taskObject.get("Mode").getAsString())) {
                                data.getTask().load(taskObject);
                            }
                        }
                    }
                }
            }
        }
        ActionBotConfig.tasks.add(ActionBotConfig.endTask);
    }

    public static void saveAutoAuthPasswords() throws IOException {
        ConfigUtils.saveInJson("AutoAuthPasswords", "", jsonObject -> {
            JsonArray jsonElements = new JsonArray();
            AutoAuth.passwords.forEach((playerName, password) -> {
                JsonObject info = new JsonObject();
                add(info, playerName, password);
                jsonElements.add(info);
            });
            add(jsonObject, "info", jsonElements);
        });
    }

    public static void loadAutoAuthPasswords() throws IOException {
        ConfigUtils.loadFromJson("AutoAuthPasswords", "", jsonObject -> {
            if (_null(jsonObject, "info")) return;
            for (JsonElement jsonElement : jsonObject.get("info").getAsJsonArray().asList()) {
                jsonElement.getAsJsonObject().asMap().forEach((playerName, password) -> AutoAuth.passwords.put(playerName, password.getAsString()));
            }
        }, () -> {});
    }


    public static void refreshWallpapers() {
        for (Wallpaper wallpaper : SelectWallpaperScreen.wallpapers) {
            if (wallpaper.texture().getTexId() != Client.clientInfo.getDefaultMainMenuImage().getTexId())
                wallpaper.texture().delete();
        }
        SelectWallpaperScreen.wallpapers.clear();

        File imagesFolder = Paths.get("BThack/Wallpapers").toFile();
        File[] files = imagesFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    if (imageFormats.contains(FilenameUtils.getExtension(file.getName()))) {
                        SelectWallpaperScreen.wallpapers.add(new Wallpaper(file.getName(), GLTexture.fromPath("BThack/Wallpapers/" + file.getName(), PathMode.OUTSIDEJAR, GLTexture.ColorMode.RGBA)));
                    }
                }
            }
        }
    }

    public static void saveWallpaper(String fileName) throws IOException {
        Path infoPath = Paths.get("BThack/Wallpapers/WallpaperInfo.txt");
        if (Files.exists(infoPath)) {
            BufferedWriter writer = Files.newBufferedWriter(infoPath);
            writer.write(fileName);
            writer.close();
        }
    }

    public static void loadWallpaper() throws IOException {
        Path infoPath = Paths.get("BThack/Wallpapers/WallpaperInfo.txt");
        if (Files.exists(infoPath)) {
            BufferedReader reader = Files.newBufferedReader(infoPath, StandardCharsets.UTF_8);
            String line = reader.readLine();
            if (line != null) {
                if (!line.equals("default") && Files.exists(Paths.get("BThack/Wallpapers/" + line))) {
                    BThackMainMenuScreen.mainMenuTexture = GLTexture.fromPath("BThack/Wallpapers/" + line, PathMode.OUTSIDEJAR, GLTexture.ColorMode.RGBA);
                }
            }
            reader.close();
        }
    }

    public static void loadPrefix() throws IOException {
        ConfigUtils.loadFromTxt("Prefix", "", Client.clientInfo::setChatPrefix);
    }

    public static void savePrefix() throws IOException {
        ConfigUtils.saveInTxt("Prefix", "", writer -> {
            try {
                writer.write(Client.clientInfo.getChatPrefix());
            } catch (IOException ignored) {}
        });
    }

    private static final Set<String> imageFormats = new HashSet<>(Arrays.asList(
            "png", "jpg", "gif"
    ));

    public static void loadLanguages() {
        LanguageSystem.loadTranslations(ConfigUtils.newInputStream("assets/bthack/langs/EN.lng", PathMode.INSIDEJAR), "EN");
        LanguageSystem.loadTranslations(ConfigUtils.newInputStream("assets/bthack/langs/RU.lng", PathMode.INSIDEJAR), "RU");
        LanguageSystem.loadTranslations(ConfigUtils.newInputStream("assets/bthack/langs/PL.lng", PathMode.INSIDEJAR), "PL");

        PluginSystem.getLoadedPlugins().forEach(Plugin::onLoadLanguages);
    }
}
