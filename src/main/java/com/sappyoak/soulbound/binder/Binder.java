package com.sappyoak.soulbound.binder;

import java.util.ArrayList;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.sappyoak.soulbound.SoulBound;
import com.sappyoak.soulbound.config.Permissions;

public final class Binder {
    private final NamespacedKey bindKey;
    private final NamespacedKey groupBindKey;
    private final SoulBound plugin;
    
    public Binder(SoulBound plugin) {
        this.bindKey = new NamespacedKey(plugin, "soulBoundUUID");
        this.groupBindKey = new NamespacedKey(plugin, "SoulBoundGroup");
        this.plugin = plugin;
    }

    public boolean isBound(ItemStack item) {
        return isBoundToPlayer(item) || isBoundToGroup(item);
    }

    public AccessLevel getAccessLevel(ItemStack item, Player player) {
        AccessLevel access = _getAccessLevel(item, player);
        plugin.getDebugger().log("Player " + player.getName() + " has access: " + access.get() + " to item: " + item);
        return access;
    }

    private AccessLevel _getAccessLevel(ItemStack item, Player player) {
        if (isBoundToPlayer(item)) {
            String id = getBoundPlayerId(item);
            if (id.equals(player.getUniqueId().toString())) {
                return AccessLevel.ALLOW;
            } else {
                return AccessLevel.DENY_PLAYER;
            }
        }

        if (isBoundToGroup(item)) {
            String groupPerm = getBoundGroupPerm(item);
            if (player.hasPermission(groupPerm)) {
                return AccessLevel.ALLOW;
            } else {
                return AccessLevel.DENY_GROUP;
            }
        }

        return AccessLevel.ALLOW;
    }

    public boolean canIgnoreBinding(Player player) {
        return player.hasPermission(Permissions.ADMIN) || player.hasPermission(Permissions.BYPASS_RULES);
    }

    public boolean isBoundToPlayer(ItemStack item) {
        return Container.readContainerTag(item, bindKey).isPresent();
    }

    public String getBoundPlayerId(ItemStack item) {
        return Container.readContainerTag(item, bindKey).get();
    }

    public ItemStack bindToPlayer(ItemStack item, Player player) {
        ItemMeta meta = item.getItemMeta();
        ArrayList<String> newLore = new ArrayList<>();

        if (meta.hasLore()) {
            newLore.addAll(meta.getLore());
        }

        newLore.add(plugin.getMessages().getLoreText().replaceAll("%username%", player.getName()));
        meta.setLore(newLore);
        item.setItemMeta(meta);

        return Container.writeContainerTag(item, player.getUniqueId().toString(), bindKey);
    }

    public boolean isBoundToGroup(ItemStack item) {
        return Container.readContainerTag(item, groupBindKey).isPresent();
    }

    public String getBoundGroupPerm(ItemStack item) {
        return Container.readContainerTag(item, groupBindKey).get();
    }

    public ItemStack bindToGroup(ItemStack item, String groupPerm) {
        return Container.writeContainerTag(item, Permissions.GROUP_BIND_ROOT + groupPerm, groupBindKey);
    }

    public ItemStack removeBinds(ItemStack item) {
        item = Container.removeContainerTag(item, bindKey);
        return Container.removeContainerTag(item, groupBindKey);
    }
}
