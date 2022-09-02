package com.sappyoak.soulbound.config;

import org.bukkit.entity.Player;

public final class Permissions {
    public static final String ADMIN = "soulbound.admin";
    public static final String BYPASS_ANVIL = "soulbound.bypass.anvil";
    public static final String BYPASS_CRAFT = "soulbound.bypass.craft";
    public static final String BYPASS_ENCHANT = "soulbound.bypass.enchant";
    public static final String BYPASS_POSSES  = "soulbound.bypass.posses";
    public static final String DEBUG = "soulbound.debug";
    public static final String GROUP_BIND_ROOT = "soulbound.group.";

    public static boolean canBypassBinding(Player player) {
        return player.hasPermission(ADMIN) || player.hasPermission(BYPASS_POSSES);
    }

    public static boolean canSetDebug(Player player) {
        return player.hasPermission(Permissions.DEBUG);
    }
    
    public static String createGroupPermission(String name) {
        return GROUP_BIND_ROOT + name;
    }
}
