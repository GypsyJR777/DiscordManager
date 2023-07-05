package com.github.gypsyjr777.discordmanager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "discord_guild")
@Getter
@Setter
@NoArgsConstructor
public class DiscordGuild {

    @Id
    private String id;

    @Column(nullable = true)
    @OneToMany(mappedBy = "guild")
    private Set<GuildMember> guildMembers;

    @Column(nullable = true)
    private boolean haveLeaveTimer = false;

    @Column(nullable = true, columnDefinition = "boolean default false")
    private boolean haveBasicRole = false;

    @Column(nullable = true)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "guild")
    private List<DiscordRole> roles;

    @Column(nullable = true)
    private String messageId;

    public List<String> getLeaveTimerIds() {
        List<String> ids = new ArrayList<>();

        roles.forEach(it -> {
            ids.add(it.getId());
        });

        return ids;
    }

    public DiscordGuild(Guild guild) {
        id = guild.getId();
        haveLeaveTimer = false;
        haveBasicRole = false;
        guildMembers = new HashSet<>();
        roles = new ArrayList<>();
    }

    public void addRole(DiscordRole role) {
        if (roles == null || roles.isEmpty())
            roles = new ArrayList<>();

        roles.add(role);
    }
}
