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

//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//    @Column(nullable = true)
//    Set<GuildMember> guildMembers = new HashSet<>();

    public DiscordUser(User user) {
        id = user.getId();
        username = user.getName();
    }

//    public void addGuildMember(GuildMember guildMember) {
//        guildMembers.add(guildMember);
//    }
//
//    public void removeGuildMember(GuildMember guildMember) {
//        guildMembers.remove(guildMember);
//    }
}
