package com.nikitadan4pi.BThack.impl.Commands;

import com.nikitadan4pi.BThack.api.CommandSystem.command.AbstractCommand;
import com.nikitadan4pi.BThack.api.Utils.ChatUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.entity.Entity;
import net.minecraft.util.Formatting;

public class EntitiesNearbyCommand extends AbstractCommand {

    public EntitiesNearbyCommand() {
        super("lang.command.EntitiesNearby.description", "entitiesNearby", "entitiesNearby"
        );
    }

    @Override
    public void execute(String[] args) {
        int a = 0;
        for (Entity entity : mc.world.getEntities()) {
            if (entity != mc.player)
                a++;
        }
        ChatUtils.sendMessage(Formatting.AQUA + String.format(LanguageSystem.translate("lang.command.EntitiesNearby.message"), a));
    }
}
