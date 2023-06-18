package com.github.gypsyjr777.discordmanager.utils;

import com.github.gypsyjr777.discordmanager.entity.DiscordGuild;
import net.dv8tion.jda.api.entities.Member;

import java.util.ArrayList;
import java.util.List;

public class CheckLeaveTimer {
    public static boolean checkLeaveTimerMember(Member member, DiscordGuild guild) {
        if (guild.isHaveLeaveTimer()) {
            List<String> leaveTimerIds = guild.getLeaveTimerIds();
            return member.getRoles().stream().anyMatch(it -> leaveTimerIds.contains(it.getId()));
        }

        return true;
    }
}
