package com.sappyoak.soulbound.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

import com.sappyoak.soulbound.SoulBound;

public class UnbindCommand implements CommandInterface {
    private SoulBound plugin;

    public UnbindCommand(SoulBound plugin) {
        this.plugin = plugin;
    }

    public List<String> getCommandIds() {
        return Arrays.asList("soulunbind", "sunbind");
    }

    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (target == null) {
            player.sendMessage(plugin.messages.noSuchPlayer());
            return false;
        }

        if (!target.isOnline()) {
            player.sendMessage(plugin.messages.applyPrefix("Player " + target.getName() + " is not online right now"));
            return true;
        }

        Player targetPlayer = target.getPlayer();
        boolean isTargetExecutor = targetPlayer.getUniqueId() == player.getUniqueId();
        ItemStack item = targetPlayer.getInventory().getItemInMainHand();

        if (item.getType().equals(Material.AIR)) {
            targetPlayer.sendMessage(plugin.messages.emptyMainhand());
            if (!isTargetExecutor) {
                player.sendMessage(plugin.messages.applyPrefix("Player " + targetPlayer.getName() + " has nothing in their main hand"));
            }
            return true;
        }

        if (!plugin.api.isItemSoulBound(item)) {
            targetPlayer.sendMessage(plugin.messages.notBound());
            if (!isTargetExecutor) {
                player.sendMessage(plugin.messages.notBound());
            }
            return true;
        }

        ItemStack newItem = plugin.api.removeItemBinding(item);
        targetPlayer.getInventory().setItemInMainHand(newItem);
        targetPlayer.sendMessage(plugin.messages.unbindSuccess());

        return true;
    }
}
