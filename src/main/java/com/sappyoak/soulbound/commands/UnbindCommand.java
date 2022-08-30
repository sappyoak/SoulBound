package com.sappyoak.soulbound.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class UnbindCommand implements CommandInterface {
    private CommandExecutor executor;

    public UnbindCommand(CommandExecutor executor) {
        this.executor = executor;
    }

    public List<String> getCommandIds() {
        return Arrays.asList("soulunbind", "sunbind");
    }

    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(executor.getMessages().getNoSuchPlayer());
            return true;
        }

        if (target.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
            target.sendMessage(executor.getMessages().getEmptyMainhand());
            player.sendMessage(executor.getMessages().applyPrefix(" Player " + target.getName() + " has nothing in their main hand"));
            return true;
        }

        ItemStack item = target.getInventory().getItemInMainHand();
        if (!executor.getPlugin().getBinder().isBound(item)) {
            target.sendMessage(executor.getMessages().getNotBound());
            player.sendMessage(executor.getMessages().getNotBound());
            return true;
        }

        cleanLore(item);

        ItemStack newItem = executor.getPlugin().getBinder().removeBinds(item);
        target.getInventory().setItemInMainHand(newItem);
        target.sendMessage(executor.getMessages().getUnbindSuccess());
        // play sound

        return true;
    }

    private void cleanLore(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();

        if (lore != null) {
            for (String line : lore) {
                if (line.startsWith(executor.getMessages().getLoreText().split("%")[0])) {
                    lore.remove(line);
                    break;
                }
            }
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
    }
}