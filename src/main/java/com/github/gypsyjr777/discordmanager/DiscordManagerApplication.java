package com.github.gypsyjr777.discordmanager;

import com.github.gypsyjr777.discordmanager.config.DiscordBotConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class DiscordManagerApplication {

    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(DiscordBotConfig.class);
    }

}
