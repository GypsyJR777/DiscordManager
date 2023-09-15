package com.github.gypsyjr777.discordmanager.utils;

import com.github.gypsyjr777.discordmanager.entity.DiscordGuild;
import com.github.gypsyjr777.discordmanager.entity.DiscordRole;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;

public class CheckLeaveTimer {
    public static boolean checkLeaveTimerMember(Member member, List<DiscordRole> roles, DiscordGuild guild) {
        if (guild.isHaveLeaveTimer()) {
            List<String> leaveTimerIds = roles.stream().map(DiscordRole::getId).toList();
            return member.getRoles().stream().anyMatch(it -> leaveTimerIds.contains(it.getId()));
        }

        return true;
    }
}
