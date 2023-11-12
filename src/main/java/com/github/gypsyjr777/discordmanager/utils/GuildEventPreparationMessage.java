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
    private static String BASE_TITLE = "Guild`s information changed";

    public static MessageEmbed getMessage(GenericGuildUpdateEvent event) {
        if (event instanceof GuildUpdateAfkChannelEvent) return getMessage((GuildUpdateAfkChannelEvent) event);
        else if (event instanceof GuildUpdateAfkTimeoutEvent) return getMessage((GuildUpdateAfkTimeoutEvent) event);
        else if (event instanceof GuildUpdateExplicitContentLevelEvent) return getMessage((GuildUpdateExplicitContentLevelEvent) event);
        else if (event instanceof GuildUpdateFeaturesEvent) return getMessage((GuildUpdateFeaturesEvent) event);
        else if (event instanceof GuildUpdateIconEvent) return getMessage((GuildUpdateIconEvent) event);
        else if (event instanceof GuildUpdateMFALevelEvent) return getMessage((GuildUpdateMFALevelEvent) event);
        else if (event instanceof GuildUpdateNameEvent) return getMessage((GuildUpdateNameEvent) event);
        else if (event instanceof GuildUpdateNotificationLevelEvent) return getMessage((GuildUpdateNotificationLevelEvent) event);
        else if (event instanceof GuildUpdateOwnerEvent) return getMessage((GuildUpdateOwnerEvent) event);
        else if (event instanceof GuildUpdateSplashEvent) return getMessage((GuildUpdateSplashEvent) event);
        else if (event instanceof GuildUpdateSystemChannelEvent) return getMessage((GuildUpdateSystemChannelEvent) event);
        else if (event instanceof GuildUpdateBannerEvent) return getMessage((GuildUpdateBannerEvent) event);
        else if (event instanceof GuildUpdateBoostCountEvent) return getMessage((GuildUpdateBoostCountEvent) event);
        else if (event instanceof GuildUpdateBoostTierEvent) return getMessage((GuildUpdateBoostTierEvent) event);
        else if (event instanceof GuildUpdateDescriptionEvent) return getMessage((GuildUpdateDescriptionEvent) event);
        else if (event instanceof GuildUpdateMaxMembersEvent) return getMessage((GuildUpdateMaxMembersEvent) event);
        else if (event instanceof GuildUpdateMaxPresencesEvent) return getMessage((GuildUpdateMaxPresencesEvent) event);
        else if (event instanceof GuildUpdateVanityCodeEvent) return getMessage((GuildUpdateVanityCodeEvent) event);
        else if (event instanceof GuildUpdateCommunityUpdatesChannelEvent) return getMessage((GuildUpdateCommunityUpdatesChannelEvent) event);
        else if (event instanceof GuildUpdateLocaleEvent) return getMessage((GuildUpdateLocaleEvent) event);
        else if (event instanceof GuildUpdateNSFWLevelEvent) return getMessage((GuildUpdateNSFWLevelEvent) event);
        else if (event instanceof GuildUpdateRulesChannelEvent) return getMessage((GuildUpdateRulesChannelEvent) event);
        else if (event instanceof GuildUpdateVerificationLevelEvent) return getMessage((GuildUpdateVerificationLevelEvent) event);

        return MessageEmbedCreator.createMessage(BASE_TITLE, "EXCEPTION! NO INFO ABOUT CHANGING", List.of());
    }

    private static MessageEmbed getMessage(GuildUpdateIconEvent event) {
        return MessageEmbedCreator.createMessageEmbed(GUILD_UPDATE_ICON, null, null, event.getNewIconUrl());
    }

    private static MessageEmbed getMessage(GuildUpdateBannerEvent event) {
        return MessageEmbedCreator.createMessageEmbed(GUILD_UPDATE_BANNER, null, null, event.getNewBannerUrl());
    }

    private static MessageEmbed getMessage(GuildUpdateAfkChannelEvent event) {
        String oldChannel = "Empty", newChannel = "Empty";
        if (event.getOldAfkChannel() != null) oldChannel = event.getOldAfkChannel().getName();
        if (event.getNewAfkChannel() != null) newChannel = event.getNewAfkChannel().getName();

        List<MessageEmbed.Field> fields = new ArrayList<>();
        fields.add(MessageEmbedCreator.createField("from", oldChannel, true));
        fields.add(MessageEmbedCreator.createField("to", newChannel, true));
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

    private static MessageEmbed getMessage(GuildUpdateMFALevelEvent event) {
        List<MessageEmbed.Field> fields = new ArrayList<>();
        fields.add(MessageEmbedCreator.createField("from", event.getOldMFALevel().name(), true));
        fields.add(MessageEmbedCreator.createField("to", event.getNewMFALevel().name(), true));
        return MessageEmbedCreator.createMessage(GUILD_UPDATE_MFA_LEVEL, null, fields);
    }

    private static MessageEmbed getMessage(GuildUpdateNameEvent event) {
        List<MessageEmbed.Field> fields = new ArrayList<>();
        fields.add(MessageEmbedCreator.createField("from", event.getOldName(), true));
        fields.add(MessageEmbedCreator.createField("to", event.getNewName(), true));
        return MessageEmbedCreator.createMessage(GUILD_UPDATE_NAME, null, fields);
    }

    private static MessageEmbed getMessage(GuildUpdateNotificationLevelEvent event) {
        List<MessageEmbed.Field> fields = new ArrayList<>();
        fields.add(MessageEmbedCreator.createField("from", event.getOldNotificationLevel().name(), true));
        fields.add(MessageEmbedCreator.createField("to", event.getOldNotificationLevel().name(), true));
        return MessageEmbedCreator.createMessage(GUILD_UPDATE_NOTIFICATION_LEVEL, null, fields);
    }

    private static MessageEmbed getMessage(GuildUpdateOwnerEvent event) {
        String oldOwner = "Empty", newOwner = "Empty";
        if (event.getOldOwner() != null) oldOwner = event.getOldOwner().getEffectiveName();
        if (event.getNewOwner() != null) newOwner = event.getNewOwner().getEffectiveName();

        List<MessageEmbed.Field> fields = new ArrayList<>();
        fields.add(MessageEmbedCreator.createField("from", oldOwner, true));
        fields.add(MessageEmbedCreator.createField("to", newOwner, true));
        return MessageEmbedCreator.createMessage(GUILD_UPDATE_OWNER, null, fields);
    }

    private static MessageEmbed getMessage(GuildUpdateSplashEvent event) {
        return MessageEmbedCreator.createMessageEmbed(GUILD_UPDATE_SPLASH, null, null, event.getNewSplashUrl());
    }

    private static MessageEmbed getMessage(GuildUpdateSystemChannelEvent event) {
        String oldChannel = "Empty", newChannel = "Empty";
        if (event.getOldSystemChannel() != null) oldChannel = event.getOldSystemChannel().getName();
        if (event.getNewSystemChannel() != null) newChannel = event.getNewSystemChannel().getName();

        List<MessageEmbed.Field> fields = new ArrayList<>();
        fields.add(MessageEmbedCreator.createField("from", oldChannel, true));
        fields.add(MessageEmbedCreator.createField("to", newChannel, true));
        return MessageEmbedCreator.createMessage(GUILD_UPDATE_SYSTEM_CHANNEL, null, fields);
    }

    private static MessageEmbed getMessage(GuildUpdateBoostCountEvent event) {
        List<MessageEmbed.Field> fields = new ArrayList<>();
        fields.add(MessageEmbedCreator.createField("from", String.valueOf(event.getOldBoostCount()), true));
        fields.add(MessageEmbedCreator.createField("to", String.valueOf(event.getNewBoostCount()), true));
        return MessageEmbedCreator.createMessage(GUILD_UPDATE_BOOST_COUNT, null, fields);
    }

    private static MessageEmbed getMessage(GuildUpdateBoostTierEvent event) {
        List<MessageEmbed.Field> fields = new ArrayList<>();
        fields.add(MessageEmbedCreator.createField("from", event.getOldBoostTier().name(), true));
        fields.add(MessageEmbedCreator.createField("to", event.getNewBoostTier().name(), true));
        return MessageEmbedCreator.createMessage(GUILD_UPDATE_BOOST_TIER, null, fields);
    }

    private static MessageEmbed getMessage(GuildUpdateDescriptionEvent event) {
        List<MessageEmbed.Field> fields = new ArrayList<>();
        fields.add(MessageEmbedCreator.createField("from", event.getOldDescription(), true));
        fields.add(MessageEmbedCreator.createField("to", event.getNewDescription(), true));
        return MessageEmbedCreator.createMessage(GUILD_UPDATE_DESCRIPTION, null, fields);
    }

    private static MessageEmbed getMessage(GuildUpdateMaxMembersEvent event) {
        List<MessageEmbed.Field> fields = new ArrayList<>();
        fields.add(MessageEmbedCreator.createField("from", String.valueOf(event.getOldMaxMembers()), true));
        fields.add(MessageEmbedCreator.createField("to", String.valueOf(event.getNewMaxMembers()), true));
        return MessageEmbedCreator.createMessage(GUILD_UPDATE_MAX_MEMBER, null, fields);
    }

    private static MessageEmbed getMessage(GuildUpdateMaxPresencesEvent event) {
        List<MessageEmbed.Field> fields = new ArrayList<>();
        fields.add(MessageEmbedCreator.createField("from", String.valueOf(event.getOldMaxPresences()), true));
        fields.add(MessageEmbedCreator.createField("to", String.valueOf(event.getNewMaxPresences()), true));
        return MessageEmbedCreator.createMessage(GUILD_UPDATE_MAX_PRESENCES, null, fields);
    }

    private static MessageEmbed getMessage(GuildUpdateVanityCodeEvent event) {
        List<MessageEmbed.Field> fields = new ArrayList<>();
        fields.add(MessageEmbedCreator.createField("from", event.getOldVanityCode(), true));
        fields.add(MessageEmbedCreator.createField("to", event.getNewVanityCode(), true));
        return MessageEmbedCreator.createMessage(GUILD_UPDATE_VANITY_CODE, null, fields);
    }

    private static MessageEmbed getMessage(GuildUpdateCommunityUpdatesChannelEvent event) {
        String oldChannel = "Empty", newChannel = "Empty";
        if (event.getOldCommunityUpdatesChannel() != null) oldChannel = event.getOldCommunityUpdatesChannel().getName();
        if (event.getNewCommunityUpdatesChannel() != null) newChannel = event.getNewCommunityUpdatesChannel().getName();

        List<MessageEmbed.Field> fields = new ArrayList<>();
        fields.add(MessageEmbedCreator.createField("from", oldChannel, true));
        fields.add(MessageEmbedCreator.createField("to", newChannel, true));
        return MessageEmbedCreator.createMessage(GUILD_UPDATE_COMMUNITY_UPDATES_CHANNEL, null, fields);
    }

    private static MessageEmbed getMessage(GuildUpdateLocaleEvent event) {
        List<MessageEmbed.Field> fields = new ArrayList<>();
        fields.add(MessageEmbedCreator.createField("from", event.getOldValue().getLanguageName() + " - " + event.getOldValue().getNativeName(), true));
        fields.add(MessageEmbedCreator.createField("to", event.getNewValue().getLanguageName() + " - " + event.getNewValue().getNativeName(), true));
        return MessageEmbedCreator.createMessage(GUILD_UPDATE_LOCALE, null, fields);
    }

    private static MessageEmbed getMessage(GuildUpdateNSFWLevelEvent event) {
        List<MessageEmbed.Field> fields = new ArrayList<>();
        fields.add(MessageEmbedCreator.createField("from", event.getOldNSFWLevel().name(), true));
        fields.add(MessageEmbedCreator.createField("to", event.getNewNSFWLevel().name(), true));
        return MessageEmbedCreator.createMessage(GUILD_UPDATE_NSFW_LEVEL, null, fields);
    }

    private static MessageEmbed getMessage(GuildUpdateRulesChannelEvent event) {
        String oldChannel = "Empty", newChannel = "Empty";
        if (event.getOldRulesChannel() != null) oldChannel = event.getOldRulesChannel().getName();
        if (event.getNewRulesChannel() != null) newChannel = event.getNewRulesChannel().getName();

        List<MessageEmbed.Field> fields = new ArrayList<>();
        fields.add(MessageEmbedCreator.createField("from", oldChannel, true));
        fields.add(MessageEmbedCreator.createField("to", newChannel, true));
        return MessageEmbedCreator.createMessage(GUILD_UPDATE_RULES_CHANNEL, null, fields);
    }

    private static MessageEmbed getMessage(GuildUpdateVerificationLevelEvent event) {
        List<MessageEmbed.Field> fields = new ArrayList<>();
        fields.add(MessageEmbedCreator.createField("from", event.getOldVerificationLevel().name(), true));
        fields.add(MessageEmbedCreator.createField("to", event.getNewVerificationLevel().name(), true));
        return MessageEmbedCreator.createMessage(GUILD_UPDATE_VERIFICATION_LEVEL, null, fields);
    }
}
