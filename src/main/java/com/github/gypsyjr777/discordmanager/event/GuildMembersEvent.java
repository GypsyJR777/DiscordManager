package com.github.gypsyjr777.discordmanager.event;

import com.github.gypsyjr777.discordmanager.entity.*;
import com.github.gypsyjr777.discordmanager.service.*;
import com.github.gypsyjr777.discordmanager.utils.CheckLeaveTimer;
import com.github.gypsyjr777.discordmanager.utils.MessageEmbedCreator;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateAvatarEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.user.GenericUserEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateAvatarEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GuildMembersEvent extends ListenerAdapter {
    private static String TITLE = "User`s information changed";

    private final UserService userService;
    private final GuildService guildService;
    private final GuildMemberService memberService;
    private final RoleService roleService;

    public GuildMembersEvent(ApplicationContext context) {
        this.userService = context.getBean(UserService.class);
        this.guildService = context.getBean(GuildService.class);
        this.memberService = context.getBean(GuildMemberService.class);
        this.roleService = context.getBean(RoleService.class);
    }

    @Override
    @SubscribeEvent
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Guild guild = event.getGuild();
        DiscordGuild discordGuild = guildService.findGuildById(guild.getId()).orElseThrow();
        Member member = event.getMember();
        userService.createNewUser(member.getUser(),
                CheckLeaveTimer.checkLeaveTimerMember(member, roleService.getAllRolesByGuild(discordGuild),
                        discordGuild),
                discordGuild);

        roleService.getAllBasicsRolesByGuild(discordGuild).forEach(role -> {
            event.getGuild().addRoleToMember(event.getUser(), event.getGuild().getRoleById(role.getId())).queue();
        });
    }

    @Override
    @SubscribeEvent
    @Transactional
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        Guild guild = event.getGuild();
        DiscordGuild discordGuild = guildService.findGuildById(guild.getId()).orElseThrow();
        DiscordUser user = userService.findByIdDiscordUser(event.getMember().getUser().getId()).orElseThrow();
        GuildMember member = memberService.findGuildMemberByMemberAndGuild(user, discordGuild).orElseThrow();

        memberService.deleteGuildMember(member);
    }

    @SubscribeEvent
    @Override
    public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event) {
        DiscordGuild discordGuild = guildService.findGuildById(event.getGuild().getId()).orElseThrow();
        if (discordGuild.isHaveLogMember()) {
            JDA jda = event.getJDA();
            TextChannel textChannel = jda.getTextChannelById(discordGuild.getLogMemberChannel());
            String oldNickname = ((GuildMemberUpdateNicknameEvent) event).getOldNickname() == null
                    ? event.getUser().getEffectiveName()
                    : ((GuildMemberUpdateNicknameEvent) event).getOldNickname();
            String newNickname = ((GuildMemberUpdateNicknameEvent) event).getNewNickname() == null
                    ? event.getUser().getEffectiveName()
                    : ((GuildMemberUpdateNicknameEvent) event).getNewNickname();

            textChannel.sendMessage("").setEmbeds(MessageEmbedCreator.createMessageEmbed(
                    TITLE,
                    "Nickname was changed",
                    oldNickname + " to " + newNickname,
                    event.getUser().getAvatarUrl())).queue();

        }
    }

    @SubscribeEvent
    @Override
    public void onGuildMemberUpdateAvatar(GuildMemberUpdateAvatarEvent event) {
        DiscordGuild discordGuild = guildService.findGuildById(event.getGuild().getId()).orElseThrow();
        if (discordGuild.isHaveLogMember()) {
            JDA jda = event.getJDA();

            TextChannel textChannel = jda.getTextChannelById(discordGuild.getLogMemberChannel());
            textChannel.sendMessage("").setEmbeds(MessageEmbedCreator.createMessageEmbed(
                    TITLE,
                    "Avatar was changed",
                    null,
                    event.getUser().getAvatarUrl())).queue();
        }
    }

    @Override
    @SubscribeEvent
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
        JDA jda = event.getJDA();
        event.getRoles().forEach(role -> {
            DiscordGuild discordGuild = guildService.findGuildById(event.getGuild().getId()).orElseThrow();
            if (discordGuild.isHaveLogMember()) {
                String nickname = event.getMember().getNickname() == null ? event.getUser().getEffectiveName()
                        : event.getMember().getNickname();

                TextChannel textChannel = jda.getTextChannelById(discordGuild.getLogMemberChannel());
                textChannel.sendMessage("").setEmbeds(MessageEmbedCreator.createMessageEmbed(
                        TITLE,
                        "Add role",
                        nickname + " got a new role " + role.getName(),
                        event.getUser().getAvatarUrl())).queue();
            }
        });
    }

    @Override
    @SubscribeEvent
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
        JDA jda = event.getJDA();
        event.getRoles().forEach(role -> {
            DiscordGuild discordGuild = guildService.findGuildById(event.getGuild().getId()).orElseThrow();
            if (discordGuild.isHaveLogMember()) {
                String nickname = event.getMember().getNickname() == null ? event.getUser().getEffectiveName()
                        : event.getMember().getNickname();

                TextChannel textChannel = jda.getTextChannelById(discordGuild.getLogMemberChannel());
                textChannel.sendMessage("").setEmbeds(MessageEmbedCreator.createMessageEmbed(
                        TITLE,
                        "Delete role",
                        nickname + " deleted a role " + role.getName(),
                        event.getUser().getAvatarUrl())).queue();
            }
        });
    }

    @Override
    @SubscribeEvent
    public void onUserUpdateAvatar(UserUpdateAvatarEvent event) {
        JDA jda = event.getJDA();
        DiscordUser user = userService.findByIdDiscordUser(event.getUser().getId()).orElseThrow();
        memberService.getGuildsByMember(user).forEach(member -> {
            DiscordGuild discordGuild = member.getGuild();

            if (discordGuild.isHaveLogMember()) {
                TextChannel textChannel = jda.getTextChannelById(discordGuild.getLogMemberChannel());
                textChannel.sendMessage("").setEmbeds(MessageEmbedCreator.createMessageEmbed(
                        TITLE,
                        "Avatar was changed",
                        null,
                        event.getUser().getAvatarUrl())).queue();
            }
        });
    }
}
