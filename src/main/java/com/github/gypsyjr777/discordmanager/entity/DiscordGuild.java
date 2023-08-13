package com.github.gypsyjr777.discordmanager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Guild;

@Entity
@Table(name = "discord_guild")
@Getter
@Setter
@NoArgsConstructor
public class DiscordGuild {

    @Id
    private String id;

    @Column(nullable = true, columnDefinition = "boolean default false")
    private boolean haveLeaveTimer = false;

    @Column(nullable = true, columnDefinition = "boolean default false")
    private boolean haveBasicRole = false;

    @Column(nullable = true, columnDefinition = "boolean default false")
    private boolean haveLogMember = false;

    @Column(nullable = true, columnDefinition = "boolean default false")
    private boolean haveLogGuild = false;

    @Column(nullable = true)
    private String logGuildChannel;

    @Column(nullable = true)
    private String logMemberChannel;

    @Column(nullable = true)
    private String messageId;

    public DiscordGuild(Guild guild) {
        id = guild.getId();
        haveLeaveTimer = false;
        haveBasicRole = false;
        haveLogMember = false;
    }
}
