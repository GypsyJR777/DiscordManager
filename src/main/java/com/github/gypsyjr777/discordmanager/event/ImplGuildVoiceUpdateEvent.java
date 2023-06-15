package com.github.gypsyjr777.discordmanager.event;

import com.github.gypsyjr777.discordmanager.service.UserService;
import com.github.gypsyjr777.discordmanager.utils.CheckVip;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImplGuildVoiceUpdateEvent implements EventListener {
    @Autowired
    private JDA jda;

    private final UserService userService;

    @Autowired
    public ImplGuildVoiceUpdateEvent(UserService userService) {
        this.userService = userService;
    }

    @Override
    @SubscribeEvent
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof GuildVoiceUpdateEvent) {
            Guild guild = ((GuildVoiceUpdateEvent) event).getGuild();
            if (guild.getId().equals("708671790477475901")) {
                if (userService.findAllGuildUsers().isEmpty()) {
                    guild.getMembers().forEach(member -> {
                        userService.updateDateUser(member.getUser(), CheckVip.checkVipMember(member));
                    });
                }
            }

            userService.updateDateUser(((GuildVoiceUpdateEvent) event).getMember().getUser(),
                    CheckVip.checkVipMember(((GuildVoiceUpdateEvent) event).getMember())
            );
        } else if (event instanceof GuildMemberJoinEvent) {
            userService.updateDateUser(((GuildMemberJoinEvent) event).getMember().getUser(),
                    CheckVip.checkVipMember(((GuildMemberJoinEvent) event).getMember())
            );
        }
    }
}
