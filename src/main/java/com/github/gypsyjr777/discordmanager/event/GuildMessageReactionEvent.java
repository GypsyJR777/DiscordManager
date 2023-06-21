package com.github.gypsyjr777.discordmanager.event;

import com.github.gypsyjr777.discordmanager.entity.DiscordGuild;
import com.github.gypsyjr777.discordmanager.entity.DiscordRole;
import com.github.gypsyjr777.discordmanager.service.GuildMemberService;
import com.github.gypsyjr777.discordmanager.service.GuildService;
import com.github.gypsyjr777.discordmanager.service.RoleService;
import com.github.gypsyjr777.discordmanager.service.UserService;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.springframework.stereotype.Service;

@Service
public class GuildMessageReactionEvent extends ListenerAdapter {
    private final GuildService guildService;
    private final GuildMemberService guildMemberService;
    private final UserService userService;
    private final RoleService roleService;

    public GuildMessageReactionEvent(GuildService guildService, GuildMemberService guildMemberService, UserService userService, RoleService roleService) {
        this.guildService = guildService;
        this.guildMemberService = guildMemberService;
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    @SubscribeEvent
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        DiscordGuild guild = guildService.findGuildById(event.getGuild().getId()).orElseThrow();
        if (guild.getMessageId().equals(event.getMessageId())) {
            DiscordRole discordRole = roleService.findRoleByReaction(event.getReaction().getEmoji().getFormatted()).orElseThrow();
            event.getGuild().addRoleToMember(event.getUser(), event.getGuild().getRoleById(discordRole.getId())).queue();
        }
    }
}
