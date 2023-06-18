package com.github.gypsyjr777.discordmanager.event;

import com.github.gypsyjr777.discordmanager.config.command.SlashCommand;
import com.github.gypsyjr777.discordmanager.entity.DiscordGuild;
import com.github.gypsyjr777.discordmanager.entity.DiscordRole;
import com.github.gypsyjr777.discordmanager.entity.GuildMember;
import com.github.gypsyjr777.discordmanager.service.GuildMemberService;
import com.github.gypsyjr777.discordmanager.service.GuildService;
import com.github.gypsyjr777.discordmanager.service.RoleService;
import com.github.gypsyjr777.discordmanager.service.UserService;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SlashCommandInteraction extends ListenerAdapter {
    private final GuildService guildService;
    private final GuildMemberService guildMemberService;
    private final UserService userService;
    private final RoleService roleService;

    public SlashCommandInteraction(GuildService guildService, GuildMemberService guildMemberService, UserService userService, RoleService roleService) {
        this.guildService = guildService;
        this.guildMemberService = guildMemberService;
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    @SubscribeEvent
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.isFromGuild()) {
            if (event.getName().equals(SlashCommand.LEAVE_TIMER.getCommand())) {
                leaveTimer(event);
            } else if (event.getName().equals(SlashCommand.ANNOUNCEMENT.getCommand())) {
                announce(event);
            }
        }
    }

    private void announce(SlashCommandInteractionEvent event) {
        if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            TextChannel textChannel;
            if (event.getOption("channel") == null) {
                textChannel = event.getChannel().asTextChannel();
            } else {
                textChannel = event.getOption("channel").getAsChannel().asTextChannel();
            }

            event.reply("The announcement is published").queue();
            textChannel
                    .sendMessage("")
                    .setEmbeds(createEmbedMessage(event.getOption("title").getAsString(), event.getOption("text").getAsString()).build())
                    .queue();
        } else {
            event.reply("For this action, you need administrator rights").queue();
        }
    }

    private void leaveTimer(SlashCommandInteractionEvent event) {
        if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            DiscordGuild guild = guildService.findGuildById(event.getGuild().getId()).orElseThrow();
            guild.setHaveLeaveTimer(true);

            DiscordRole role = new DiscordRole(event.getOption("role").getAsRole());
            roleService.saveRole(role);

            guild.addRole(role);
            guildService.saveGuild(guild);

            event.getGuild().getMembers().forEach(member -> {
                if (member.getRoles().stream().anyMatch(r -> r.getId().equals(role.getId()))) {
                    GuildMember guildMember = guildMemberService.findGuildMemberByMemberAndGuild(
                            userService.findByIdDiscordUser(member.getUser().getId()).orElseThrow(), guild
                    ).orElseThrow();

                    guildMember.setLeaveTimer(true);

                    guildMemberService.saveGuildMember(guildMember);
                }
            });

            event.reply("Role added").queue();
        } else {
            event.reply("For this action, you need administrator rights").queue();
        }
    }

    private EmbedBuilder createEmbedMessage(String title, String text) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(title);
        eb.setDescription(text);

        return eb;
    }
}
