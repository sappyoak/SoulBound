package com.sappyoak.soulbound.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

import com.sappyoak.soulbound.SoulBound;
import com.sappyoak.soulbound.util.Predicates;

public class GroupBindCommand implements CommandInterface {
    private SoulBound plugin;
    
    public GroupBindCommand(SoulBound plugin) {
        this.plugin = plugin;
    }

    public List<String> getCommandIds() {
        return Arrays.asList("sb-group", "sbg");
    }

    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(plugin.messages.applyPrefix("<red> You must provide group permission name to bind to"));
            return true;
        }

        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItemInMainHand();

        if (Predicates.ITEM_IS_AIR.test(item)) {
            player.sendMessage(plugin.messages.emptyMainhand());
            return true;
        }

        if (plugin.api.isItemSoulBound(item)) {
            player.sendMessage(plugin.messages.alreadyBound());
            return true;
        }

        plugin.api.bindItemToGroup(item, args[0], player);
        return true;
    }
}
