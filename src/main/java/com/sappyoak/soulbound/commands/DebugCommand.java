package com.sappyoak.soulbound.commands;

import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;


public class DebugCommand implements CommandInterface {
    private CommandExecutor executor;

    public DebugCommand(CommandExecutor executor) {
        this.executor = executor;
    }

    public List<String> getCommandIds() {
        return Arrays.asList("sb-debug");
    }

    public boolean allowConsoleExecution() {
        return true;
    }

    public boolean execute(CommandSender sender, String[] args) {
        executor.getPlugin().getSettings().toggleDebug();
        sender.sendMessage(executor.getPlugin().getMessages().applyPrefix(" Debug mode as been set to: " + executor.getPlugin().getSettings().isDebug()));
        return true;
    }
}
