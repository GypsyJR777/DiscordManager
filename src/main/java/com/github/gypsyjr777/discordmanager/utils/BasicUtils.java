package com.github.gypsyjr777.discordmanager.utils;

import com.github.gypsyjr777.discordmanager.entity.*;
import com.github.gypsyjr777.discordmanager.service.*;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class BasicUtils {

    @Autowired
    private UserService userService;

    @Autowired
    private GuildMemberService guildMemberService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private GuildService guildService;
    public static boolean checkLeaveTimerMember(Member member, List<DiscordRole> roles, DiscordGuild guild) {
        if (guild.isHaveLeaveTimer()) {
            List<String> leaveTimerIds = roles.stream().map(DiscordRole::getId).toList();
            return member.getRoles().stream().anyMatch(it -> leaveTimerIds.contains(it.getId()));
        }

        return true;
    }

    public void addMembersFromGuild(Guild guild, DiscordGuild discordGuild) {
        guild.getMembers().forEach(member -> {
            DiscordUser user = userService.findByIdDiscordUser(member.getUser().getId()).orElseGet(() -> new DiscordUser(member.getUser()));
            GuildMember guildMember = guildMemberService.findGuildMemberByMemberAndGuild(user, discordGuild).orElseGet(() -> new GuildMember(user, discordGuild));

            if (guildMember.getLastOut() == null) {
                guildMember.setLastOut(LocalDateTime.now());
            }

            userService.saveGuildUser(user);
            guildMemberService.saveGuildMember(guildMember);

            member.getRoles().forEach(role -> {
                DiscordRole discordRole = roleService.findRoleById(role.getId()).orElseGet(() -> {
                    DiscordRole newRole = new DiscordRole(role, discordGuild);
                    roleService.saveRole(newRole);
                    return newRole;
                });

                UserRole userRole = userRoleService.getByRoleAndUser(discordRole, user).orElseGet(() -> new UserRole(discordRole, user));
                userRoleService.saveUserRole(userRole);
            });
        });
    }

    public DiscordGuild createDiscordGuild(Guild guild) {
        DiscordGuild newGuild = new DiscordGuild(guild);
        guildService.saveGuild(newGuild);
        addMembersFromGuild(guild, newGuild);

        return newGuild;
    }

    public DiscordUser createDiscordUser(User user) {
        DiscordUser discordUser = new DiscordUser(user);
        userService.saveGuildUser(discordUser);
        return discordUser;
    }

    public GuildMember createGuildMember(DiscordUser discordUser, DiscordGuild discordGuild) {
        GuildMember guildMember = new GuildMember(discordUser, discordGuild);
        guildMemberService.saveGuildMember(guildMember);
        return guildMember;
    }
}
