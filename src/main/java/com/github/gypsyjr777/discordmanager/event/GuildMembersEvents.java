package com.github.gypsyjr777.discordmanager.event;

import com.github.gypsyjr777.discordmanager.entity.DiscordGuild;
import com.github.gypsyjr777.discordmanager.entity.GuildMember;
import com.github.gypsyjr777.discordmanager.service.GuildMemberService;
import com.github.gypsyjr777.discordmanager.service.GuildService;
import com.github.gypsyjr777.discordmanager.service.UserService;
import com.github.gypsyjr777.discordmanager.utils.CheckLeaveTimer;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class GuildMembersEvents extends ListenerAdapter {
    private final UserService userService;
    private final GuildService guildService;

    private final GuildMemberService memberService;

    @Autowired
    public GuildMembersEvents(UserService userService, GuildService guildService, GuildMemberService memberService) {
        this.userService = userService;
        this.guildService = guildService;
        this.memberService = memberService;
    }

    @Override
    @SubscribeEvent
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Guild guild = event.getGuild();
        DiscordGuild discordGuild = guildService.findGuildById(guild.getId()).orElseThrow();
        Member member = event.getMember();
        userService.createNewUser(member.getUser(),
                CheckLeaveTimer.checkLeaveTimerMember(member, discordGuild),
                discordGuild
        );
    }

    @Override
    @SubscribeEvent
    @Transactional
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        Guild guild = event.getGuild();
        DiscordGuild discordGuild = guildService.findGuildById(guild.getId()).orElseThrow();

        memberService.deleteGuildUser(
                userService.findByIdDiscordUser(event.getMember().getUser().getId()).orElseThrow(),
                discordGuild
        );
    }
}
