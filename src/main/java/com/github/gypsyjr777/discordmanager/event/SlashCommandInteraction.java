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
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.internal.JDAImpl;
import org.springframework.stereotype.Service;

import java.util.Objects;

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
            } else if (event.getName().equals(SlashCommand.REACTION_ROLE_ADD.getCommand())) {
                addReactionRole(event);
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

            textChannel
                    .sendMessage("")
                    .setEmbeds(createEmbedMessage(event.getOption("title").getAsString().replace("\\n", "\n"), event.getOption("text").getAsString().replace("\\n", "\n")).build())
                    .queue();
            event.reply("The announcement is published").queue();
        } else {
            event.reply("For this action, you need administrator rights").queue();
        }
    }

    private void leaveTimer(SlashCommandInteractionEvent event) {
        if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            DiscordGuild guild = guildService.findGuildById(event.getGuild().getId()).orElseThrow();
            guild.setHaveLeaveTimer(true);

            DiscordRole role = new DiscordRole(event.getOption("role").getAsRole(), guild);
            role.setVip(true);
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

    private void addReactionRole(SlashCommandInteractionEvent event) {
        if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            DiscordGuild guild = guildService.findGuildById(event.getGuild().getId()).orElseThrow();
            DiscordRole role = roleService.findRoleById(event.getOption("role").getAsRole().getId()).orElse(new DiscordRole(event.getOption("role").getAsRole(), guild));
            String reaction = event.getOption("reaction").getAsString();
            String messageId = event.getOption("message_id").getAsString();

            for (TextChannel textChannel : event.getGuild().getTextChannels()) {
                if (textChannel.getIterableHistory().stream().anyMatch(message -> message.getId().equals(messageId))) {
                    textChannel.retrieveMessageById(messageId).queue(message -> message.addReaction(Emoji.fromFormatted(reaction)).queue());
                    role.setReaction(reaction);
                    role.setMessageId(messageId);
                    roleService.saveRole(role);
                    break;
                }
            }

            event.reply("Reaction added").queue();
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
