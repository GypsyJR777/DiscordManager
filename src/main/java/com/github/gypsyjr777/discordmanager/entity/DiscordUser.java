package com.github.gypsyjr777.discordmanager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.dv8tion.jda.api.entities.User;

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

    public DiscordUser(User user) {
        id = user.getId();
        username = user.getName();
    }
}
