package com.github.gypsyjr777.discordmanager.config;

import com.github.gypsyjr777.discordmanager.service.GuildService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class AfkChecker {
    @Autowired
    private GuildService guildService;

    private Logger log = LogManager.getLogger(AfkChecker.class);

    @Scheduled(fixedRate = 30000000)
    public void schedule() {
        log.info("ALL IS GOOD!!!");

        guildService.findAllGuildsWithLeaveTimers().forEach(guild -> {
            if (guild.isHaveLeaveTimer())
                guildService.findAndKickAfk(guild);
        });
    }
}
