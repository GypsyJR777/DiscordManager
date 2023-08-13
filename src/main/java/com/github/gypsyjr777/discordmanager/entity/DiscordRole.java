package com.github.gypsyjr777.discordmanager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Role;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private boolean vip = false;

    public DiscordRole(Role role, DiscordGuild guild) {
        id = role.getId();
        this.guild = guild;
    }
}
