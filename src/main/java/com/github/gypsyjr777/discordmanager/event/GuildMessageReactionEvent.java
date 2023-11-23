package com.github.gypsyjr777.discordmanager.event;

import com.github.gypsyjr777.discordmanager.entity.DiscordGuild;
import com.github.gypsyjr777.discordmanager.entity.DiscordRole;
import com.github.gypsyjr777.discordmanager.exception.NullRoleException;
import com.github.gypsyjr777.discordmanager.service.GuildService;
import com.github.gypsyjr777.discordmanager.service.RoleService;
import com.github.gypsyjr777.discordmanager.utils.BasicUtils;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class GuildMessageReactionEvent extends ListenerAdapter {
    private final GuildService guildService;
    private final RoleService roleService;
    private final BasicUtils utils;

    public GuildMessageReactionEvent(ApplicationContext context) {
        this.guildService = context.getBean(GuildService.class);
        this.roleService = context.getBean(RoleService.class);
        this.utils = context.getBean(BasicUtils.class);
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
