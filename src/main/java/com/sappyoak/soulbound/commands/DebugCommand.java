package com.sappyoak.soulbound.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

import com.sappyoak.soulbound.SoulBound;

public class DebugCommand implements CommandInterface {
    private SoulBound plugin;

    public DebugCommand(SoulBound plugin) {
        this.plugin = plugin;
    }

    public List<String> getCommandIds() {
        return Arrays.asList("sb-debug");
    }

    public boolean allowConsoleExecution() {
        return true;
    }

    public boolean execute(CommandSender sender, String[] args) {
        boolean isDebug = plugin.settings.debug();
        
        if (args.length == 0) {
            plugin.settings.debug(!isDebug);
            sender.sendMessage(plugin.messages.applyPrefix(" Debug mode has been set to: " + plugin.settings.debug()));
            return true;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
        
        if (player == null) {
            sender.sendMessage(plugin.messages.applyPrefix("<red>That is not a valid player"));
            return false;
        }

        if (!player.isOnline()) {
            sender.sendMessage(plugin.messages.applyPrefix("<red>That player is offline atm. There is no need to set debug for them"));
            return true;
        }

        Player onlinePlayer = player.getPlayer();        
        if (plugin.debugger.hasPlayerDebug(onlinePlayer)) {
            plugin.debugger.removePlayerDebug(onlinePlayer);
            sender.sendMessage(plugin.messages.applyPrefix("Removing player: " + onlinePlayer.getName() + " from debug list"));
            return true;
        }

        if (!plugin.settings.debug()) {
            sender.sendMessage(plugin.messages.applyPrefix("Attempting to add player to debug list without it being enabled. Flipping debug to enabled now..."));
            plugin.settings.debug(true);
        }

        plugin.debugger.addPlayerDebug(onlinePlayer);
        
        Component messageForTarget = plugin.messages.applyPrefix("You have been added to the debugging list. You'll receive log messages from this plugin");

        if (sender instanceof Player) {
            Player sd = (Player) sender;
            // Avoid duplicate messages when possible
            if (sd.getUniqueId() == onlinePlayer.getUniqueId()) {
                onlinePlayer.sendMessage(messageForTarget);
                return true;
            }
        }

        sender.sendMessage(plugin.messages.applyPrefix("Added player: " + player.getName() + " to debug list")); 
        onlinePlayer.sendMessage(messageForTarget);

        return true;
    }
}
