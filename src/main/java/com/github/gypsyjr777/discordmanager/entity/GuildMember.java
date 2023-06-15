package com.github.gypsyjr777.discordmanager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "discord_user")
@Getter
@Setter
@NoArgsConstructor
public class GuildMember {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    DiscordUser member;

    @ManyToOne
    @JoinColumn(name = "guild_id")
    DiscordGuild guild;

    private LocalDateTime lastOut;
    private boolean isVip;
}
