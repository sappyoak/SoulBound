package com.sappyoak.soulbound;

import org.bukkit.plugin.java.JavaPlugin;

import com.sappyoak.soulbound.config.Messages;
import com.sappyoak.soulbound.config.Settings;

public class SoulBound extends JavaPlugin {
    private Messages messages;
    private Settings settings;

    @Override
    public void onEnable() {
        messages = new Messages(this);
        settings = new Settings(this);
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
}