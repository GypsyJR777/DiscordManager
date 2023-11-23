package com.github.gypsyjr777.discordmanager.event;

import com.github.gypsyjr777.discordmanager.entity.DiscordGuild;
import com.github.gypsyjr777.discordmanager.exception.NullChannelException;
import com.github.gypsyjr777.discordmanager.utils.BasicUtils;
import com.github.gypsyjr777.discordmanager.utils.GuildEventPreparationMessage;
import com.github.gypsyjr777.discordmanager.service.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.update.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class GuildNewsEvent extends ListenerAdapter {

    private final GuildService guildService;
    private final BasicUtils utils;
    private final Logger log;

    public GuildNewsEvent(ApplicationContext context) {
        this.guildService = context.getBean(GuildService.class);
        this.utils = context.getBean(BasicUtils.class);
        this.log = LogManager.getLogger(GuildNewsEvent.class);
    }

    @SubscribeEvent
    @Override
    public void onGenericGuildUpdate(GenericGuildUpdateEvent event) {
        log.info("Guild {} setting/information was updated", event.getGuild().getName());

        DiscordGuild discordGuild = guildService.findGuildById(event.getGuild().getId())
                .orElseGet(() -> utils.createDiscordGuild(event.getGuild()));

        if (discordGuild.isHaveLogGuild()) {
            JDA jda = event.getJDA();
            TextChannel textChannel = jda.getTextChannelById(discordGuild.getLogGuildChannel());
            if (textChannel == null) throw new NullChannelException(discordGuild.getLogMemberChannel());

            textChannel.sendMessage("").setEmbeds(GuildEventPreparationMessage.getMessage(event)).queue();
        }
    }
}
