package com.sappyoak.soulbound.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class BindCommand implements CommandInterface {
    private CommandExecutor executor;

    public BindCommand(CommandExecutor executor) {
        this.executor = executor;
    }

    public List<String> getCommandIds() {
        return Arrays.asList("soulbind", "sbind");
    }

    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
            player.sendMessage(executor.getMessages().getEmptyMainhand());
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (executor.getPlugin().getBinder().isBound(item)) {
            player.sendMessage(executor.getMessages().getAlreadyBound());
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(executor.getMessages().getNoSuchPlayer());
            return true;
        }

        executor.getPlugin().getBinder().bindToPlayer(item, target);
        
        player.getWorld().spawnParticle(Particle.SPELL_WITCH, player.getLocation().add(0, 1, 0), 30, 0.5, 0.5, 0.5);
        player.playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_PREPARE_MIRROR, 1, 2);
        
        player.sendMessage(executor.getMessages().getBindSuccess());
        target.sendMessage(executor.getMessages().getBindSuccess());
        
        return true;
    }
}
