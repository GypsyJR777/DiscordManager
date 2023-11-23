package com.github.gypsyjr777.discordmanager.event;

import com.github.gypsyjr777.discordmanager.entity.*;
import com.github.gypsyjr777.discordmanager.exception.NullGuildException;
import com.github.gypsyjr777.discordmanager.exception.NullRoleException;
import com.github.gypsyjr777.discordmanager.service.*;
import com.github.gypsyjr777.discordmanager.utils.BasicUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private final BasicUtils utils;
    private final Logger log;

    public NewGuildEvent(ApplicationContext context) {
        this.userService = context.getBean(UserService.class);
        this.guildService = context.getBean(GuildService.class);
        this.memberService = context.getBean(GuildMemberService.class);
        this.roleService = context.getBean(RoleService.class);
        this.userRoleService = context.getBean(UserRoleService.class);
        this.utils = context.getBean(BasicUtils.class);
        this.log = LogManager.getLogger(NewGuildEvent.class);
    }

    @Override
    @SubscribeEvent
    @Transactional
    public void onGuildJoin(GuildJoinEvent event) {
        Guild guild = event.getGuild();
        DiscordGuild discordGuild = guildService.findGuildById(guild.getId()).orElse(new DiscordGuild(guild));
        log.info("New guild {} added bot", guild.getName());
        guildService.saveGuild(discordGuild);
        utils.addMembersFromGuild(guild, discordGuild);
    }

    //TODO протестировать
    @Override
    @SubscribeEvent
    public void onGuildLeave(GuildLeaveEvent event) {
        Guild guild = event.getGuild();
        log.info("Guild {} removed bot", guild.getName());
        DiscordGuild discordGuild = guildService.findGuildById(guild.getId())
                .orElseThrow(() -> new NullGuildException(guild.getName()));

        guildService.deleteGuild(discordGuild);
    }

    @Override
    @SubscribeEvent
    public void onRoleCreate(RoleCreateEvent event) {
        DiscordGuild discordGuild = guildService.findGuildById(event.getGuild().getId())
                .orElse(utils.createDiscordGuild(event.getGuild()));

        DiscordRole discordRole = new DiscordRole(event.getRole(), discordGuild);
        roleService.saveRole(discordRole);
    }

    @Override
    @SubscribeEvent
    public void onRoleDelete(RoleDeleteEvent event) {
        DiscordRole discordRole = roleService.findRoleById(event.getRole().getId())
                .orElseThrow(() -> new NullRoleException(event.getRole().getName()));

        if (discordRole.isVip()) {
            deleteRole(userRoleService.getAllByRole(discordRole), guildService.findGuildById(event.getGuild().getId())
                    .orElseThrow(() -> new NullGuildException(event.getGuild().getName())));
        }

        roleService.deleteRole(discordRole);
    }

    @Override
    @SubscribeEvent
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
        event.getRoles().forEach(role -> {
            DiscordGuild discordGuild = guildService.findGuildById(event.getGuild().getId())
                    .orElse(utils.createDiscordGuild(event.getGuild()));

            DiscordRole discordRole = roleService.findRoleById(role.getId())
                    .orElseGet(() -> {
                        DiscordRole newRole = new DiscordRole(role, discordGuild);
                        roleService.saveRole(newRole);
                        return newRole;
                    });
            DiscordUser discordUser = userService.findByIdDiscordUser(event.getUser().getId())
                    .orElse(utils.createDiscordUser(event.getUser()));

            UserRole userRole = new UserRole(discordRole, discordUser);

            if (discordRole.isVip()) {
                GuildMember guildMember = memberService.findGuildMemberByMemberAndGuild(discordUser, discordGuild)
                        .orElse(utils.createGuildMember(discordUser, discordGuild));

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
            DiscordRole discordRole = roleService.findRoleById(role.getId()).orElseThrow(() -> new NullRoleException(role.getName()));

            deleteRole(userRoleService.getAllByRole(discordRole), guildService.findGuildById(event.getGuild().getId())
                    .orElseThrow(() -> new NullGuildException(event.getGuild().getName()))
            );
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
