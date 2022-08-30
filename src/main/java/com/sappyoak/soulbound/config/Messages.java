package com.sappyoak.soulbound.config;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.EnumMap;
import java.util.Map;

import com.sappyoak.soulbound.SoulBound;

public class Messages {
    private static final Map<Key, String> messages = new EnumMap<>(Key.class);
    private final SoulBound plugin;

    public Messages(SoulBound plugin) {
        this.plugin = plugin;
    }

    public void load() {
        FileConfiguration config = plugin.getConfig();
        for (Key key : Key.values()) {
            messages.put(key, colorMessage(config.getString(key.get())));
        }
    }

    public String getMessage(Key key) {
        return messages.get(key);
    }

    public String getLoreText() {
        return getMessage(Key.LORE_TEXT);
    }

    public String applyPrefix(String msg) {
        return getMessage(Key.PREFIX) + msg;
    }

    public String getDeny() {
        return applyPrefix(getMessage(Key.DENY));
    }

    public String getDenyGroup() {
        return applyPrefix(getMessage(Key.DENY_GROUP));
    }

    public String getEmptyMainhand() {
        return applyPrefix(getMessage(Key.EMPTY_MAIN_HAND));
    }

    public String getAlreadyBound() {
        return applyPrefix(getMessage(Key.ALREADY_BOUND));
    }

    public String getNotBound() {
        return applyPrefix(getMessage(Key.NOT_BOUND));
    }

    public String getNoSuchPlayer() {
        return applyPrefix(getMessage(Key.NO_SUCH_PLAYER));
    }

    public String getBindSuccess() {
        return applyPrefix(getMessage(Key.SUCCESS));
    }

    public String getUnbindSuccess() {
        return applyPrefix(getMessage(Key.SUCCESS_UNBIND));
    }
    
    public String colorMessage(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static enum Key {
        LORE_TEXT("loreText"),
        PREFIX("messages.prefix"),
        DENY("messages.deny"),
        DENY_GROUP("messages.denyGroup"),
        EMPTY_MAIN_HAND("messages.errorEmptyMainHand"),
        ALREADY_BOUND("messages.errorAlreadyBound"),
        NOT_BOUND("messages.errorNotBound"),
        NO_SUCH_PLAYER("messages.errorNoSuchPlayer"),
        SUCCESS("messages.success"),
        SUCCESS_UNBIND("messages.successUnbind");

        private String value;

        Key(String value) {
            this.value = value;
        }

        public String get() {
            return value;
        }

        @Override
        public String toString() {
            return name() + "(" + value + ")";
        }
    }    
}
