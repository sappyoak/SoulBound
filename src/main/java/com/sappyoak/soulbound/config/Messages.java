package com.sappyoak.soulbound.config;

import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.EnumMap;
import java.util.Map;

import com.sappyoak.soulbound.SoulBound;
import com.sappyoak.soulbound.text.TextProvider;

public class Messages {
    private static final Map<Key, Component> messages = new EnumMap<>(Key.class);
    private final SoulBound plugin;
    private String rawLoreText;

    public Messages(SoulBound plugin) {
        this.plugin = plugin;
    }

    public void load() {
        FileConfiguration config = plugin.getConfig();
        for (Key key : Key.values()) {
            if (key == Key.LORE_TEXT) {
                rawLoreText = config.getString(key.get());
            }

            messages.put(key, TextProvider.deserialize(config.getString(key.get())));
        }
    }

    public Component getMessage(Key key) {
        return messages.get(key);
    }

    public Component getLoreText() {
        return getMessage(Key.LORE_TEXT);
    }

    public String getRawLoreText() {
        return rawLoreText;
    }
    
    public Component applyPrefix(Component msg) {
        return getMessage(Key.PREFIX).append(msg);
    }

    public Component applyPrefix(String msg) {
        return getMessage(Key.PREFIX).append(TextProvider.deserialize(msg));
    }

    public Component getDeny() {
        return applyPrefix(getMessage(Key.DENY));
    }

    public Component getDenyGroup() {
        return applyPrefix(getMessage(Key.DENY_GROUP));
    }

    public Component getEmptyMainhand() {
        return applyPrefix(getMessage(Key.EMPTY_MAIN_HAND));
    }

    public Component getAlreadyBound() {
        return applyPrefix(getMessage(Key.ALREADY_BOUND));
    }

    public Component getNotBound() {
        return applyPrefix(getMessage(Key.NOT_BOUND));
    }

    public Component getNoSuchPlayer() {
        return applyPrefix(getMessage(Key.NO_SUCH_PLAYER));
    }

    public Component getBindSuccess() {
        return applyPrefix(getMessage(Key.SUCCESS));
    }

    public Component getUnbindSuccess() {
        return applyPrefix(getMessage(Key.SUCCESS_UNBIND));
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
