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
            .addSubcommands(SubcommandEnum.listOfLeaveTimers())
            .setGuildOnly(true),
            "leavetimer"),
    ANNOUNCEMENT(Commands
            .slash("announce", "Publish an announce")
            .setGuildOnly(true)
            .addOption(OptionType.STRING, "title", "Announce title", true)
            .addOption(OptionType.STRING, "text", "Announce text", true)
            .addOption(OptionType.CHANNEL, "channel", "Channel for publishing", false),
            "announce"),
    REACTION_ROLE(Commands
            .slash("reactionrole", "Create a reaction for role")
            .addSubcommands(SubcommandEnum.listOfReactionRoles())
            .setGuildOnly(true),
            "reactionrole"),
    MEMBER_LOG(Commands
            .slash("memberslog", "Turn on members logging")
            .addSubcommands(SubcommandEnum.listOfMemberLogs())
            .setGuildOnly(true),
            "memberslog"),
    GUILD_LOG(Commands
            .slash("guildlog", "Turn on guild logging")
            .addSubcommands(SubcommandEnum.listOfGuildLogs())
            .setGuildOnly(true),
            "guildlog"),
    DEFAULT_ROLE(Commands
            .slash("defaultrole", "Set default role. To enable it, fill in the \"role\" field. To disable it, leave it empty.")
            .addOption(OptionType.ROLE, "role", "Role", false)
            .setGuildOnly(true),
            "defaultrole"),
    LEVEL(Commands
            .slash("level", "Information about member's level")
            .setGuildOnly(true),
            "level"),
    GENERATE_IMAGE(Commands
            .slash("generateimage", "Kandinsky generate an image")
            .addOption(OptionType.STRING, "prompt", "Prompt to render", true)
            .setGuildOnly(true),
            "generateimage"),
    ABOUT_BOT(Commands
            .slash("about", "Information bot")
            .setGuildOnly(false),
            "about"),
    HELP(Commands
            .slash("help", "Command list")
            .setGuildOnly(false),
            "help");

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
