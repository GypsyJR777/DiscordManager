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
            .addOption(OptionType.ROLE, "role", "Role for protecting", true),
            "leavetimer"),
    ANNOUNCEMENT(Commands
            .slash("announce", "Publish an announce")
            .addOption(OptionType.STRING, "title", "Announce title", true)
            .addOption(OptionType.STRING, "text", "Announce text", true)
            .addOption(OptionType.CHANNEL, "channel", "Channel for publishing", false),
            "announce");

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