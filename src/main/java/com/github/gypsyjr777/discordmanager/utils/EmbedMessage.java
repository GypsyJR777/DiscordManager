package com.github.gypsyjr777.discordmanager.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class EmbedMessage {
    public static MessageEmbed createMessageEmbed(String title, String description, String footer, String image) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle(title);
        embedBuilder.setDescription(description);
        embedBuilder.setFooter(footer);
        embedBuilder.setImage(image);

        return embedBuilder.build();
    }

    public static MessageEmbed createMessageEmbed(String title, String description, String footer) {
        return createMessageEmbed(title, description, footer, null);
    }

    public static MessageEmbed createMessageEmbed(String title, String description) {
        return createMessageEmbed(title, description, null);
    }
}
