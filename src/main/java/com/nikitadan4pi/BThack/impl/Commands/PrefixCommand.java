package com.nikitadan4pi.BThack.impl.Commands;

import com.nikitadan4pi.BThack.Core.Client.Client;
import com.nikitadan4pi.BThack.Core.FileSystem.ConfigSystem.ConfigSystem;
import com.nikitadan4pi.BThack.api.CommandSystem.command.AbstractCommand;
import com.nikitadan4pi.BThack.api.Utils.ChatUtils;
import net.minecraft.util.Formatting;

import java.io.IOException;

public class PrefixCommand extends AbstractCommand {

    public PrefixCommand() {
        super("lang.command.Prefix.description", "prefix/p [new_prefix]", "prefix", "p");
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            invalidArgumentError();
            return;
        }

        Client.clientInfo.setChatPrefix(args[0]);

        try {
            ConfigSystem.savePrefix();
            ConfigSystem.loadPrefix();
        } catch (IOException ignored) {}
        ChatUtils.sendMessage(Formatting.AQUA + "Prefix rewritten!");
    }
}
