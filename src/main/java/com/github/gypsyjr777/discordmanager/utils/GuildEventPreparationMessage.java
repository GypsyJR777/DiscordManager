package com.github.gypsyjr777.discordmanager.utils;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.guild.update.*;

import java.util.ArrayList;
import java.util.List;

public class GuildEventPreparationMessage {
    private static String GUILD_UPDATE_AFK_CHANNEL = "Update AFK channel";
    private static String GUILD_UPDATE_AFK_TIMEOUT = "Update AFK timeout";
    private static String GUILD_UPDATE_EXPLICIT_CONTENT_LEVEL = "Update explicit content level";
    private static String GUILD_UPDATE_FEATURES = "Update features";
    private static String GUILD_UPDATE_ICON = "Update icon";
    private static String GUILD_UPDATE_MFA_LEVEL = "Update MFA level";
    private static String GUILD_UPDATE_NAME = "Update name";
    private static String GUILD_UPDATE_NOTIFICATION_LEVEL = "Update notification level";
    private static String GUILD_UPDATE_OWNER = "Update owner";
    private static String GUILD_UPDATE_SPLASH = "Update splash";
    private static String GUILD_UPDATE_SYSTEM_CHANNEL = "Update system channel";
    private static String GUILD_UPDATE_BANNER = "Update banner";
    private static String GUILD_UPDATE_BOOST_COUNT = "Update boost count";
    private static String GUILD_UPDATE_BOOST_TIER = "Update boost tier";
    private static String GUILD_UPDATE_DESCRIPTION = "Update description";
    private static String GUILD_UPDATE_MAX_MEMBER = "Update max members";
    private static String GUILD_UPDATE_MAX_PRESENCES = "Update max presences";
    private static String GUILD_UPDATE_VANITY_CODE = "Update vanity code";
    private static String GUILD_UPDATE_COMMUNITY_UPDATES_CHANNEL = "Update community updates channel";
    private static String GUILD_UPDATE_LOCALE = "Update locale";
    private static String GUILD_UPDATE_NSFW_LEVEL = "Update NSFW level";
    private static String GUILD_UPDATE_RULES_CHANNEL = "Update rules";
    private static String GUILD_UPDATE_VERIFICATION_LEVEL = "Update verification level";

    private final String message;
    private final Class<? extends GenericGuildUpdateEvent> aClass;

    GuildEventPreparationMessage(Class<? extends GenericGuildUpdateEvent> aClass, String message) {
        this.aClass = aClass;
        this.message = message;
    }

    public static MessageEmbed getMessage(GenericGuildUpdateEvent event) {
        return getMessage(event);
    }

    private static MessageEmbed getMessage(GuildUpdateIconEvent event) {
        return MessageEmbedCreator.createMessageEmbed(GUILD_UPDATE_ICON, null, null, event.getNewIconUrl());
    }

    private static MessageEmbed getMessage(GuildUpdateBannerEvent event) {
        return MessageEmbedCreator.createMessageEmbed(GUILD_UPDATE_BANNER, null, null, event.getNewBannerUrl());
    }

    private static MessageEmbed getMessage(GuildUpdateAfkChannelEvent event) {
        List<MessageEmbed.Field> fields = new ArrayList<>();
        fields.add(MessageEmbedCreator.createField("from", event.getOldAfkChannel().getName(), true));
        fields.add(MessageEmbedCreator.createField("to", event.getNewAfkChannel().getName(), true));
        return MessageEmbedCreator.createMessage(GUILD_UPDATE_AFK_CHANNEL, null, fields);
    }

    private static MessageEmbed getMessage(GuildUpdateAfkTimeoutEvent event) {
        List<MessageEmbed.Field> fields = new ArrayList<>();

        int afkSec = event.getOldAfkTimeout().getSeconds();
        fields.add(MessageEmbedCreator.createField("from", String.valueOf(afkSec > 60 ? afkSec / 60.0 : afkSec), true));
        afkSec = event.getNewAfkTimeout().getSeconds();
        fields.add(MessageEmbedCreator.createField("to", String.valueOf(afkSec > 60 ? afkSec / 60.0 : afkSec), true));

        return MessageEmbedCreator.createMessage(GUILD_UPDATE_AFK_TIMEOUT, null, fields);
    }

    private static MessageEmbed getMessage(GuildUpdateExplicitContentLevelEvent event) {
        List<MessageEmbed.Field> fields = new ArrayList<>();
        fields.add(MessageEmbedCreator.createField("from", event.getOldLevel().getDescription(), true));
        fields.add(MessageEmbedCreator.createField("to", event.getNewLevel().getDescription(), true));
        return MessageEmbedCreator.createMessage(GUILD_UPDATE_EXPLICIT_CONTENT_LEVEL, null, fields);
    }

    private static MessageEmbed getMessage(GuildUpdateFeaturesEvent event) {
        List<MessageEmbed.Field> fields = new ArrayList<>();
        fields.add(MessageEmbedCreator.createField("from", event.getOldFeatures().toString(), false));
        fields.add(MessageEmbedCreator.createField("to", event.getNewFeatures().toString(), false));
        return MessageEmbedCreator.createMessage(GUILD_UPDATE_FEATURES, null, fields);
    }


}
