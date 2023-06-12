package com.github.gypsyjr777.discordmanager.event;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReadyEventListener implements EventListener {
    @Override
    @SubscribeEvent
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof ReadyEvent) {
            log.info("Bot is ready!");
        }
    }
}
