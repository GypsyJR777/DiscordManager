package com.github.gypsyjr777.discordmanager.config;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.internal.requests.restaction.CommandListUpdateActionImpl;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class DiscordBotConfig {
    @Autowired
    private ApplicationContext context;
    @Bean
    public JDA jda(){
        JDA jda = Mockito.mock(JDA.class);
        CommandListUpdateAction updateAction = Mockito.mock(CommandListUpdateAction.class);
        CommandListUpdateActionImpl action = Mockito.mock(CommandListUpdateActionImpl.class);
        Mockito.when(jda.updateCommands()).thenReturn(updateAction);
        Mockito.when(jda.updateCommands().addCommands(Mockito.anyList())).thenReturn(action);
        return jda;
    }
}
