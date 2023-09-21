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
    private String id;

    @ManyToOne
    private DiscordGuild guild;

    @Column(nullable = true)
    private String reaction;

    @Column(nullable = true, columnDefinition = "boolean default false")
    private boolean vip;

    @Column(nullable = true, columnDefinition = "boolean default false")
    private boolean basic;

    public DiscordRole(Role role, DiscordGuild guild) {
        id = role.getId();
        this.guild = guild;
    }
}
