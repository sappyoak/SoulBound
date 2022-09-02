package com.sappyoak.soulbound.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

import com.sappyoak.soulbound.SoulBound;

public class BindCommand implements CommandInterface {
    private SoulBound plugin;

    public BindCommand(SoulBound plugin) {
        this.plugin = plugin;
    }

    public List<String> getCommandIds() {
        return Arrays.asList("soulbind", "sbind");
    }

    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType().equals(Material.AIR)) {
            player.sendMessage(plugin.messages.emptyMainhand());
            return true;
        }

        if (plugin.api.isItemSoulBound(item)) {
            player.sendMessage(plugin.messages.alreadyBound());
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (target == null) {
            player.sendMessage(plugin.messages.noSuchPlayer());
            return false;
        }

        plugin.api.bindItemToPlayer(item, target, player);

        player.getWorld().spawnParticle(Particle.SPELL_WITCH, player.getLocation().add(0, 1, 0), 30, 0.5, 0.5, 0.5);
        player.playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_PREPARE_MIRROR, 1, 2);
        
        player.sendMessage(plugin.messages.bindSuccess());
        if (target.isOnline()) {
            target.getPlayer().sendMessage(plugin.messages.bindSuccess());
        }
        
        return true;
    }
}
