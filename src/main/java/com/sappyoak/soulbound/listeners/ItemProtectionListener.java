package com.sappyoak.soulbound.listeners;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

import com.sappyoak.soulbound.SoulBound;
import com.sappyoak.soulbound.binder.AccessLevel;
import com.sappyoak.soulbound.binder.Binder;

public class ItemProtectionListener implements Listener {
    private final Map<UUID, Map<Integer, ItemStack>> deathItems = new HashMap<>();
    private final SoulBound plugin;
    
    public ItemProtectionListener(SoulBound plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            Binder binder = plugin.getBinder();

           if (binder.canIgnoreBinding(player)) {
                return;
            } 

            plugin.getDebugger().log("InventoryHolder: " + event.getClickedInventory().getHolder());

            if (
                event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta() &&
                event.getClickedInventory() != null && event.getClickedInventory().getHolder() != null &&
                !event.getClickedInventory().getHolder().equals(player)
            ) {
                ItemStack item = event.getCurrentItem();
                AccessLevel access = binder.getAccessLevel(item, player);
                if (access != AccessLevel.ALLOW) {
                    event.setCancelled(true);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1F, 1F);
                    player.sendMessage(access == AccessLevel.DENY_GROUP ? plugin.getMessages().getDenyGroup() : plugin.getMessages().getDeny());
                }
            }
        }
    }

    @EventHandler
    public void onPlayerPickupAttempt(PlayerAttemptPickupItemEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem().getItemStack();
        
        if (plugin.getBinder().canIgnoreBinding(player)) {
            return;
        }

        if (plugin.getBinder().getAccessLevel(item, player) != AccessLevel.ALLOW) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickUp(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (plugin.getBinder().canIgnoreBinding(player)) {
                return;
            }

            if (plugin.getBinder().getAccessLevel(event.getItem().getItemStack(), player) != AccessLevel.ALLOW) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeath(PlayerDeathEvent event) {
        if (!event.getKeepInventory()) {
            Map<Integer, ItemStack> boundItems = new HashMap<>();

            ItemStack[] inventory = event.getEntity().getInventory().getContents();

            for (int i = 0; i < inventory.length; i++) {
                ItemStack item = inventory[i];
                if (item != null && plugin.getBinder().isBound(item)) {
                    // Check for invalidly held items
                    if (plugin.getBinder().getAccessLevel(item, event.getPlayer()) != AccessLevel.ALLOW) {
                        event.getDrops().remove(item);
                    } else {
                        boundItems.put(i, item);
                    }
                }
            }

            if (boundItems.keySet().size() > 0) {
                event.getDrops().removeAll(boundItems.values());
                deathItems.put(event.getEntity().getUniqueId(), boundItems);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        UUID id = player.getUniqueId();

        if (deathItems.containsKey(id)) {
            Map<Integer, ItemStack> items = deathItems.get(id);
            PlayerInventory inventory = player.getInventory();

            for (Integer i : items.keySet()) {
                if (inventory.getItem(i) == null) {
                    inventory.setItem(i, items.get(i));
                } else {
                    // In case another plugin has taken the original slot of the item
                    inventory.addItem(items.get(i));
                }
            }

            deathItems.remove(id);
        }
    }

    @EventHandler
    public void onArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
        Player player = event.getPlayer();
        if (plugin.getBinder().canIgnoreBinding(player)) {
            return;
        }

        ItemStack item = event.getArmorStandItem();
        if (item.hasItemMeta() && plugin.getBinder().getAccessLevel(item, player) != AccessLevel.ALLOW) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onArmorDispense(BlockDispenseArmorEvent event) {
        if (!(event.getTargetEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getTargetEntity();
        if (plugin.getBinder().canIgnoreBinding(player)) {
            return;
        }

        ItemStack item = event.getItem();
        if (plugin.getBinder().isBound(item) && plugin.getBinder().getAccessLevel(item, player) != AccessLevel.ALLOW) {
            event.setCancelled(true);
        }
    }
}
