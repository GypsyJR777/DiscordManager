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
            "reactionrole_add"
    );

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
