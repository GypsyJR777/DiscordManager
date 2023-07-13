package com.github.gypsyjr777.discordmanager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "guild_member", uniqueConstraints = { @UniqueConstraint(columnNames = { "member_id", "guild_id" }) } )
@Getter
@Setter
@NoArgsConstructor
public class GuildMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "member_id")
    private DiscordUser member;

    @NotNull
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
}
