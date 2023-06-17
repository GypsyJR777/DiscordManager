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

    @OneToMany(mappedBy = "guild")
    private Set<GuildMember> guildMembers = new HashSet<>();

    private boolean haveVips;

    @Column(nullable = true)
    @OneToMany
    private List<DiscordRole> vipRoles = new ArrayList<>();

    public List<String> getVipIds() {
        List<String> ids = new ArrayList<>();

        vipRoles.forEach(it -> {
            ids.add(it.getId());
        });

        return ids;
    }

    public DiscordGuild(Guild guild) {
        id = guild.getId();
//        guild.getMembers().forEach(member -> {
//            DiscordUser user = new DiscordUser(member);
//            GuildMember guildMember = new GuildMember();
//            guildMember.setGuild(this);
//            guildMember.setMember(user);
//            guildMembers.add(guildMember);
//        });
    }
}
