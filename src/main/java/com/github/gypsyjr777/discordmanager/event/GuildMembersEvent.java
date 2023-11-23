package com.github.gypsyjr777.discordmanager.event;

import com.github.gypsyjr777.discordmanager.entity.*;
import com.github.gypsyjr777.discordmanager.exception.NullChannelException;
import com.github.gypsyjr777.discordmanager.exception.NullMemberException;
import com.github.gypsyjr777.discordmanager.exception.NullRoleException;
import com.github.gypsyjr777.discordmanager.exception.NullUserException;
import com.github.gypsyjr777.discordmanager.service.*;
import com.github.gypsyjr777.discordmanager.utils.BasicUtils;
import com.github.gypsyjr777.discordmanager.utils.MessageEmbedCreator;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateAvatarEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateAvatarEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class GuildMembersEvent extends ListenerAdapter {
    private static String TITLE = "User`s information changed";

    private final UserService userService;
    private final GuildService guildService;
    private final GuildMemberService memberService;
    private final RoleService roleService;
    private final BasicUtils utils;
    private final Logger log;

    public GuildMembersEvent(ApplicationContext context) {
        this.userService = context.getBean(UserService.class);
        this.guildService = context.getBean(GuildService.class);
        this.memberService = context.getBean(GuildMemberService.class);
        this.roleService = context.getBean(RoleService.class);
        this.utils = context.getBean(BasicUtils.class);

        this.log = LogManager.getLogger(GuildMembersEvent.class);
    }

    @Override
    @SubscribeEvent
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Guild guild = event.getGuild();

        DiscordGuild discordGuild = guildService.findGuildById(guild.getId()).orElseGet(() -> utils.createDiscordGuild(guild));

        Member member = event.getMember();

        log.info("New member {} in guild {}", member.getUser().getEffectiveName(), guild.getName());

        userService.createNewUser(
                member.getUser(),
                BasicUtils.checkLeaveTimerMember(member, roleService.getAllRolesByGuild(discordGuild), discordGuild),
                discordGuild
        );

        roleService.getAllBasicsRolesByGuild(discordGuild).forEach(role -> {

            Role discordRole = event.getGuild().getRoleById(role.getId());
            if (discordRole == null) throw new NullRoleException(role.getId());

            event.getGuild().addRoleToMember(event.getUser(), discordRole).queue();
        });
    }

    @Override
    @SubscribeEvent
    @Transactional
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        Guild guild = event.getGuild();
        DiscordGuild discordGuild = guildService.findGuildById(guild.getId()).orElseGet(() -> utils.createDiscordGuild(guild));
        DiscordUser user = userService.findByIdDiscordUser(event.getMember().getUser().getId())
                .orElseThrow(() -> new NullUserException(event.getUser().getId()));

        GuildMember member = memberService.findGuildMemberByMemberAndGuild(user, discordGuild)
                .orElseThrow(() -> new NullMemberException(user.getUsername()));

        log.info("Remove member {} in guild {}", user.getUsername(), guild.getName());

        memberService.deleteGuildMember(member);
    }

    @SubscribeEvent
    @Override
    public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event) {
        DiscordGuild discordGuild = guildService.findGuildById(event.getGuild().getId())
                .orElseGet(() -> utils.createDiscordGuild(event.getGuild()));

        if (discordGuild.isHaveLogMember()) {
            JDA jda = event.getJDA();
            TextChannel textChannel = jda.getTextChannelById(discordGuild.getLogMemberChannel());
            String oldNickname = event.getOldNickname() == null
                    ? event.getUser().getEffectiveName()
                    : event.getOldNickname();
            String newNickname = event.getNewNickname() == null
                    ? event.getUser().getEffectiveName()
                    : event.getNewNickname();

            if (textChannel == null) throw new NullChannelException(discordGuild.getLogMemberChannel());

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
        DiscordGuild discordGuild = guildService.findGuildById(event.getGuild().getId())
                .orElseGet(() -> utils.createDiscordGuild(event.getGuild()));

        if (discordGuild.isHaveLogMember()) {
            JDA jda = event.getJDA();

            TextChannel textChannel = jda.getTextChannelById(discordGuild.getLogMemberChannel());
            if (textChannel == null) throw new NullChannelException(discordGuild.getLogMemberChannel());

            textChannel.sendMessage("").setEmbeds(MessageEmbedCreator.createFullMessageEmbed(
                    null,
                    TITLE,
                    "Avatar was changed",
                    OffsetDateTime.now(),
                    null,
                    MessageEmbedCreator.createAuthorInfo(event.getMember().getEffectiveName(), null, event.getUser().getEffectiveAvatarUrl(), null),
                    null,
                    event.getNewAvatar().getUrl(),
                    List.of(),
                    Color.CYAN
            )).queue();
        }
    }

    @Override
    @SubscribeEvent
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
        JDA jda = event.getJDA();
        event.getRoles().forEach(role -> {
            DiscordGuild discordGuild = guildService.findGuildById(event.getGuild().getId())
                    .orElseGet(() -> utils.createDiscordGuild(event.getGuild()));

            if (discordGuild.isHaveLogMember()) {
                String nickname = event.getMember().getNickname() == null ? event.getUser().getEffectiveName()
                        : event.getMember().getNickname();

                TextChannel textChannel = jda.getTextChannelById(discordGuild.getLogMemberChannel());
                if (textChannel == null) throw new NullChannelException(discordGuild.getLogMemberChannel());

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
            DiscordGuild discordGuild = guildService.findGuildById(event.getGuild().getId())
                    .orElseGet(() -> utils.createDiscordGuild(event.getGuild()));

            if (discordGuild.isHaveLogMember()) {
                String nickname = event.getMember().getNickname() == null ? event.getUser().getEffectiveName()
                        : event.getMember().getNickname();

                TextChannel textChannel = jda.getTextChannelById(discordGuild.getLogMemberChannel());
                if (textChannel == null) throw new NullChannelException(discordGuild.getLogMemberChannel());

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
        DiscordUser user = userService.findByIdDiscordUser(event.getUser().getId())
                .orElseThrow(() -> new NullUserException(event.getUser().getEffectiveName()));

        memberService.getGuildsByMember(user).forEach(member -> {
            DiscordGuild discordGuild = member.getGuild();

            if (discordGuild.isHaveLogMember()) {
                TextChannel textChannel = jda.getTextChannelById(discordGuild.getLogMemberChannel());
                if (textChannel == null) throw new NullChannelException(discordGuild.getLogMemberChannel());

                textChannel.sendMessage("").setEmbeds(MessageEmbedCreator.createFullMessageEmbed(
                        null,
                        TITLE,
                        "Avatar was changed",
                        OffsetDateTime.now(),
                        null,
                        MessageEmbedCreator.createAuthorInfo(event.getUser().getEffectiveName(), null, event.getUser().getEffectiveAvatarUrl(), null),
                        null,
                        event.getNewAvatar().getUrl(),
                        List.of(),
                        Color.CYAN
                )).queue();
            }
        });
    }
}
