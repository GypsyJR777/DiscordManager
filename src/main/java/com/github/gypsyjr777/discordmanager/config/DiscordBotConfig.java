package com.github.gypsyjr777.discordmanager.config;

import com.github.gypsyjr777.discordmanager.event.ImplGuildVoiceUpdateEvent;
import com.github.gypsyjr777.discordmanager.event.ReadyEventListener;
import com.github.gypsyjr777.discordmanager.service.GuildMemberService;
import com.github.gypsyjr777.discordmanager.service.GuildService;
import com.github.gypsyjr777.discordmanager.service.UserService;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.github.gypsyjr777.discordmanager.repository")
public class DiscordBotConfig {
    @Autowired
    private ApplicationContext context;

    @Value("${discordToken}")
    private String token;

    @Bean
    public JDA jda() throws InterruptedException {
        return JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT) // enables explicit access to message.getContentDisplay()
                .setEventManager(new AnnotatedEventManager())
                .addEventListeners(new ReadyEventListener())
                .addEventListeners(new ImplGuildVoiceUpdateEvent(context.getBean(UserService.class), context.getBean(GuildService.class), context.getBean(GuildMemberService.class)))
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .enableIntents(GatewayIntent.GUILD_PRESENCES)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .build()
                .awaitReady();
    }
}
