package com.github.gypsyjr777.discordmanager.service;

import com.github.gypsyjr777.discordmanager.entity.DiscordGuild;
import com.github.gypsyjr777.discordmanager.entity.DiscordUser;
import com.github.gypsyjr777.discordmanager.entity.GuildMember;
import com.github.gypsyjr777.discordmanager.repository.GuildMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GuildMemberService {
    @Autowired
    private GuildMemberRepository guildMemberRepository;

    public void saveGuildMember(GuildMember guildMember) {
        guildMemberRepository.save(guildMember);
    }

    public void deleteGuildUser(DiscordUser user, DiscordGuild discordGuild) {
        guildMemberRepository.deleteByGuildAndMember(discordGuild, user);
    }

    public void updateMembersInfoRoles(DiscordUser user, DiscordGuild guild) {
        GuildMember guildMember = findGuildMemberByMemberAndGuild(user, guild).orElseThrow();
        guildMember.setLeaveTimer(true);
        saveGuildMember(guildMember);
    }

    public Optional<GuildMember> findGuildMemberByMemberAndGuild(DiscordUser user, DiscordGuild guild) {
        return guildMemberRepository.findGuildMemberByMemberAndGuild(user, guild);
    }

    public List<GuildMember> getAllGuildMembers(DiscordGuild guild) {
        return guildMemberRepository.findAllByGuild(guild);
    }

    public void deleteGuildMember(GuildMember member) {
        guildMemberRepository.delete(member);
    }
}
