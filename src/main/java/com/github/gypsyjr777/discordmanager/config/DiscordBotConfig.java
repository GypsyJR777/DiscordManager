package com.github.gypsyjr777.discordmanager.config;

import com.github.gypsyjr777.discordmanager.event.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.github.gypsyjr777.discordmanager")
@AutoConfiguration
public class DiscordBotConfig {
    @Autowired
    private ApplicationContext context;

    @Value("${discordToken}")
    private String token;

    @Bean
    public JDA jda() throws InterruptedException {

        return JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT) // enables explicit access to message.getContentDisplay()
                .setActivity(Activity.playing("Bot`s simple life"))
                .setEventManager(new AnnotatedEventManager())
                .addEventListeners(new ReadyEventListener(context))
                .addEventListeners(new GuildVoiceEvent(context))
                .addEventListeners(new GuildMembersEvent(context))
                .addEventListeners(new SlashCommandInteraction(context))
                .addEventListeners(new GuildMessageReactionEvent(context))
                .addEventListeners(new GuildMessageEvent(context))
                .addEventListeners(new NewGuildEvent(context))
                .addEventListeners(new GuildNewsEvent(context))
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .enableIntents(GatewayIntent.GUILD_PRESENCES)
                .enableIntents(GatewayIntent.GUILD_MESSAGES)
                .enableIntents(GatewayIntent.GUILD_MESSAGE_REACTIONS)
                .enableIntents(GatewayIntent.GUILD_MESSAGE_TYPING)
                .enableIntents(GatewayIntent.GUILD_EMOJIS_AND_STICKERS)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .build()
                .awaitReady();
    }
}
