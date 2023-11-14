package com.github.gypsyjr777.discordmanager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "guild_member", uniqueConstraints = { @UniqueConstraint(columnNames = { "member_id", "guild_id" }) } )
@Getter
@Setter
@NoArgsConstructor
public class GuildMember {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "member_id")
    private DiscordUser member;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "guild_id")
    private DiscordGuild guild;

    @Column(name = "last_out")
    private LocalDateTime lastOut;

    @Column(nullable = true, name = "is_leave_timer")
    private boolean isLeaveTimer;

    @Column(nullable = false, columnDefinition = "int default 1", name = "level")
    private int level;


    @Column(nullable = false, columnDefinition = "bigint default 0", name = "xp")
    private long xp;

    @Column(nullable = true, columnDefinition = "double precision default 0", name = "voice_time")
    private double voiceTime = 0.0;

    @Column(name = "last_voice_time")
    private LocalDateTime lastVoiceTime;

    public GuildMember(DiscordUser member, DiscordGuild guild) {
        this.member = member;
        this.guild = guild;
    }

    public void addVoiceTime(long between) {
        voiceTime += between / 60.0;
        addXp(between / 5);
    }

    public void addXp(long someXp) {
        xp += someXp;
        if (xp >= level * 500L) {
            xp -= level * 500L;
            level++;
            addXp(0);
        }
    }
}
