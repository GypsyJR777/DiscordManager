package com.github.gypsyjr777.discordmanager.event;

import com.github.gypsyjr777.discordmanager.entity.DiscordGuild;
import com.github.gypsyjr777.discordmanager.exception.NullMemberException;
import com.github.gypsyjr777.discordmanager.service.GuildService;
import com.github.gypsyjr777.discordmanager.service.UserService;
import com.github.gypsyjr777.discordmanager.utils.BasicUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class GuildMessageEvent extends ListenerAdapter {

    private final UserService userService;
    private final GuildService guildService;
    private final BasicUtils utils;

    public GuildMessageEvent(ApplicationContext context) {
        this.userService = context.getBean(UserService.class);
        this.guildService = context.getBean(GuildService.class);
        this.utils = context.getBean(BasicUtils.class);
    }

    @Override
    @SubscribeEvent
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.isFromGuild()) return;
        Guild guild = event.getGuild();
        DiscordGuild discordGuild = guildService.findGuildById(guild.getId())
                .orElseGet(() -> utils.createDiscordGuild(guild));

        if (guild.getCommunityUpdatesChannel() != null && guild.getCommunityUpdatesChannel().getId().equals(event.getChannel().getId())){
            return;
        } else if (event.getAuthor().isBot()){
            return;
        }

        Member member = event.getMember();

        if (member == null) throw new NullMemberException("NULL");

        userService.updateDateUser(member.getUser(), discordGuild);
    }
}
