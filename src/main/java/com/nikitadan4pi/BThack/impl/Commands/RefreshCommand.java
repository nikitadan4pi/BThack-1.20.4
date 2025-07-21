package com.nikitadan4pi.BThack.impl.Commands;

import com.nikitadan4pi.BThack.api.CommandSystem.command.AbstractCommand;
import com.nikitadan4pi.BThack.api.Social.Clans.ClansUtils;
import com.nikitadan4pi.BThack.api.Social.SocialManagers;
import com.nikitadan4pi.BThack.api.Utils.ChatUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.util.Formatting;

public class RefreshCommand extends AbstractCommand {

    public RefreshCommand() {
        super("lang.command.Refresh.description", "refresh [friends/enemies/clans]", "refresh"
        );
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0 || !args[0].equals("friends") && !args[0].equals("enemies") && !args[0].equals("clans")) {
            invalidArgumentError();
        } else {
            switch (args[0]) {
                case "friends":
                    SocialManagers.FRIENDS.load();
                    ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Refresh.friendRefreshed"));
                    break;
                case "enemies":
                    SocialManagers.ENEMIES.load();
                    ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Refresh.enemyRefreshed"));
                    break;
                case "clans":
                    ClansUtils.reloadClans();
                    ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Refresh.clanRefreshed"));
                    break;
            }
        }
    }
}
