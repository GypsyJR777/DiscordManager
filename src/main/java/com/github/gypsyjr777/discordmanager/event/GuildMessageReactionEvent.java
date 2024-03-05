package com.github.gypsyjr777.discordmanager.event;

import com.github.gypsyjr777.discordmanager.entity.DiscordGuild;
import com.github.gypsyjr777.discordmanager.entity.DiscordRole;
import com.github.gypsyjr777.discordmanager.exception.NullRoleException;
import com.github.gypsyjr777.discordmanager.service.GuildService;
import com.github.gypsyjr777.discordmanager.service.RoleService;
import com.github.gypsyjr777.discordmanager.service.BasicUtilsService;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GuildMessageReactionEvent extends ListenerAdapter {
    private final GuildService guildService;
    private final RoleService roleService;
    private final BasicUtilsService utils;

    @Autowired
    public GuildMessageReactionEvent(GuildService guildService, RoleService roleService, BasicUtilsService utils) {
        this.guildService = guildService;
        this.roleService = roleService;
        this.utils = utils;
    }

    @Override
    @SubscribeEvent
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        DiscordGuild guild = guildService.findGuildById(event.getGuild().getId())
                .orElseGet(() -> utils.createDiscordGuild(event.getGuild()));

        if (guild.getMessageId() != null && !guild.getMessageId().isBlank() && guild.getMessageId().equals(event.getMessageId())) {
            DiscordRole discordRole = roleService.findRoleByReaction(event.getReaction().getEmoji().getFormatted())
                    .orElseThrow(() -> {
                        event.retrieveMessage().complete().editMessage("Empty role action message").queue();
                        return new NullRoleException("NULL");
                    });

            event.getGuild().addRoleToMember(event.getUser(), event.getGuild().getRoleById(discordRole.getId())).queue();
        }
    }
}
