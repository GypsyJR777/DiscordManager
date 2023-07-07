package com.github.gypsyjr777.discordmanager;

import com.github.gypsyjr777.discordmanager.config.DiscordBotConfig;
import com.github.gypsyjr777.discordmanager.entity.DiscordGuild;
import com.github.gypsyjr777.discordmanager.entity.DiscordUser;
import com.github.gypsyjr777.discordmanager.event.*;
import com.github.gypsyjr777.discordmanager.repository.DiscordGuildRepository;
import com.github.gypsyjr777.discordmanager.service.GuildService;
import com.github.gypsyjr777.discordmanager.service.UserService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = DiscordBotConfig.class)
public class HibernateTests {
    @Autowired
    GuildService guildService;
    @Autowired
    UserService userService;
    @Autowired
    DiscordGuildRepository guildRepository;

    @Test
    void test1() {
        DiscordGuild guild = Instancio.create(DiscordGuild.class);
        guild.getGuildMembers().forEach(guildMember -> {
            userService.saveGuildUser(guildMember.getMember());
        });

        guildRepository.save(guild);
        Assertions.assertNotNull(guildRepository.findById(guild.getId()).orElse(null));
    }
}
