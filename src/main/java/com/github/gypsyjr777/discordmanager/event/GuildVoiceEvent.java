package com.github.gypsyjr777.discordmanager.event;

import com.github.gypsyjr777.discordmanager.entity.DiscordGuild;
import com.github.gypsyjr777.discordmanager.service.GuildService;
import com.github.gypsyjr777.discordmanager.service.UserService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class GuildVoiceEvent extends ListenerAdapter {

    private final UserService userService;
    private final GuildService guildService;

    @Autowired
    public GuildVoiceEvent(ApplicationContext context) {
        this.userService = context.getBean(UserService.class);
        this.guildService = context.getBean(GuildService.class);
    }

    @Override
    @SubscribeEvent
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        Guild guild = event.getGuild();
        DiscordGuild discordGuild = guildService.findGuildById(guild.getId()).orElseThrow();

        Member member = event.getMember();
        userService.updateDateUser(member.getUser(), discordGuild);
    }
}
