package com.github.gypsyjr777.discordmanager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "guild_member")
@Getter
@Setter
@NoArgsConstructor
public class GuildMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private DiscordUser member;

    @ManyToOne
    @JoinColumn(name = "guild_id")
    private DiscordGuild guild;

    private LocalDateTime lastOut;

    @Column(nullable = true)
    private boolean isLeaveTimer;

    public GuildMember(DiscordUser member, DiscordGuild guild) {
        this.member = member;
        this.guild = guild;
    }

    public GuildMember(DiscordUser member) {
        this.member = member;
    }
}
