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

    private boolean haveLeaveTimer;

    @Column(nullable = true)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "guild")
    private List<DiscordRole> leaveTimerRoles;

    @Column(nullable = true)
    private String messageId;

    public List<String> getLeaveTimerIds() {
        List<String> ids = new ArrayList<>();

        leaveTimerRoles.forEach(it -> {
            ids.add(it.getId());
        });

        return ids;
    }

    public DiscordGuild(Guild guild) {
        id = guild.getId();
        haveLeaveTimer = false;
        guildMembers = new HashSet<>();
        leaveTimerRoles = new ArrayList<>();
    }

    public void addRole(DiscordRole role) {
        if (leaveTimerRoles == null || leaveTimerRoles.isEmpty())
            leaveTimerRoles = new ArrayList<>();

        leaveTimerRoles.add(role);
    }
}
