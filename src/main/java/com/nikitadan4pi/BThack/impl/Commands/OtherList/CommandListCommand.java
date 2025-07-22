package com.nikitadan4pi.BThack.impl.Commands.OtherList;

import com.nikitadan4pi.BThack.Core.Client.Client;
import com.nikitadan4pi.BThack.api.CommandSystem.command.AbstractCommand;
import com.nikitadan4pi.BThack.api.CommandSystem.command.ICommand;
import com.nikitadan4pi.BThack.api.CommandSystem.CommandManager;
import com.nikitadan4pi.BThack.api.Utils.ChatUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.util.Formatting;

public class CommandListCommand extends AbstractCommand {

    public CommandListCommand() {
        super("lang.command.CommandList.description", "commandlist", "commandlist"
        );
    }

    @Override
    public void execute(String[] args) {
        ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.CommandList.message"));

        for (ICommand command : CommandManager.commands) {
            ChatUtils.sendMessage(Formatting.GRAY + Client.clientInfo.getChatPrefix() + command.getUsage() + Formatting.WHITE + " - " + Formatting.YELLOW + command.getDescription() + ";");
        }
    }
}
