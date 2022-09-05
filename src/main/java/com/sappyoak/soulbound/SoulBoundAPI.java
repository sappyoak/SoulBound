package com.sappyoak.soulbound;

import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.sappyoak.soulbound.config.Permissions;
import com.sappyoak.soulbound.text.TextProvider;

/**
 * The main API logic for the binding/unbinding of items in the plugin. 
 */

public class SoulBoundAPI {
    private final static String GROUP_STRING = "SoulBoundGroup";
    private final static String PLAYER_STRING = "SoulBoundUUID";
    private final static String LORE_STRING = "<blue>Soul Bound to <placeholder>";
    
    private final NamespacedKey groupKey;
    private final NamespacedKey playerKey;
    private final SoulBound plugin;

    public SoulBoundAPI(SoulBound plugin) {
        this.groupKey = new NamespacedKey(plugin, GROUP_STRING);
        this.playerKey = new NamespacedKey(plugin, PLAYER_STRING);
        this.plugin = plugin;
    }

    /**
     * 
     * @param item         - The item to bind
     * @param targetPlayer - The player the item should be bound to
     * @param invoker      - The player invoking the command
     * @return             - The updated bound item
     */
    public ItemStack bindItemToPlayer(ItemStack item, OfflinePlayer targetPlayer, Player invoker) {
        ItemMeta meta = setBoundItemMeta(
            item,
            playerKey,
            targetPlayer.getName(),
            targetPlayer.getUniqueId().toString()
        );
        item.setItemMeta(meta);
        return item;
    }

    /**
     * 
     * @param item          - The item to be bound
     * @param group         - The Permission label for the group
     * @param holdingPlayer - The player holding the item to be bound
     * @return              - The updated bound item
     */
    public ItemStack bindItemToGroup(ItemStack item, String group, Player holdingPlayer) {
        String groupPerm = Permissions.createGroupPermission(group);
        ItemMeta meta = setBoundItemMeta(item, groupKey, groupPerm, groupPerm);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * This method works for both groupbinds as well as player binds
     * 
     * @param item - The item to remove the bindings from
     * @return     - The item with the binds removed
     */
    public ItemStack removeItemBinding(ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        List<Component> lore = meta.lore();
        for (Component component : lore) {
            String loreLine = TextProvider.serialize(component);
            if (loreLine != null && loreLine.toLowerCase().contains("soul bound")) {
                lore.remove(component);
                break;
            }
        }

        meta.lore(lore);
        meta.getPersistentDataContainer().remove(groupKey);
        meta.getPersistentDataContainer().remove(playerKey);

        item.setItemMeta(meta);
        return item;
    }

    /**
     * This method is called by the listeners when events happen to determine if a
     * player should be able to interact with the item depending on if it is 
     * bound or not
     * 
     * @param item    - The item being accessed
     * @param player  - The player attempting to access the item
     * @return        - {@link SoulBoundAPI.ItemAccess} value with whether the player is allowed or denied 
     */
    public ItemAccess attemptAccess(ItemStack item, Player player) {
        Optional<String> playerTag = getBoundPlayerTag(item);
        if (playerTag.isPresent()) {
            if (playerTag.get().equals(player.getUniqueId().toString())) {
                return ItemAccess.ALLOW;
            }
            return ItemAccess.DENY_PLAYER;
        }

        Optional<String> groupTag = getBoundGroupTag(item);
        if (groupTag.isPresent()) {
            if (player.hasPermission(groupTag.get())) {
                return ItemAccess.ALLOW;
            }
            return ItemAccess.DENY_GROUP;
        }

        return ItemAccess.ALLOW;
    }

    /**
     * 
     * @param item - The item to check
     * @return     - A boolean whether the item has a player-bind or a group-bind
     */
    public boolean isItemSoulBound(ItemStack item) {
        return getBoundPlayerTag(item).isPresent() || getBoundGroupTag(item).isPresent();
    }

    /**
     * 
     * @param item - The item to get tag from
     * @return     - an Optional<String> containing the tag
     */
    public Optional<String> getBoundGroupTag(ItemStack item) {
        return getBoundItemTag(item, groupKey);
    }

    /**
     * 
     * @param item - The item to get tag from
     * @return     - an Optional<String> containing the tag
     */
    public Optional<String> getBoundPlayerTag(ItemStack item) {
        return getBoundItemTag(item, playerKey);
    }


    private ItemMeta setBoundItemMeta(ItemStack item, NamespacedKey key, String loreReplacement, String persistedValue) {
        ItemMeta meta = item.getItemMeta();
        
        if (plugin.settings.attachLoreToItem()) {
            List<Component> updatedLore = new ArrayList<>();

            if (meta.hasLore()) {
                updatedLore.addAll(meta.lore());
            }

            updatedLore.add(TextProvider.deserialize(LORE_STRING, "placeholder", loreReplacement));
            meta.lore(updatedLore);
        }

        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, persistedValue);
        return meta;
    }

    private Optional<String> getBoundItemTag(ItemStack item, NamespacedKey key) {
        if (!item.hasItemMeta()) {
            return Optional.empty();
        }

        ItemMeta meta = item.getItemMeta();
        String tag = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);

        if (tag != null && tag.length() == 0) {
            return Optional.empty();
        }

        return Optional.ofNullable(tag);
    }

    /**
     * An enum containing the different access states available for an item
     */
    public static enum ItemAccess {
        ALLOW("allow", true),
        DENY_GROUP("deny-group", false),
        DENY_PLAYER("deny-player", false);

        private String key;
        private boolean allowed;

        ItemAccess(String key, boolean allowed) {
            this.key = key;
            this.allowed = allowed;
        }

        public String key() {
            return key;
        }

        public boolean allowed() {
            return allowed;
        }

        @Override
        public String toString() {
            return name() + "(" + key + ")";
        }
    }
}
