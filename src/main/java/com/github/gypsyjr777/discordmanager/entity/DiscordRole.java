package com.github.gypsyjr777.discordmanager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Role;

@Entity
@Table(name = "discord_role")
@Getter
@Setter
@NoArgsConstructor
public class DiscordRole {
    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne
    @JoinColumn(name = "guild_id", nullable = false)
    private DiscordGuild guild;

    @Column(nullable = true, name = "reaction")
    private String reaction;

    @Column(nullable = true, columnDefinition = "boolean default false", name = "vip")
    private boolean vip;

    @Column(nullable = true, columnDefinition = "boolean default false", name = "basic")
    private boolean basic;

    public DiscordRole(Role role, DiscordGuild guild) {
        id = role.getId();
        this.guild = guild;
    }
}
