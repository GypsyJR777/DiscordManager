package com.github.gypsyjr777.discordmanager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "user_role", uniqueConstraints = { @UniqueConstraint(columnNames = { "role_id", "user_id" }) } )
@Getter
@Setter
@NoArgsConstructor
public class UserRole {
    @Id
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "role_id")
    private DiscordRole role;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id")
    private DiscordUser user;

    public UserRole(DiscordRole discordRole, DiscordUser discordUser) {
        id = UUID.randomUUID();
        role = discordRole;
        user = discordUser;
    }
}
