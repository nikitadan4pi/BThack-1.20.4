package com.nikitadan4pi.BThack.impl.Commands;

import com.nikitadan4pi.BThack.Core.Client.Client;
import com.nikitadan4pi.BThack.api.CommandSystem.command.AbstractCommand;
import com.nikitadan4pi.BThack.api.Managers.Managers;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.Setting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.ChatUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.util.Formatting;

public class ResetModuleCommand extends AbstractCommand {

    public ResetModuleCommand() {
        super("lang.command.ResetModule.description", "resetModule [module_name]", "resetModule"
        );
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            invalidArgumentError();
            return;
        }
        Module module = Client.getModuleByName(args[0]);
        if (module == null) {
            ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.ResetModule.moduleNotFound"));
            return;
        }
        for (Setting<?> setting : Managers.SETTINGS_MANAGER.getSettingsByMod(module)) {
            setting.toDefault();
        }
        ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.ResetModule.successfulResetting"));
    }
}
