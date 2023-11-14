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
    @Column(name = "id")
    private String id;

    @Column(nullable = true, columnDefinition = "boolean default false", name = "have_leave_timer")
    private boolean haveLeaveTimer = false;

    @Column(nullable = true, columnDefinition = "boolean default false", name = "have_basic_role")
    private boolean haveBasicRole = false;

    @Column(nullable = true, columnDefinition = "boolean default false", name = "have_log_member")
    private boolean haveLogMember = false;

    @Column(nullable = true, columnDefinition = "boolean default false", name = "have_log_guild")
    private boolean haveLogGuild = false;

    @Column(nullable = true, name = "log_guild_channel")
    private String logGuildChannel;

    @Column(nullable = true, name = "log_member_channel")
    private String logMemberChannel;

    @Column(nullable = true, name = "message_id")
    private String messageId;

    public DiscordGuild(Guild guild) {
        id = guild.getId();
        haveLeaveTimer = false;
        haveBasicRole = false;
        haveLogMember = false;
    }
}
