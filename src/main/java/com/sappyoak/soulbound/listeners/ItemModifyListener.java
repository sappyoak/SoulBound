package com.sappyoak.soulbound.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import com.sappyoak.soulbound.SoulBound;
import com.sappyoak.soulbound.config.Permissions;

public class ItemModifyListener implements Listener {
    private SoulBound plugin;

    public ItemModifyListener(SoulBound plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAnvilClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!plugin.settings.blockAnvil() || player.hasPermission(Permissions.BYPASS_ANVIL)) {
            if (player.hasPermission(Permissions.BYPASS_ANVIL)) {
                plugin.debugger.log(() -> "Player: " + player.getName() + " is bypassing the blockAnvil setting with permission.");
            }

            return;
        }

        if (isAnvilOutputSlotClick(event)) {
            ItemStack[] anvil = event.getClickedInventory().getContents();
            for (ItemStack item : anvil) {
                if (plugin.api.isItemSoulBound(item)) {
                    player.sendMessage(plugin.messages.denyAnvil());
                    // play block sound
                    event.setCancelled(true);
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!plugin.settings.blockCraft() || player.hasPermission(Permissions.BYPASS_CRAFT)) {
            if (player.hasPermission(Permissions.BYPASS_CRAFT)) {
                plugin.debugger.log(() -> "Player: " + player.getName() + " is bypassing the blockCraft setting with permission");
            }
            return;
        }

        ItemStack[] matrix = event.getInventory().getMatrix();
        for (ItemStack item : matrix) {
            if (item != null && plugin.api.isItemSoulBound(item)) {
                player.sendMessage(plugin.messages.denyCraft());
                // player sound
                event.setCancelled(true);
                break;
            }
        }
    }

    @EventHandler
    public void onEnchant(EnchantItemEvent event) {
        Player player = (Player) event.getInventory().getViewers().get(0);
        if (plugin.settings.blockEnchant() || player.hasPermission(Permissions.BYPASS_ENCHANT)) {
            if (player.hasPermission(Permissions.BYPASS_ENCHANT)) {
                plugin.debugger.log(() -> "Player: " + player.getName() + " is bypassing the blockEnchant setting with permission"); 
            }
            return;
        }

        if (plugin.api.isItemSoulBound(event.getItem())) {
            player.sendMessage(plugin.messages.denyEnchant());
            event.setCancelled(true);
        }
    }

    private boolean isAnvilOutputSlotClick(InventoryClickEvent event) {
        return event.getClickedInventory() != null && 
            event.getClickedInventory().getType().equals(InventoryType.ANVIL) &&
            event.getSlot() == 2;
    }
}
