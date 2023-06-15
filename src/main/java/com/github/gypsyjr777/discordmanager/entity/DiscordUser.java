package com.github.gypsyjr777.discordmanager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
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
    Set<GuildMember> guildMembers;
}
