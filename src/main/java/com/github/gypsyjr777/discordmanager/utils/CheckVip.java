package com.github.gypsyjr777.discordmanager.utils;

import com.github.gypsyjr777.discordmanager.entity.DiscordGuild;
import net.dv8tion.jda.api.entities.Member;

import java.util.ArrayList;
import java.util.List;

public class CheckVip {
    private static final List<String> vipId = new ArrayList<>();
    static {
        vipId.add("1117852291165327371");
        vipId.add("1046181525529563277");
        vipId.add("1030617301382348922");
    }

    public static boolean checkVipMember(Member member, DiscordGuild guild) {
        if (guild.isHaveVips()) {
            List<String> vipIds = guild.getVipIds();
            return member.getRoles().stream().anyMatch(it -> vipIds.contains(it.getId()));
        }

        return true;
    }
}
