package com.sappyoak.soulbound.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Level;

import com.sappyoak.soulbound.SoulBound;
import com.sappyoak.soulbound.config.Messages;
import com.sappyoak.soulbound.config.Permissions;

public class CommandExecutor {
    private final Map<String, CommandInterface> commandMap = new HashMap<>();
    private final SoulBound plugin;

    public CommandExecutor(SoulBound plugin) {
        this.plugin = plugin;
    }

    public void init() {
        registerCommand(new BindCommand(this));
        registerCommand(new UnbindCommand(this));
        registerCommand(new DebugCommand(this));
        registerCommand(new ReloadCommand(this));
    }

    public void registerCommand(CommandInterface command) {
        for (String commandId : command.getCommandIds()) {
            commandMap.put(commandId.toLowerCase(), command);
        }
    }

    public CommandInterface getCommand(String commandId) {
        return commandMap.get(commandId);
    }

    public SoulBound getPlugin() {
        return plugin;
    }

    public Messages getMessages() {
        return plugin.getMessages();
    }

    public boolean execute(CommandSender sender, Command cmd, String[] args) {
        CommandInterface command = getCommand(cmd.getName().toLowerCase());
        if (command == null) {
            return false;
        }

        if (sender instanceof Player) {
            plugin.getLogger().info((Player) sender + " executing command: " + command.getCommandIds().get(0) + " with args: " +  Arrays.asList(args));
        } else if (!command.allowConsoleExecution()) {
            sender.sendMessage(ChatColor.RED + " Only players can execute this command");
            return true;
        }

        if (!sender.hasPermission(Permissions.ADMIN)) {
            sender.sendMessage(ChatColor.RED + "You do not have the permissions to execute that command");
            return false;
        }

        try {
            return command.execute(sender, args);
        } catch (Exception err) {
            err.printStackTrace();
            plugin.getLogger().log(Level.SEVERE, "Ran into error executing command: " + command.getCommandIds().get(0), err);
            return false;
        }
    }
}
