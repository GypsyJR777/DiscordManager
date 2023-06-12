package com.github.gypsyjr777.discordmanager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "discordGuildUser")
@Getter
@Setter
@NoArgsConstructor
public class DiscordGuildUser {
    @Id
    private String id;

    private LocalDateTime lastOut;
    private boolean isVip;
    private String username;
}
