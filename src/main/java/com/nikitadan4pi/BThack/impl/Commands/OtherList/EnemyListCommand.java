package com.nikitadan4pi.BThack.impl.Commands.OtherList;

import com.nikitadan4pi.BThack.api.CommandSystem.command.AbstractCommand;
import com.nikitadan4pi.BThack.api.Social.SocialManagers;
import com.nikitadan4pi.BThack.api.Utils.ChatUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.util.Formatting;

public class EnemyListCommand extends AbstractCommand {

    public EnemyListCommand() {
        super("lang.command.EnemyList.description", "enemylist", "enemylist"
        );
    }

    @Override
    public void execute(String[] args) {
        ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.EnemyList.message"));

        for (String name : SocialManagers.ENEMIES.getPlayers()) {
            ChatUtils.sendMessage(name);
        }
    }
}
