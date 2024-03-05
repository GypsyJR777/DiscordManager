package com.github.gypsyjr777.discordmanager.event;

import com.github.gypsyjr777.discordmanager.entity.DiscordGuild;
import com.github.gypsyjr777.discordmanager.service.GuildService;
import com.github.gypsyjr777.discordmanager.service.UserService;
import com.github.gypsyjr777.discordmanager.service.BasicUtilsService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GuildVoiceEvent extends ListenerAdapter {

    private final UserService userService;
    private final GuildService guildService;
    private final BasicUtilsService utils;

    @Autowired
    public GuildVoiceEvent(UserService userService, GuildService guildService, BasicUtilsService utils) {
        this.userService = userService;
        this.guildService = guildService;
        this.utils = utils;
    }

    @Override
    @SubscribeEvent
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        Guild guild = event.getGuild();
        DiscordGuild discordGuild = guildService.findGuildById(guild.getId())
                .orElseGet(() -> utils.createDiscordGuild(guild));

        Member member = event.getMember();

        userService.updateDateUser(member.getUser(), discordGuild, isLeft(event, guild));
    }

    private boolean isLeft(GuildVoiceUpdateEvent event, Guild guild) {
        return event.getChannelLeft() != null && guild.getAfkChannel() != null && guild.getAfkChannel().getIdLong() != event.getChannelLeft().asVoiceChannel().getIdLong();
    }
}
