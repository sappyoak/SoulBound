package com.sappyoak.soulbound.commands;

import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class ReloadCommand implements CommandInterface {
    private CommandExecutor executor;

    public ReloadCommand(CommandExecutor executor) {
        this.executor = executor;
    }

    public List<String> getCommandIds() {
        return Arrays.asList("sb-reload");
    }

    public boolean allowConsoleExecution() {
        return true;
    }

    public boolean execute(CommandSender sender, String[] args) {
        executor.getPlugin().initializeConfiguration();
        sender.sendMessage(executor.getPlugin().getMessages().applyPrefix("Reloaded configuration"));
        return true;
    }
}
