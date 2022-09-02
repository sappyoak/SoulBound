package com.sappyoak.soulbound.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;

import java.util.List;
import java.util.stream.Collectors;

public class TextProvider {
    private static final MiniMessage miniMessage = MiniMessage.builder()
        .tags(TagResolver.builder()
            .resolver(StandardTags.defaults())
            .resolver(TagResolver.resolver("a", Resolvers::linkResolver))
            .build()
        )
        .build();
    
    public static List<String> serializeComponentList(List<Component> components) {
        return components.stream()
            .map(miniMessage::serialize)
            .map(ColorFormatter::replaceLegacyColors)
            .collect(Collectors.toList());
    }

    public static String serialize(Component component) {
        return miniMessage.serialize(component);
    }

    public static List<String> serialize(List<Component> components) {
        return components.stream()
            .map(miniMessage::serialize)
            .map(ColorFormatter::replaceLegacyColors)
            .collect(Collectors.toList());
    }

    public static Component deserialize(String string) {
        return miniMessage.deserialize(ColorFormatter.replaceLegacyColors(string));
    }

    public static Component deserialize(String str, String tagTarget, String replacementText) {
        return miniMessage.deserialize(str, Placeholder.parsed(tagTarget, replacementText));
    }
    
    public static List<Component> deserialize(List<String> strings) {
        return strings.stream()
            .map(ColorFormatter::replaceLegacyColors)
            .map(miniMessage::deserialize)
            .collect(Collectors.toList());
    }
}
