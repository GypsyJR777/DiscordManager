package com.github.gypsyjr777.discordmanager.service;

import com.github.gypsyjr777.discordmanager.entity.DiscordGuildUser;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class GuildService {
    @Autowired
    private JDA jda;

    @Autowired
    private GuildUserService guildUserService;

    public void findAndKickAfk(List<DiscordGuildUser> guildUsers) {
        guildUsers.forEach(user -> {
            LocalDateTime userLastVisit = user.getLastOut();

            if (ChronoUnit.DAYS.between(userLastVisit, LocalDateTime.now()) > 30 && !user.isVip()) {
                removeUser(user.getId());
            }
        });
    }
    public void removeUser(String id) {
        Guild guild = jda.getGuildById("708671790477475901");
        assert guild != null;
        log.info("Kick {}", id);
        guild.kick(Objects.requireNonNull(jda.getUserById(id))).queue();
        guildUserService.deleteGuildUser(id);
    }
}
