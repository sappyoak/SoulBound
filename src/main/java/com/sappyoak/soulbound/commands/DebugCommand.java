package com.sappyoak.soulbound.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

import com.sappyoak.soulbound.Debugger;

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
        boolean isDebug = executor.getPlugin().getSettings().isDebug();
        
        if (args.length == 0) {
            sender.sendMessage(executor.getPlugin().getMessages().applyPrefix(" Debug mode has been set to: " + !isDebug));
            executor.getPlugin().getSettings().setDebug(!isDebug);
            return true;
        }

        Player player = Bukkit.getPlayer(args[0]);
        
        if (player == null) {
            sender.sendMessage(executor.getPlugin().getMessages().applyPrefix(ChatColor.RED + " That is not a valid player"));
            return false;
        }

        Debugger debugger = executor.getPlugin().getDebugger();
        
        if (debugger.hasPlayerDebug(player)) {
            debugger.removePlayerDebug(player);
            sender.sendMessage(executor.getPlugin().getMessages().applyPrefix("Removing player: " + player.getName() + " from debug list"));
            return true;
        }

        debugger.addPlayerDebug(player);
        sender.sendMessage(executor.getPlugin().getMessages().applyPrefix("Added player: " + player.getName() + " to debug list"));
        return true;
    }
}
