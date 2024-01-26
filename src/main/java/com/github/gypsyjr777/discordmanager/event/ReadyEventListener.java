package com.github.gypsyjr777.discordmanager.event;

import com.github.gypsyjr777.discordmanager.config.command.SlashCommand;
import com.github.gypsyjr777.discordmanager.entity.*;
import com.github.gypsyjr777.discordmanager.service.*;
import com.github.gypsyjr777.discordmanager.utils.BasicUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ReadyEventListener extends ListenerAdapter {
    private final GuildService guildService;
    private final RoleService roleService;
    private final BasicUtils utils;

    private Logger log;

    public ReadyEventListener(ApplicationContext context) {
        this.guildService = context.getBean(GuildService.class);
        this.roleService = context.getBean(RoleService.class);
        this.utils = context.getBean(BasicUtils.class);
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
            DiscordGuild discordGuild = guildService.findGuildById(guild.getId()).orElseGet(() -> new DiscordGuild(guild));
            guildService.saveGuild(discordGuild);

            guild.getRoles().forEach(role -> {
                DiscordRole discordRole = roleService.findRoleById(role.getId())
                        .orElseGet(() -> new DiscordRole(role, discordGuild));
                roleService.saveRole(discordRole);
            });

            utils.addMembersFromGuild(guild, discordGuild);
        });

        jda.updateCommands().addCommands(
                SlashCommand.getSlashCommand()
        ).queue();
    }
}



