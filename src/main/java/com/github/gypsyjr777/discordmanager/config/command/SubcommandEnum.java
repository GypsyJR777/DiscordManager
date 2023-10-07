package com.github.gypsyjr777.discordmanager.config.command;

import lombok.Getter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum SubcommandEnum {
    LEAVE_TIMER(new SubcommandData("role", "Adding roles to protect against AFK")
            .addOption(OptionType.ROLE, "role", "Role for protecting", true),
            "leavetimer role"),
    LEAVE_TIMER_ON(new SubcommandData("on", "On afk timer"), "leavetimer on"),
    LEAVE_TIMER_OFF(new SubcommandData("off", "Off afk timer"), "leavetimer off"),

    REACTION_ROLE_ADD(new SubcommandData("add", "Create new reaction for role")
            .addOption(OptionType.STRING, "message_id", "Message id", true)
            .addOption(OptionType.STRING, "reaction", "Emoji reaction", true)
            .addOption(OptionType.ROLE, "role", "Role", true),
            "reactionrole add"),
    REACTION_ROLE_TEXT(new SubcommandData("text", "Create new message for role")
            .addOption(OptionType.STRING, "text", "Announce text", true)
            .addOption(OptionType.CHANNEL, "channel", "Channel for publishing", true)
            .addOption(OptionType.STRING, "title", "Announce title", false),
            "reactionrole text"),
    REACTION_ROLE_MESSAGE(new SubcommandData("text", "Create new message with roles")
            .addOption(OptionType.STRING, "text", "Announce text", true)
            .addOption(OptionType.CHANNEL, "channel", "Channel for publishing", true)
            .addOption(OptionType.STRING, "title", "Announce title", false),
            "reactionrole text"),

    MEMBER_LOG_ON(new SubcommandData("on", "Turn on members logging")
            .addOption(OptionType.CHANNEL, "channel", "Channel for publishing", true),
            "memberslog on"),
    MEMBER_LOG_OFF(new SubcommandData("off", "Turn off members logging"),
            "memberslog off"),

    GUILD_LOG_ON(new SubcommandData("on", "Turn on guild logging")
            .addOption(OptionType.CHANNEL, "channel", "Channel for publishing", true),
            "guildlog on"),
    GUILD_LOG_OFF(new SubcommandData("off", "Turn off guild logging"),
            "guildlog off");

    private final SubcommandData subcommandData;
    private final String command;

    SubcommandEnum(SubcommandData subcommandData, String command) {
        this.subcommandData = subcommandData;
        this.command = command;
    }

    public static List<SubcommandData> listOfLeaveTimers() {
        List<SubcommandData> dataList = new ArrayList<>();

        dataList.add(LEAVE_TIMER.subcommandData);
        dataList.add(LEAVE_TIMER_ON.subcommandData);
        dataList.add(LEAVE_TIMER_OFF.subcommandData);

        return dataList;
    }

    public static List<SubcommandData> listOfReactionRoles() {
        List<SubcommandData> dataList = new ArrayList<>();

        dataList.add(REACTION_ROLE_ADD.subcommandData);
        dataList.add(REACTION_ROLE_TEXT.subcommandData);

        return dataList;
    }

    public static List<SubcommandData> listOfMemberLogs() {
        List<SubcommandData> dataList = new ArrayList<>();

        dataList.add(MEMBER_LOG_ON.subcommandData);
        dataList.add(MEMBER_LOG_OFF.subcommandData);

        return dataList;
    }

    public static List<SubcommandData> listOfGuildLogs() {
        List<SubcommandData> dataList = new ArrayList<>();

        dataList.add(GUILD_LOG_ON.subcommandData);
        dataList.add(GUILD_LOG_OFF.subcommandData);

        return dataList;
    }
}
