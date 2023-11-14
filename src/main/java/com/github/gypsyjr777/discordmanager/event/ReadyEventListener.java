package com.github.gypsyjr777.discordmanager.event;

import com.github.gypsyjr777.discordmanager.config.command.SlashCommand;
import com.github.gypsyjr777.discordmanager.entity.*;
import com.github.gypsyjr777.discordmanager.service.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReadyEventListener extends ListenerAdapter {
    private final GuildService guildService;
    private final GuildMemberService guildMemberService;
    private final UserService userService;
    private final RoleService roleService;
    private final UserRoleService userRoleService;

    private Logger log;

    public ReadyEventListener(ApplicationContext context) {
        this.guildService = context.getBean(GuildService.class);
        this.guildMemberService = context.getBean(GuildMemberService.class);
        this.userService = context.getBean(UserService.class);
        this.roleService = context.getBean(RoleService.class);
        this.userRoleService = context.getBean(UserRoleService.class);
        this.log = LogManager.getLogger(ReadyEventListener.class);
    }

    @Override
    @SubscribeEvent
    public void onReady(ReadyEvent event) {
        log.info("Bot is ready!");
        runAfterStartup(event.getJDA());
    }

    private void runAfterStartup(JDA jda) {
        jda.getGuilds().forEach(guild -> {
            DiscordGuild discordGuild = guildService.findGuildById(guild.getId()).orElse(new DiscordGuild(guild));
            guildService.saveGuild(discordGuild);

            guild.getRoles().forEach(role -> {
                roleService.saveRole(new DiscordRole(role, discordGuild));
            });

            List<Member> members = guild.getMembers();
            members.forEach(member -> {
                DiscordUser user = userService.findByIdDiscordUser(member.getUser().getId()).orElse(new DiscordUser(member.getUser()));
                GuildMember guildMember = guildMemberService.findGuildMemberByMemberAndGuild(user, discordGuild).orElse(new GuildMember(user, discordGuild));

                if (guildMember.getLastOut() == null) {
                    guildMember.setLastOut(LocalDateTime.now());
                }

                userService.saveGuildUser(user);
                guildMemberService.saveGuildMember(guildMember);

                member.getRoles().forEach(role -> {
                    DiscordRole discordRole = roleService.findRoleById(role.getId()).orElseThrow();
                    UserRole userRole = userRoleService.getByRoleAndUser(discordRole, user).orElse(new UserRole(discordRole, user));

                    userRoleService.saveUserRole(userRole);
                });
            });

        });

        jda.updateCommands().addCommands(
                SlashCommand.getSlashCommand()
        ).queue();
    }
}



