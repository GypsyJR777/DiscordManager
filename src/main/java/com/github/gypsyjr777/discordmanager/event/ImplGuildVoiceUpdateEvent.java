package com.github.gypsyjr777.discordmanager.event;

import com.github.gypsyjr777.discordmanager.service.GuildUserService;
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

    private final GuildUserService guildUserService;

    @Autowired
    public ImplGuildVoiceUpdateEvent(GuildUserService guildUserService) {
        this.guildUserService = guildUserService;
    }

    @Override
    @SubscribeEvent
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof GuildVoiceUpdateEvent) {
            Guild guild = ((GuildVoiceUpdateEvent) event).getGuild();
            if (guild.getId().equals("708671790477475901")) {
                if (guildUserService.findAllGuildUsers().isEmpty()) {
                    guild.getMembers().forEach(member -> {
                        guildUserService.updateDateUser(member.getUser(), CheckVip.checkVipMember(member));
                    });
                }
            }

            guildUserService.updateDateUser(((GuildVoiceUpdateEvent) event).getMember().getUser(),
                    CheckVip.checkVipMember(((GuildVoiceUpdateEvent) event).getMember())
            );
        } else if (event instanceof GuildMemberJoinEvent) {
            guildUserService.updateDateUser(((GuildMemberJoinEvent) event).getMember().getUser(),
                    CheckVip.checkVipMember(((GuildMemberJoinEvent) event).getMember())
            );
        }
    }
}
