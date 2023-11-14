package com.github.gypsyjr777.discordmanager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.dv8tion.jda.api.entities.User;

@Entity
@Table(name = "discord_user")
@Getter
@Setter
@NoArgsConstructor
public class DiscordUser {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "username")
    private String username;

    public DiscordUser(User user) {
        id = user.getId();
        username = user.getName();
    }
}
