package com.nikitadan4pi.BThack.impl.Commands.ItemList;

import com.nikitadan4pi.BThack.api.CommandSystem.command.AbstractCommand;
import com.nikitadan4pi.BThack.api.Utils.ChatUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.util.Formatting;

public abstract class AbstractItemListCommand extends AbstractCommand {

    private final String listName;

    public AbstractItemListCommand(String listName , String alias) {
        super("", alias, alias);

        this.listName = listName;
    }

    @Override
    public void execute(String[] args) {
        ChatUtils.sendMessage(Formatting.AQUA + String.format(LanguageSystem.translate("lang.command.ItemList.message"), listName));

        sendAllList();
    }

    @Override
    public String getDescription() {
        return String.format(LanguageSystem.translate("lang.command.ItemList.description"), listName);
    }

    public abstract void sendAllList();
}
