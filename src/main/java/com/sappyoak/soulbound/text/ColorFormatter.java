package com.sappyoak.soulbound.text;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorFormatter {
    private static final char COLOR_CHAR = '&';
    private static final Pattern COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf(COLOR_CHAR) + "[0-9A-FK-OR]");

    public static String replaceLegacyColors(String input) {
        if (input == null) {
            return null;
        }

        Matcher matcher = COLOR_PATTERN.matcher(input);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            Colors color = Colors.getByKey(matcher.group());
            
            if (color == null) {
                continue;
            }

            matcher.appendReplacement(buffer, color.getReplacement());
        }

        matcher.appendTail(buffer);
        return buffer.toString();
    }

    public static enum Colors {
        BLACK("&0", "<black>"),
        DARK_BLUE("&1", "<dark_blue>"),
        DARK_GREEN("&2", "<dark_green>"),
        DARK_AQUA("&3", "<dark_aqua>"),
        DARK_RED("&4", "<dark_red>"),
        DARK_PURPLE("&5", "<dark_purple>"),
        GOLD("&6", "<gold>"),
        GREY("&7", "<gray>"),
        DARK_GREY("&8", "<dark_gray>"),
        BLUE("&9", "<blue>"),
        GREEN("&a", "<green>"),
        AQUA("&b", "<aqua>"),
        RED("&c", "<red>"),
        LIGHT_PURPLE("&d", "<light_purple>"),
        YELLOW("&e", "<yellow>"),
        WHITE("&f", "<white>"),
        MAGIC("&k", "<obfuscated>"),
        BOLD("&l", "<bold>"),
        STRIKETHROUGH("&m", "<strikethrough>"),
        UNDERLINE("&n", "<underlined>"),
        ITALIC("&o", "<italic>"),
        RESET("&r", "<reset>");

        private static final Map<String, Colors> BY_KEY = new HashMap<>();

        static {
            for (Colors color : values()) {
                BY_KEY.put(color.getValue(), color);
            }
        }

        public static Colors getByKey(String str) {
            return BY_KEY.get(str);
        }

        private String value;
        private String replacement;

        Colors(String value, String replacement) {
            this.value = value;
            this.replacement = replacement;
        }

        public String getValue() {
            return value;
        }

        public String getReplacement() {
            return replacement;
        }


        @Override
        public String toString() {
            return name() + "(" + value + ")" + "[" + replacement + "]";
        }
    }
}
