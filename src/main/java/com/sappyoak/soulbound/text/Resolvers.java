package com.sappyoak.soulbound.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;

public final class Resolvers {
    public static final Tag linkResolver(final ArgumentQueue queue, final Context context) {
        final String link = queue.popOr("The <a> tag requires exactly one argument, the link to open").value();

        return Tag.styling(
            NamedTextColor.AQUA,
            TextDecoration.UNDERLINED,
            ClickEvent.openUrl(link),
            HoverEvent.showText(Component.text("Open " + link))
        );
    }
}
