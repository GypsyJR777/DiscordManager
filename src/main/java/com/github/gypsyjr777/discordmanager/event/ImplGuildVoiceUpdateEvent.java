package com.github.gypsyjr777.discordmanager.event;

import com.github.gypsyjr777.discordmanager.entity.DiscordGuild;
import com.github.gypsyjr777.discordmanager.service.GuildMemberService;
import com.github.gypsyjr777.discordmanager.service.GuildService;
import com.github.gypsyjr777.discordmanager.service.UserService;
import com.github.gypsyjr777.discordmanager.utils.CheckVip;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ImplGuildVoiceUpdateEvent implements EventListener {
    @Autowired
    private JDA jda;

    private final ApplicationContext context;

    private final UserService userService;
    private final GuildService guildService;
    private final GuildMemberService guildMemberService;

    @Autowired
    public ImplGuildVoiceUpdateEvent(UserService userService, GuildService guildService, GuildMemberService guildMemberService, ApplicationContext context) {
        this.userService = userService;
        this.guildService = guildService;
        this.guildMemberService = guildMemberService;
        this.context = context;
    }

    @Override
    @SubscribeEvent
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof GuildVoiceUpdateEvent) {
            if (jda == null) {
                jda = context.getBean(JDA.class);
            }

            jda.getGuilds().forEach(guild -> {
                DiscordGuild discordGuild = new DiscordGuild(guild);
                guildService.saveGuild(discordGuild);
            });

            Guild guild = ((GuildVoiceUpdateEvent) event).getGuild();
            DiscordGuild discordGuild = guildService.findGuildById(guild.getId()).get();
            if (guildMemberService.getAllGuildMembers(discordGuild).isEmpty()) {
                guild.getMembers().forEach(it -> {
                    userService.updateDateUser(it.getUser(), CheckVip.checkVipMember(it, discordGuild), discordGuild);
                });
            }

            Member member = ((GuildVoiceUpdateEvent) event).getMember();
            userService.updateDateUser(member.getUser(),
                    CheckVip.checkVipMember(((GuildVoiceUpdateEvent) event).getMember(), discordGuild),
                    discordGuild
            );
        } else if (event instanceof GuildMemberJoinEvent) {
            Guild guild = ((GuildMemberJoinEvent) event).getGuild();
            DiscordGuild discordGuild = guildService.findGuildById(guild.getId()).orElseThrow();
            Member member = ((GuildMemberJoinEvent) event).getMember();
            userService.updateDateUser(member.getUser(),
                    CheckVip.checkVipMember(member, discordGuild),
                    discordGuild
            );
        }
    }
}
