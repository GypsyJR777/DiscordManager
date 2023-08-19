package com.github.gypsyjr777.discordmanager.event;

import com.github.gypsyjr777.discordmanager.entity.DiscordGuild;
import com.github.gypsyjr777.discordmanager.service.*;
import com.github.gypsyjr777.discordmanager.utils.EmbedMessage;
import com.github.gypsyjr777.discordmanager.utils.PreparationMessage;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.update.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GuildNewsEvent extends ListenerAdapter {
    private static String TITLE = "Guild`s information changed";

    private final GuildService guildService;

    @Autowired
    public GuildNewsEvent(ApplicationContext context) {
        this.guildService = context.getBean(GuildService.class);
    }

    @SubscribeEvent
    @Override
    public void onGenericGuildUpdate(GenericGuildUpdateEvent event) {
        DiscordGuild discordGuild = guildService.findGuildById(event.getGuild().getId()).orElseThrow();

        if (discordGuild.isHaveLogGuild()) {
            JDA jda = event.getJDA();
            TextChannel textChannel = jda.getTextChannelById(discordGuild.getLogGuildChannel());
            textChannel.sendMessage("").setEmbeds(EmbedMessage.createMessageEmbed(
                            TITLE,
                            GuildUpdateEventModel.getMessage(event),
                            PreparationMessage.preparationGuildUpdateMessage(event)
                    )
            ).queue();
        }
    }
}
