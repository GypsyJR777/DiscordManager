package com.github.gypsyjr777.discordmanager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "discord_role")
@Getter
@Setter
@NoArgsConstructor
public class DiscordRole {
    @Id
    private String id;

    private String name;
}
