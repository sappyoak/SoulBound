package com.sappyoak.soulbound.config;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.EnumMap;
import java.util.Map;

import com.sappyoak.soulbound.SoulBound;

public class Settings {
    private final static Map<Key, Object> settings = new EnumMap<>(Key.class);
    private SoulBound plugin;
    
    public boolean dirty = false;

    public Settings(SoulBound plugin) {
        this.plugin = plugin;
    }

    public void load() {
        FileConfiguration config = plugin.getConfig();
        for (Key key : Key.values()) {
            settings.put(key, config.get(key.get()));
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Key key) {
        return (T) settings.get(key);
    }
    
    public boolean attachLoreToItem() {
        return get(Key.ATTACH_LORE_TO_ITEM);
    }

    public boolean blockAnvil() {
        return get(Key.BLOCK_ANVIL);
    }

    public boolean blockCraft() {
        return get(Key.BLOCK_CRAFT);
    }

    public boolean blockEnchant() {
        return get(Key.BLOCK_ENCHANT);
    }
    
    public boolean keepOnDeath() {
        return get(Key.KEEP_ON_DEATH);
    }
    
    public boolean debug() {
        return get(Key.DEBUG);
    }

    public void debug(boolean value) {
        settings.put(Key.DEBUG, value);
        plugin.getConfig().set(Key.DEBUG.get(), value);
        dirty = true;
    }

    public enum Key {
        ATTACH_LORE_TO_ITEM("attachLoreToItem"),
        BLOCK_ANVIL("blockAnvil"),
        BLOCK_CRAFT("blockCraft"),
        BLOCK_ENCHANT("blockEnchant"),
        KEEP_ON_DEATH("keepOnDeath"),
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
