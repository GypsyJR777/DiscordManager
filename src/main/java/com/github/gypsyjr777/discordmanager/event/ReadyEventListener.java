package com.github.gypsyjr777.discordmanager.event;

import com.github.gypsyjr777.discordmanager.entity.DiscordGuild;
import com.github.gypsyjr777.discordmanager.service.GuildService;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ReadyEventListener implements EventListener {

    @Autowired
    private JDA jda;

    private final ApplicationContext context;

    private final GuildService guildService;

    @Autowired
    @Lazy
    public ReadyEventListener(ApplicationContext context, GuildService guildService) {
        this.context = context;
        this.guildService = guildService;
    }

    @Override
    @SubscribeEvent
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof ReadyEvent) {
            log.info("Bot is ready!");

        }
    }
}
