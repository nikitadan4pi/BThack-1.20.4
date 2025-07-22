package com.nikitadan4pi.BThack.api.Module;

import com.nikitadan4pi.BThack.api.Category.Category;
import com.nikitadan4pi.BThack.api.Plugin.Plugin;

public class PluginModule extends Module {

    public final Plugin plugin;

    public PluginModule(String name, String description, int key, MCategory c, boolean autoEnabled, Plugin plugin) {
        this(name, description, key, c.category, autoEnabled, plugin);
    }

    public PluginModule(String name, String descriptionEN, int key, Category c, boolean autoEnabled, Plugin plugin) {
        super(name, descriptionEN, key, c, autoEnabled);

        this.plugin = plugin;
    }
}
