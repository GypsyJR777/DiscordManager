package com.github.gypsyjr777.discordmanager.event;

import com.github.gypsyjr777.discordmanager.entity.DiscordGuild;
import com.github.gypsyjr777.discordmanager.service.GuildService;
import com.github.gypsyjr777.discordmanager.service.UserService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GuildMessageEvent extends ListenerAdapter {

    private final UserService userService;
    private final GuildService guildService;

    @Autowired
    public GuildMessageEvent(UserService userService, GuildService guildService) {
        this.userService = userService;
        this.guildService = guildService;
    }

    @Override
    @SubscribeEvent
    public void onMessageReceived(MessageReceivedEvent event) {
        Guild guild = event.getGuild();
        DiscordGuild discordGuild = guildService.findGuildById(guild.getId()).orElseThrow();
        if (guild.getCommunityUpdatesChannel() != null && guild.getCommunityUpdatesChannel().getId().equals(event.getChannel().getId())){
            return;
        }
        Member member = event.getMember();
        userService.updateDateUser(member.getUser(), discordGuild);
    }
}
