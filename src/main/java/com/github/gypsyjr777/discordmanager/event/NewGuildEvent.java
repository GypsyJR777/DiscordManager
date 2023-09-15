package com.github.gypsyjr777.discordmanager.event;

import com.github.gypsyjr777.discordmanager.entity.*;
import com.github.gypsyjr777.discordmanager.service.*;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class NewGuildEvent extends ListenerAdapter {
    private final UserService userService;
    private final GuildService guildService;
    private final GuildMemberService memberService;
    private final RoleService roleService;
    private final UserRoleService userRoleService;

    public NewGuildEvent(ApplicationContext context) {
        this.userService = context.getBean(UserService.class);
        this.guildService = context.getBean(GuildService.class);
        this.memberService = context.getBean(GuildMemberService.class);
        this.roleService = context.getBean(RoleService.class);
        this.userRoleService = context.getBean(UserRoleService.class);
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
            GuildMember guildMember = memberService.findGuildMemberByMemberAndGuild(user, discordGuild).orElse(new GuildMember(user, discordGuild));

            if (guildMember.getLastOut() == null) {
                guildMember.setLastOut(LocalDateTime.now());
            }

            userService.saveGuildUser(user);
            memberService.saveGuildMember(guildMember);
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
        DiscordGuild discordGuild = guildService.findGuildById(guild.getId()).orElseThrow();
        guildService.deleteGuild(discordGuild);
    }

    @Override
    @SubscribeEvent
    public void onRoleCreate(RoleCreateEvent event) {
        DiscordGuild discordGuild = guildService.findGuildById(event.getGuild().getId()).orElseThrow();
        DiscordRole discordRole = new DiscordRole(event.getRole(), discordGuild);

        roleService.saveRole(discordRole);
    }

    @Override
    @SubscribeEvent
    public void onRoleDelete(RoleDeleteEvent event) {
        DiscordRole discordRole = roleService.findRoleById(event.getRole().getId()).orElseThrow();

        if (discordRole.isVip()) {
            deleteRole(userRoleService.getAllByRole(discordRole), guildService.findGuildById(event.getGuild().getId()).orElseThrow());
        }

        roleService.deleteRole(discordRole);
    }

    @Override
    @SubscribeEvent
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
        event.getRoles().forEach(role -> {
            DiscordRole discordRole = roleService.findRoleById(role.getId()).orElseThrow();
            DiscordUser discordUser = userService.findByIdDiscordUser(event.getUser().getId()).orElseThrow();

            UserRole userRole = new UserRole(discordRole, discordUser);

            if (discordRole.isVip()) {
                GuildMember guildMember = memberService.findGuildMemberByMemberAndGuild(discordUser, guildService.findGuildById(event.getGuild().getId()).orElseThrow()).orElseThrow();
                guildMember.setLeaveTimer(true);
                memberService.saveGuildMember(guildMember);
            }
            userRoleService.saveUserRole(userRole);
        });
    }

    @Override
    @SubscribeEvent
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
        event.getRoles().forEach(role -> {
            DiscordRole discordRole = roleService.findRoleById(role.getId()).orElseThrow();

            deleteRole(userRoleService.getAllByRole(discordRole), guildService.findGuildById(event.getGuild().getId()).orElseThrow());
        });
    }

    private void deleteRole(Set<UserRole> userRoles, DiscordGuild guild) {
        userRoles.forEach(userRole -> {
            DiscordUser user = userRole.getUser();
            if (userRoleService.getAllByUser(user).stream().noneMatch(it -> it.getRole().isVip())) {
                GuildMember guildMember = memberService.findGuildMemberByMemberAndGuild(user, guild).orElseThrow();
                guildMember.setLeaveTimer(false);
                memberService.saveGuildMember(guildMember);
            }

            userRoleService.deleteUserRole(userRole);
        });
    }
}
