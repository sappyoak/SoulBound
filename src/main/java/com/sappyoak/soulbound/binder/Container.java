package com.sappyoak.soulbound.binder;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;

public final class Container {
    public static ItemStack writeContainerTag(ItemStack item, String data, NamespacedKey key) {
        ItemMeta meta = item.getItemMeta();
        item.setItemMeta(writeContainerTag(meta, data, key));
        return item;
    }

    public static ItemMeta writeContainerTag(ItemMeta meta, String data, NamespacedKey key) {
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, data);
        return meta;
    }

    public static Optional<String> readContainerTag(ItemStack item, NamespacedKey key) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        String tag = container.get(key, PersistentDataType.STRING);

        if (tag != null && tag.length() == 0) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(tag);
        }
    }

    public static ItemStack removeContainerTag(ItemStack item, NamespacedKey key) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().remove(key);
        item.setItemMeta(meta);
        return item;
    }
}
