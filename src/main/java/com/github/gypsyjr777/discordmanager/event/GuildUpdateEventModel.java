package com.github.gypsyjr777.discordmanager.event;

import net.dv8tion.jda.api.events.guild.update.*;

enum GuildUpdateEventModel {
    GUILD_UPDATE_AFK_CHANNEL(GuildUpdateAfkChannelEvent.class, "Update AFK channel"),
    GUILD_UPDATE_AFK_TIMEOUT(GuildUpdateAfkTimeoutEvent.class, "Update AFK timeout"),
    GUILD_UPDATE_EXPLICIT_CONTENT_LEVEL(GuildUpdateExplicitContentLevelEvent.class, "Update explicit content level"),
    GUILD_UPDATE_FEATURES(GuildUpdateFeaturesEvent.class, "Update features"),
    GUILD_UPDATE_ICON(GuildUpdateIconEvent.class, "Update icon"),
    GUILD_UPDATE_MFA_LEVEL(GuildUpdateMFALevelEvent.class, "Update MFA level"),
    GUILD_UPDATE_NAME(GuildUpdateNameEvent.class, "Update name"),
    GUILD_UPDATE_NOTIFICATION_LEVEL(GuildUpdateNotificationLevelEvent.class, "Update notification level"),
    GUILD_UPDATE_OWNER(GuildUpdateOwnerEvent.class, "Update owner"),
    GUILD_UPDATE_SPLASH(GuildUpdateSplashEvent.class, "Update splash"),
    GUILD_UPDATE_SYSTEM_CHANNEL(GuildUpdateSystemChannelEvent.class, "Update system channel"),
    GUILD_UPDATE_BANNER(GuildUpdateBannerEvent.class, "Update banner"),
    GUILD_UPDATE_BOOST_COUNT(GuildUpdateBoostCountEvent.class, "Update boost count"),
    GUILD_UPDATE_BOOST_TIER(GuildUpdateBoostTierEvent.class, "Update boost tier"),
    GUILD_UPDATE_DESCRIPTION(GuildUpdateDescriptionEvent.class, "Update description"),
    GUILD_UPDATE_MAX_MEMBER(GuildUpdateMaxMembersEvent.class, "Update max members"),
    GUILD_UPDATE_MAX_PRESENCES(GuildUpdateMaxPresencesEvent.class, "Update max presences"),
    GUILD_UPDATE_VANITY_CODE(GuildUpdateVanityCodeEvent.class, "Update vanity code"),
    GUILD_UPDATE_COMMUNITY_UPDATES_CHANNEL(GuildUpdateCommunityUpdatesChannelEvent.class, "Update community updates channel"),
    GUILD_UPDATE_LOCALE(GuildUpdateLocaleEvent.class, "Update locale"),
    GUILD_UPDATE_NSFW_LEVEL(GuildUpdateNSFWLevelEvent.class, "Update NSFW level"),
    GUILD_UPDATE_RULES_CHANNEL(GuildUpdateRulesChannelEvent.class, "Update rules"),
    GUILD_UPDATE_VERIFICATION_LEVEL(GuildUpdateVerificationLevelEvent.class, "Update verification level");

    private final String message;
    private final Class<? extends GenericGuildUpdateEvent> aClass;

    GuildUpdateEventModel(Class<? extends GenericGuildUpdateEvent> aClass, String message) {
        this.aClass = aClass;
        this.message = message;
    }

    public static String getMessage(GenericGuildUpdateEvent aClass) {
        for (GuildUpdateEventModel eventModel : GuildUpdateEventModel.values()) {
            if (eventModel.aClass.equals(aClass.getClass())) {
                return eventModel.message;
            }
        }

        return "";
    }
}
