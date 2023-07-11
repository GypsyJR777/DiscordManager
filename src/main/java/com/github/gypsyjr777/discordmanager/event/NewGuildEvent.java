package com.github.gypsyjr777.discordmanager.event;

import com.github.gypsyjr777.discordmanager.entity.DiscordGuild;
import com.github.gypsyjr777.discordmanager.entity.DiscordRole;
import com.github.gypsyjr777.discordmanager.entity.DiscordUser;
import com.github.gypsyjr777.discordmanager.entity.GuildMember;
import com.github.gypsyjr777.discordmanager.service.GuildMemberService;
import com.github.gypsyjr777.discordmanager.service.GuildService;
import com.github.gypsyjr777.discordmanager.service.RoleService;
import com.github.gypsyjr777.discordmanager.service.UserService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class NewGuildEvent extends ListenerAdapter {
    private final UserService userService;
    private final GuildService guildService;
    private final GuildMemberService guildMemberService;
    private final RoleService roleService;

    @Autowired
    public NewGuildEvent(UserService userService, GuildService guildService, GuildMemberService guildMemberService, RoleService roleService) {
        this.userService = userService;
        this.guildService = guildService;
        this.guildMemberService = guildMemberService;
        this.roleService = roleService;
    }

    //TODO убрать дубляж
    @Override
    @SubscribeEvent
    @Transactional
    public void onGuildJoin(GuildJoinEvent event) {
        Guild guild = event.getGuild();
        DiscordGuild discordGuild = guildService.findGuildById(guild.getId()).orElse(new DiscordGuild(guild));
        guildService.saveGuild(discordGuild);
        guild.getMembers().forEach(member -> {
            DiscordUser user = userService.findByIdDiscordUser(member.getUser().getId()).orElse(new DiscordUser(member.getUser()));
            GuildMember guildMember = guildMemberService.findGuildMemberByMemberAndGuild(user, discordGuild).orElse(new GuildMember(user, discordGuild));

            if (guildMember.getLastOut() == null) {
                guildMember.setLastOut(LocalDateTime.now());
            }

            userService.saveGuildUser(user);
            guildMemberService.saveGuildMember(guildMember);
//            discordGuild.addGuildMember(guildMember);
        });

        guild.getRoles().forEach(role -> {
            DiscordRole discordRole = new DiscordRole(role, discordGuild);
            roleService.saveRole(discordRole);
//            discordGuild.addRole(discordRole);
        });
    }

    //TODO протестировать
    @Override
    @SubscribeEvent
    public void onGuildLeave(GuildLeaveEvent event) {
        Guild guild = event.getGuild();
        DiscordGuild discordGuild = guildService.findGuildById(guild.getId()).orElse(new DiscordGuild(guild));
        guildService.deleteGuild(discordGuild);
    }
}
