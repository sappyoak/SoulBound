package com.sappyoak.soulbound;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

import com.sappyoak.soulbound.commands.CommandExecutor;
import com.sappyoak.soulbound.config.Messages;
import com.sappyoak.soulbound.config.Settings;
import com.sappyoak.soulbound.listeners.ItemModifyListener;
import com.sappyoak.soulbound.listeners.ItemProtectionListener;
import com.sappyoak.soulbound.util.MetricsWrapper;

public class SoulBound extends JavaPlugin {
    public SoulBoundAPI api = new SoulBoundAPI(this);
    public CommandExecutor commandExecutor;
    public Debugger debugger;
    public Messages messages;
    public Settings settings;

    private MetricsWrapper metrics;
    private UpdateChecker updater;

    public void onDisable() {
        debugger.disable();
        settings.close();
    }
    
    @Override
    public void onEnable() {
        commandExecutor = new CommandExecutor(this);
        debugger = new Debugger(this);
        messages = new Messages(this);
        settings = new Settings(this);
        metrics = new MetricsWrapper(this, 16304);
        updater = new UpdateChecker(this);

        initializeConfiguration();
        initializeModules();
        setupListeners();
        
        getServer().getScheduler().runTaskAsynchronously(this, () -> {
            updater.check();
        });
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        metrics.markCommand(cmd.getName(), true);
        return commandExecutor.execute(sender, cmd, args);
    }

    public void initializeConfiguration() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        reloadConfig();

        settings.load();
        messages.load();
    }

    public void initializeModules() {
        debugger.init();
        commandExecutor.init();
    }

    public void setupListeners() {
        getServer().getPluginManager().registerEvents(new ItemProtectionListener(this), this);
        getServer().getPluginManager().registerEvents(new ItemModifyListener(this), this);
    }

    public void publishEvent(Event event) {
        getServer().getPluginManager().callEvent(event);
    }
}