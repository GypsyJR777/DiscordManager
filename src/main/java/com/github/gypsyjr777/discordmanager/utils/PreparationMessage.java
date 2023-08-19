package com.github.gypsyjr777.discordmanager.utils;
import net.dv8tion.jda.api.events.guild.update.*;

public class PreparationMessage {
    public static String preparationGuildUpdateMessage(GenericGuildUpdateEvent event) {
        String oldValue = "Empty value";
        String newValue = "Empty value";
        if (event instanceof GuildUpdateAfkChannelEvent) {
            oldValue = event.getOldValue() == null ? oldValue : ((GuildUpdateAfkChannelEvent) event).getOldAfkChannel().getName();
            newValue = event.getNewValue() == null ? newValue : ((GuildUpdateAfkChannelEvent) event).getNewAfkChannel().getName();
        } else if (event instanceof GuildUpdateAfkTimeoutEvent) {
            oldValue = String.valueOf(((GuildUpdateAfkTimeoutEvent) event).getOldAfkTimeout().getSeconds());
            newValue = String.valueOf(((GuildUpdateAfkTimeoutEvent) event).getNewAfkTimeout().getSeconds());
        } else if (event instanceof GuildUpdateExplicitContentLevelEvent) {
            oldValue = ((GuildUpdateExplicitContentLevelEvent) event).getOldLevel().getDescription();
            newValue = ((GuildUpdateExplicitContentLevelEvent) event).getNewLevel().getDescription();
        } else if (event instanceof GuildUpdateFeaturesEvent) {
            oldValue = ((GuildUpdateFeaturesEvent) event).getOldFeatures().toString();
            newValue = ((GuildUpdateFeaturesEvent) event).getNewFeatures().toString();
        } else if (event instanceof GuildUpdateIconEvent) {
            return ((GuildUpdateIconEvent) event).getNewIconUrl();
        } else if (event instanceof GuildUpdateMFALevelEvent) {
            oldValue = ((GuildUpdateMFALevelEvent) event).getOldMFALevel().name();
            newValue = ((GuildUpdateMFALevelEvent) event).getNewMFALevel().name();
        } else if (event instanceof GuildUpdateNameEvent) {
            oldValue = ((GuildUpdateNameEvent) event).getOldName();
            newValue = ((GuildUpdateNameEvent) event).getNewName();
        } else if (event instanceof GuildUpdateNotificationLevelEvent) {
            oldValue = ((GuildUpdateNotificationLevelEvent) event).getOldNotificationLevel().name();
            newValue = ((GuildUpdateNotificationLevelEvent) event).getNewNotificationLevel().name();
        } else if (event instanceof GuildUpdateOwnerEvent) {
            oldValue = event.getOldValue() == null ? oldValue : ((GuildUpdateOwnerEvent) event).getOldOwner().getEffectiveName();
            newValue = event.getNewValue() == null ? newValue : ((GuildUpdateOwnerEvent) event).getNewOwner().getEffectiveName();
        } else if (event instanceof GuildUpdateSplashEvent) {
            oldValue = event.getOldValue() == null ? oldValue : ((GuildUpdateSplashEvent) event).getOldSplashUrl();
            newValue = event.getNewValue() == null ? newValue : ((GuildUpdateSplashEvent) event).getNewSplashUrl();
        } else if (event instanceof GuildUpdateSystemChannelEvent) {
            oldValue = event.getOldValue() == null ? oldValue : ((GuildUpdateSystemChannelEvent) event).getOldSystemChannel().getName();
            newValue = event.getNewValue() == null ? newValue : ((GuildUpdateSystemChannelEvent) event).getNewSystemChannel().getName();
        } else if (event instanceof GuildUpdateBannerEvent) {
            oldValue = event.getOldValue() == null ? oldValue : ((GuildUpdateBannerEvent) event).getOldBannerUrl();
            newValue = event.getNewValue() == null ? newValue : ((GuildUpdateBannerEvent) event).getNewBannerUrl();
        } else if (event instanceof GuildUpdateBoostCountEvent) {
            oldValue = String.valueOf(((GuildUpdateBoostCountEvent) event).getOldBoostCount());
            newValue = String.valueOf(((GuildUpdateBoostCountEvent) event).getNewBoostCount());
        } else if (event instanceof GuildUpdateBoostTierEvent) {
            oldValue = ((GuildUpdateBoostTierEvent) event).getOldBoostTier().name();
            newValue = ((GuildUpdateBoostTierEvent) event).getNewValue().name();
        } else if (event instanceof GuildUpdateDescriptionEvent) {
            oldValue = event.getOldValue() == null ? oldValue : ((GuildUpdateDescriptionEvent) event).getOldDescription();
            newValue = event.getNewValue() == null ? newValue : ((GuildUpdateDescriptionEvent) event).getNewDescription();
        } else if (event instanceof GuildUpdateMaxMembersEvent) {
            oldValue = String.valueOf(((GuildUpdateMaxMembersEvent) event).getOldMaxMembers());
            newValue = String.valueOf(((GuildUpdateMaxMembersEvent) event).getNewMaxMembers());
        } else if (event instanceof GuildUpdateMaxPresencesEvent) {
            oldValue = String.valueOf(((GuildUpdateMaxPresencesEvent) event).getOldMaxPresences());
            newValue = String.valueOf(((GuildUpdateMaxPresencesEvent) event).getNewMaxPresences());
        } else if (event instanceof GuildUpdateVanityCodeEvent) {
            oldValue = event.getOldValue() == null ? oldValue : ((GuildUpdateVanityCodeEvent) event).getOldVanityUrl();
            newValue = event.getNewValue() == null ? newValue : ((GuildUpdateVanityCodeEvent) event).getNewVanityCode();
        } else if (event instanceof GuildUpdateCommunityUpdatesChannelEvent) {
            oldValue = event.getOldValue() == null ? oldValue : ((GuildUpdateCommunityUpdatesChannelEvent) event).getOldCommunityUpdatesChannel().getName();
            newValue = event.getNewValue() == null ? newValue : ((GuildUpdateCommunityUpdatesChannelEvent) event).getNewCommunityUpdatesChannel().getName();
        } else if (event instanceof GuildUpdateLocaleEvent) {
            oldValue = ((GuildUpdateLocaleEvent) event).getOldValue().getLocale();
            newValue = ((GuildUpdateLocaleEvent) event).getNewValue().getLocale();
        } else if (event instanceof GuildUpdateNSFWLevelEvent) {
            oldValue = ((GuildUpdateNSFWLevelEvent) event).getOldNSFWLevel().name();
            newValue = ((GuildUpdateNSFWLevelEvent) event).getNewNSFWLevel().name();
        } else if (event instanceof GuildUpdateRulesChannelEvent) {
            oldValue = event.getOldValue() == null ? oldValue : ((GuildUpdateRulesChannelEvent) event).getOldRulesChannel().getName();
            newValue = event.getNewValue() == null ? newValue : ((GuildUpdateRulesChannelEvent) event).getNewRulesChannel().getName();
        } else if (event instanceof GuildUpdateVerificationLevelEvent) {
            oldValue = ((GuildUpdateVerificationLevelEvent) event).getOldVerificationLevel().name();
            newValue = ((GuildUpdateVerificationLevelEvent) event).getNewVerificationLevel().name();
        }

        return oldValue + " to " + newValue;
    }
}
