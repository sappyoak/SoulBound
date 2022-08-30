package com.sappyoak.soulbound.commands;

import org.bukkit.command.CommandSender;
import java.util.List;

public interface CommandInterface {
    public List<String> getCommandIds();

    public boolean execute(CommandSender sender, String[] args);

    public default boolean allowConsoleExecution() {
        return false;
    }
}
