package com.github.gypsyjr777.discordmanager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Member;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "discord_user")
@Getter
@Setter
@NoArgsConstructor
public class DiscordUser {
    @Id
    private String id;

    private String username;

    @OneToMany(mappedBy = "member")
    Set<GuildMember> guildMembers = new HashSet<>();

    public DiscordUser(Member member) {
        id = member.getId();
        username = member.getUser().getName();
    }

    public void setGuildMember(GuildMember guildMember) {
        guildMembers.add(guildMember);
    }
}
