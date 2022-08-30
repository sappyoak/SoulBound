package com.sappyoak.soulbound;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.sappyoak.soulbound.binder.Binder;
import com.sappyoak.soulbound.commands.CommandExecutor;
import com.sappyoak.soulbound.config.Messages;
import com.sappyoak.soulbound.config.Settings;
import com.sappyoak.soulbound.listeners.ItemProtectionListener;

public class SoulBound extends JavaPlugin {
    private Binder binder;
    private CommandExecutor commandExecutor;
    private Messages messages;
    private Settings settings;

    @Override
    public void onEnable() {
        binder = new Binder(this);
        commandExecutor = new CommandExecutor(this);
        messages = new Messages(this);
        settings = new Settings(this);

        initializeConfiguration();
        setupListeners();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return commandExecutor.execute(sender, cmd, args);
    }

    public Binder getBinder() {
        return binder;
    }

    public Messages getMessages() {
        return messages;
    }

    public Settings getSettings() {
        return settings;
    }

    public void initializeConfiguration() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        reloadConfig();

        settings.load();
        messages.load();
    }

    public void setupListeners() {
        getServer().getPluginManager().registerEvents(new ItemProtectionListener(this), this);
    }
}