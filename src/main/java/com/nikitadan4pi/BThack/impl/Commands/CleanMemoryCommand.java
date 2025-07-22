package com.nikitadan4pi.BThack.impl.Commands;

import com.nikitadan4pi.BThack.api.CommandSystem.command.AbstractCommand;
import com.nikitadan4pi.BThack.api.Managers.Managers;

public class CleanMemoryCommand extends AbstractCommand {

    public CleanMemoryCommand() {
        super("lang.command.CleanMemory.description", "cleanmemory", "cleanmemory"
        );
    }

    @Override
    public void execute(String[] args) {
        Managers.MEMORY_MANAGER.cleanMemory();
    }
}
