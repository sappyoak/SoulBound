package com.sappyoak.soulbound.commands;

import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

import com.sappyoak.soulbound.SoulBound;

public class ReloadCommand implements CommandInterface {
    private SoulBound plugin;

    public ReloadCommand(SoulBound plugin) {
        this.plugin = plugin;
    }

    public List<String> getCommandIds() {
        return Arrays.asList("sb-reload");
    }

    public boolean allowConsoleExecution() {
        return true;
    }

    public boolean execute(CommandSender sender, String[] args) {
        plugin.initializeConfiguration();
        sender.sendMessage(plugin.messages.applyPrefix("<green>Reloaded configuration"));
        return true;
    }
}
