package com.github.gypsyjr777.discordmanager.config.command;

import lombok.Getter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public enum SlashCommand {
        LEAVE_TIMER(Commands
                        .slash("leavetimer", "Adding roles to protect against AFK")
                        .setGuildOnly(true)
                        .addOption(OptionType.ROLE, "role", "Role for protecting", true),
                        "leavetimer"),
        LEAVE_TIMER_ON(Commands
                        .slash("leavetimer_on", "On afk timer")
                        .setGuildOnly(true),
                        "leavetimer_on"),
        LEAVE_TIMER_OFF(Commands
                        .slash("leavetimer_off", "Off afk timer")
                        .setGuildOnly(true),
                        "leavetimer_off"),
        ANNOUNCEMENT(Commands
                        .slash("announce", "Publish an announce")
                        .setGuildOnly(true)
                        .addOption(OptionType.STRING, "title", "Announce title", true)
                        .addOption(OptionType.STRING, "text", "Announce text", true)
                        .addOption(OptionType.CHANNEL, "channel", "Channel for publishing", false),
                        "announce"),
        REACTION_ROLE_ADD(Commands
                        .slash("reactionrole_add", "Create new reaction for role")
                        .setGuildOnly(true)
                        .addOption(OptionType.STRING, "message_id", "Message id", true)
                        .addOption(OptionType.STRING, "reaction", "Emoji reaction", true)
                        .addOption(OptionType.ROLE, "role", "Role", true),
                        "reactionrole_add"),
        REACTION_ROLE_TEXT(Commands
                        .slash("reactionrole_text", "Create new reaction for role")
                        .setGuildOnly(true)
                        .addOption(OptionType.STRING, "text", "Announce text", true)
                        .addOption(OptionType.CHANNEL, "channel", "Channel for publishing", true)
                        .addOption(OptionType.STRING, "title", "Announce title", false),
                        "reactionrole_text"),
        MEMBER_LOG_ON(Commands
                        .slash("memberslog_on", "Turn on members logging")
                        .setGuildOnly(true)
                        .addOption(OptionType.CHANNEL, "channel", "Channel for publishing", true),
                        "memberslog_on"),
        MEMBER_LOG_OFF(Commands
                        .slash("memberslog_off", "Turn off members logging")
                        .setGuildOnly(true),
                        "memberslog_off"),
        GUILD_LOG_ON(Commands
                        .slash("guildlog_on", "Turn on guild logging")
                        .setGuildOnly(true)
                        .addOption(OptionType.CHANNEL, "channel", "Channel for publishing", true),
                        "guildlog_on"),
        GUILD_LOG_OFF(Commands
                        .slash("guildlog_off", "Turn off guild logging")
                        .setGuildOnly(true),
                        "guildlog_off"),
        DEFAULT_ROLE(Commands
                .slash("default_role", "Set default role. To enable it, fill in the \"role\" field. To disable it, leave it empty.")
                .addOption(OptionType.ROLE, "role", "Role", false)
                .setGuildOnly(true),
                "default_role"),
        LEVEL(Commands
                        .slash("level", "Information about member's level")
                        .setGuildOnly(true),
                        "level"),
        GENERATE_IMAGE(Commands
                .slash("generate_image", "Kandinsky generate an image")
                .addOption(OptionType.STRING, "prompt", "Prompt to render", true)
                .setGuildOnly(true),
                "generate_image");

        private final SlashCommandData slashCommandData;
        private final String command;

        SlashCommand(SlashCommandData slashCommandData, String command) {
                this.slashCommandData = slashCommandData;
                this.command = command;
        }

        public static List<SlashCommandData> getSlashCommand() {
                List<SlashCommandData> slashCommandDataList = new ArrayList<>();
                Arrays.stream(SlashCommand.values()).forEach(it -> {
                        slashCommandDataList.add(it.slashCommandData);
                });

                return slashCommandDataList;
        }
}
