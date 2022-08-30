package com.sappyoak.soulbound.config;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.EnumMap;
import java.util.Map;

import com.sappyoak.soulbound.SoulBound;

public class Settings {
    private final static Map<Key, Object> settings = new EnumMap<>(Key.class);
    private SoulBound plugin;
    
    public Settings(SoulBound plugin) {
        this.plugin = plugin;
    }

    public void load() {
        FileConfiguration config = plugin.getConfig();
        for (Key key : Key.values()) {
            settings.put(key, config.get(key.get()));
        }
    }

    public boolean isDebug() {
        return (Boolean) settings.get(Key.DEBUG);
    }

    public enum Key {
        DEBUG("debug");

        private String value;

        Key(String value) {
            this.value = value;
        }

        public String get() {
            return this.value;
        }

        @Override
        public String toString() {
            return name() + "(" + value + ")";
        }
    }
}
