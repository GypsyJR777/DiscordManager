package com.github.gypsyjr777.discordmanager.repository;

import com.github.gypsyjr777.discordmanager.entity.DiscordGuild;
import com.github.gypsyjr777.discordmanager.entity.DiscordUser;
import com.github.gypsyjr777.discordmanager.entity.GuildMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuildMemberRepository extends JpaRepository<GuildMember, Long> {
    Optional<GuildMember> findGuildMemberByMemberAndGuild(DiscordUser member, DiscordGuild guild);
    List<GuildMember> findAllByGuild(DiscordGuild guild);

    void deleteByGuildAndMember(DiscordGuild guild, DiscordUser member);

    List<GuildMember> findAllByMember(DiscordUser member);
}
