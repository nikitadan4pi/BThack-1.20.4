package com.nikitadan4pi.BThack.Core.Client;

import com.nikitadan4pi.BThack.BThack;
import com.nikitadan4pi.BThack.Core.Client.Systems.FirstLaunchWelcomer;
import com.nikitadan4pi.BThack.Core.Client.Systems.KeyHandler;
import com.nikitadan4pi.BThack.api.CommandSystem.CommandManager;
import com.nikitadan4pi.BThack.api.Interfaces.Mc;
import com.nikitadan4pi.BThack.api.Managers.managers.Build.BuildManager;
import com.nikitadan4pi.BThack.api.Managers.managers.EntityDeathManager;
import com.nikitadan4pi.BThack.api.Managers.Managers;
import com.nikitadan4pi.BThack.api.Plugin.Plugin;
import com.nikitadan4pi.BThack.api.Plugin.PluginSystem;
import com.nikitadan4pi.BThack.api.Plugin.PluginUtils;
import com.nikitadan4pi.BThack.api.Utils.Grim.GrimFreezeUtils;
import com.nikitadan4pi.BThack.api.Utils.List.BlockList.BlockLists;
import com.nikitadan4pi.BThack.api.Utils.List.ItemList.ItemLists;
import com.nikitadan4pi.BThack.impl.Commands.*;
import com.nikitadan4pi.BThack.impl.Commands.OtherList.ClansListCommand;
import com.nikitadan4pi.BThack.impl.Commands.OtherList.CommandListCommand;
import com.nikitadan4pi.BThack.impl.Commands.OtherList.EnemyListCommand;
import com.nikitadan4pi.BThack.impl.Commands.OtherList.FriendListCommand;
import com.nikitadan4pi.BThack.impl.Commands.Social.Clans.ClanMembersCommand;
import com.nikitadan4pi.BThack.impl.Commands.Social.Clans.ClanStatusCommand;
import com.nikitadan4pi.BThack.impl.Commands.Social.Clans.ClansCommand;
import com.nikitadan4pi.BThack.impl.Commands.Social.EnemiesCommand;
import com.nikitadan4pi.BThack.impl.Commands.Social.FriendsCommand;
import com.nikitadan4pi.BThack.impl.Modules.CLIENT.ClientSetting;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.ferra13671.TextureUtils.Controller.DefaultGlController;
import com.ferra13671.TextureUtils.GLTextureSystem;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;

public final class InitializeHelper implements Mc {
    private static boolean hasInitedLibraries = false;

    static void initLibraries() {
        if (hasInitedLibraries) return;

        LanguageSystem.setCurrentLanguageGetter(() -> ClientSetting.language.getValue());
        GLTextureSystem.setGlController(new DefaultGlController() {
            @Override
            public void run(Runnable runnable) {
                RenderSystem.recordRenderCall(runnable::run);
            }

            @Override
            public int genTexId() {
                return TextureUtil.generateTextureId();
            }

            @Override
            public void bindTexture(int id) {
                GlStateManager._bindTexture(id);
            }

            @Override
            public void deleteTexture(int id) {
                GlStateManager._deleteTexture(id);
            }
        });

        hasInitedLibraries = true;
    }

    static void initCommands() {
        PluginSystem.getLoadedPlugins().forEach(Plugin::onInitCommands);

        CommandManager.addCommands(
                new FriendsCommand(),
                new EnemiesCommand(),
                new CleanMemoryCommand(),


                new ClansCommand(),
                new ClanMembersCommand(),
                new ClanStatusCommand(),


                new PrefixCommand(),
                new RefreshCommand(),
                new RotateCommand(),
                new BindCommand(),
                new BreakCommand(),
                new BuildCommand(),
                new SoundReloadCommand(),
                new EntitiesNearbyCommand(),
                new ConfigCommand(),
                new ResetModuleCommand(),
                new DisableAllCommand(),
                new ClientGamemodeCommand(),
                new AutoAuthCommand(),
                new AccountCommand(),

                new HClipCommand(),
                new VClipCommand()
        );
        BlockLists.forEach(blockList -> CommandManager.addCommands(
                blockList.editBlockListCommand,
                blockList.blockListCommand
        ));
        ItemLists.forEach(itemList -> CommandManager.addCommands(
                itemList.editBlockListCommand,
                itemList.blockListCommand
        ));
        CommandManager.addCommands(
                new CommandListCommand(),
                new FriendListCommand(),
                new EnemyListCommand(),
                new ClansListCommand()
        );
        CommandManager.commands.addAll(PluginUtils.getPluginsCommands());
    }

    static void initManagers() {
        BThack.EVENT_BUS.register(Managers.TPS_MANAGER);
        BThack.EVENT_BUS.register(Managers.FIREWORK_MANAGER);
        BThack.EVENT_BUS.register(Managers.DESTROY_MANAGER);
        BThack.EVENT_BUS.register(Managers.MAIN_MENU_SHADER_MANAGER);
        BThack.EVENT_BUS.register(Managers.TOTEM_POP_MANAGER);
        BThack.EVENT_BUS.register(Managers.TRAVEL_CHANGE_MANAGER);

        BThack.EVENT_BUS.register(new GrimFreezeUtils());
        BThack.EVENT_BUS.register(new EntityDeathManager());
        BThack.EVENT_BUS.register(new BuildManager());
    }

    static void initCustomCategories() {
        PluginSystem.getLoadedPlugins().forEach(Plugin::onInitCustomCategories);
    }

    static void initSystems() {
        BThack.EVENT_BUS.register(new KeyHandler());
        if (BThack.instance.versionInfo.isFirstLaunched()) BThack.EVENT_BUS.register(new FirstLaunchWelcomer());
    }
}
