package com.sappyoak.soulbound.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.function.Predicate;

public final class Predicates {
    public final static Predicate<ItemStack> ITEM_IS_AIR = item -> item.getType().equals(Material.AIR);    
}
