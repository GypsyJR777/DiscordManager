package com.github.gypsyjr777.discordmanager.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.List;

public class MessageEmbedCreator {
    public static MessageEmbed createMessageEmbed(String title, String description, String footer, String image) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle(title);
        embedBuilder.setDescription(description);
        embedBuilder.setFooter(footer);
        embedBuilder.setImage(image);

        return embedBuilder.build();
    }

    public static MessageEmbed createMessageEmbedWithDate(String title, String description, OffsetDateTime timestamp) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle(title);
        embedBuilder.setDescription(description);
        embedBuilder.setTimestamp(timestamp);

        return embedBuilder.build();
    }

    public static MessageEmbed createMessageEmbed(String title, String description, String footer) {
        return createMessageEmbed(title, description, footer, null);
    }

    public static MessageEmbed createMessageEmbed(String title, String description) {
        return createMessageEmbed(title, description, null);
    }

    public static MessageEmbed createFullMessageEmbed(
            String url, String title, String description, OffsetDateTime timestamp, String thumbnailUrl,
            MessageEmbed.AuthorInfo author, MessageEmbed.Footer footer, String imageUrl,
            List<MessageEmbed.Field> fields, Color color
    ) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setUrl(url);
        embedBuilder.setTitle(title);
        embedBuilder.setDescription(description);
        embedBuilder.setTimestamp(timestamp);
        embedBuilder.setColor(color);
        embedBuilder.setThumbnail(thumbnailUrl);
        if (author != null) embedBuilder.setAuthor(author.getName(), author.getUrl(), author.getIconUrl());
        if (footer != null) embedBuilder.setFooter(footer.getText(), footer.getIconUrl());
        embedBuilder.setImage(imageUrl);
        if (fields != null && !fields.isEmpty()) fields.forEach(embedBuilder::addField);

        return embedBuilder.build();
    }

    public static MessageEmbed.Field createField(String name, String value, boolean inline) {
        return new MessageEmbed.Field(name, value, inline);
    }

    public static MessageEmbed createMessage(
            MessageEmbed.AuthorInfo author, String title, String description, MessageEmbed.Field field
    ) {
        return createMessage(author, title, description, List.of(field));
    }

    public static MessageEmbed createMessage(
            MessageEmbed.AuthorInfo author, String title, String description, List<MessageEmbed.Field> field
    ) {
        return createFullMessageEmbed(
                null, title, description, OffsetDateTime.now(), null, author,
                null, null, field, Color.CYAN);
    }

    public static MessageEmbed createMessage(
            MessageEmbed.AuthorInfo author, String title, String description
    ) {
        return createMessage(author, title, description, List.of());
    }

    public static MessageEmbed createMessage(
            String title, String description, List<MessageEmbed.Field> field
    ) {
        return createMessage(null, title, description, field);
    }

    public static MessageEmbed.AuthorInfo createAuthorInfo(
            String name, String url, String iconUrl, String proxyIconUrl
    ) {
        return new MessageEmbed.AuthorInfo(name, url, iconUrl, proxyIconUrl);
    }
}
