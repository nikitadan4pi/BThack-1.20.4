package com.nikitadan4pi.BThack.impl.Commands;

import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.nikitadan4pi.BThack.api.CommandSystem.command.AbstractCommand;
import com.nikitadan4pi.BThack.api.Utils.Account.types.CrackedAccount;
import com.nikitadan4pi.BThack.api.Utils.ChatUtils;
import net.minecraft.util.Formatting;


public class AccountCommand extends AbstractCommand {

    public AccountCommand() {
        super("lang.command.Account.description", "account login [nick]", "account"
        );
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            invalidArgumentError();
            return;
        }

        CrackedAccount account = new CrackedAccount(args[1]);
        account.login();
            ChatUtils.sendMessage(Formatting.RED + String.format("Loggined", Formatting.WHITE + args[0] + Formatting.RED));

    }
}
