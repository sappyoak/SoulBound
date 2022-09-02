package com.sappyoak.soulbound.config;

import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.EnumMap;
import java.util.Map;

import com.sappyoak.soulbound.SoulBound;
import com.sappyoak.soulbound.text.TextProvider;

public final class Messages {
    private static final Map<Key, Component> messages = new EnumMap<>(Key.class);
    private final SoulBound plugin;

    public Messages(SoulBound plugin) {
        this.plugin = plugin;
    }

    public void load() {
        FileConfiguration config = plugin.getConfig();
        for (Key key : Key.values()) {
            messages.put(key, TextProvider.deserialize(config.getString(key.get())));
        }
    }

    public Component getMessage(Key key) {
        return messages.get(key);
    }

    public Component applyPrefix(Component msg) {
        return getMessage(Key.PREFIX).append(msg);
    }

    public Component applyPrefix(String msg) {
        return getMessage(Key.PREFIX).append(TextProvider.deserialize(msg));
    }

    public Component denyAnvil() {
        return applyPrefix(getMessage(Key.DENY_ANVIL));
    }

    public Component denyCraft() {
        return applyPrefix(getMessage(Key.DENY_CRAFT));
    }

    public Component denyEnchant() {
        return applyPrefix(getMessage(Key.DENY_ENCHANT));
    }
    
    public Component denyGroup() {
        return applyPrefix(getMessage(Key.DENY_GROUP));
    }

    public Component denyPlayer() {
        return applyPrefix(getMessage(Key.DENY_PLAYER));
    }

    public Component emptyMainhand() {
        return applyPrefix(getMessage(Key.EMPTY_MAIN_HAND));
    }

    public Component alreadyBound() {
        return applyPrefix(getMessage(Key.ALREADY_BOUND));
    }

    public Component notBound() {
        return applyPrefix(getMessage(Key.NOT_BOUND));
    }

    public Component noSuchPlayer() {
        return applyPrefix(getMessage(Key.NO_SUCH_PLAYER));
    }

    public Component bindSuccess() {
        return applyPrefix(getMessage(Key.SUCCESS));
    }

    public Component unbindSuccess() {
        return applyPrefix(getMessage(Key.SUCCESS_UNBIND));
    }
    
    public static enum Key {
        PREFIX("messages.prefix"),
        DENY_ANVIL("messages.denyAnvil"),
        DENY_CRAFT("messages.denyCraft"),
        DENY_ENCHANT("messages.denyEnchant"),
        DENY_GROUP("messages.denyGroup"),
        DENY_PLAYER("messages.denyPlayer"),
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
