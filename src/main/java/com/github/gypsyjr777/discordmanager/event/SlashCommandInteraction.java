package com.github.gypsyjr777.discordmanager.event;

import com.github.gypsyjr777.discordmanager.config.command.SlashCommand;
import com.github.gypsyjr777.discordmanager.config.command.SubcommandEnum;
import com.github.gypsyjr777.discordmanager.entity.DiscordGuild;
import com.github.gypsyjr777.discordmanager.entity.DiscordRole;
import com.github.gypsyjr777.discordmanager.entity.DiscordUser;
import com.github.gypsyjr777.discordmanager.entity.GuildMember;
import com.github.gypsyjr777.discordmanager.exception.NullChannelException;
import com.github.gypsyjr777.discordmanager.model.KandinskyBody;
import com.github.gypsyjr777.discordmanager.service.*;
import com.github.gypsyjr777.discordmanager.utils.BasicUtils;
import com.github.gypsyjr777.discordmanager.utils.MessageEmbedCreator;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageEditData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SlashCommandInteraction extends ListenerAdapter {
    private final GuildService guildService;
    private final GuildMemberService memberService;
    private final UserService userService;
    private final RoleService roleService;
    private final KandinskyService kandinskyService;
    private final BasicUtils utils;

    private Logger log;

    public SlashCommandInteraction(ApplicationContext context) {
        this.userService = context.getBean(UserService.class);
        this.guildService = context.getBean(GuildService.class);
        this.memberService = context.getBean(GuildMemberService.class);
        this.roleService = context.getBean(RoleService.class);
        this.kandinskyService = context.getBean(KandinskyService.class);
        this.utils = context.getBean(BasicUtils.class);
        this.log = LogManager.getLogger(SlashCommandInteraction.class);
    }

    @Override
    @SubscribeEvent
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.isFromGuild()) {
            if (event.getFullCommandName().equals(SubcommandEnum.LEAVE_TIMER_ADD_ROLE.getCommand())) {
                leaveTimerAddRole(event);
            } else if (event.getFullCommandName().equals(SubcommandEnum.LEAVE_TIMER_DEL_ROLE.getCommand())) {
                leaveTimerDelRole(event);
            } else if (event.getFullCommandName().equals(SubcommandEnum.LEAVE_TIMER_LIST.getCommand())) {
                leaveTimerList(event);
            } else if (event.getFullCommandName().equals(SubcommandEnum.LEAVE_TIMER_ON.getCommand())) {
                leaveTimerOn(event);
            } else if (event.getFullCommandName().equals(SubcommandEnum.LEAVE_TIMER_OFF.getCommand())) {
                leaveTimerOff(event);
            } else if (event.getFullCommandName().equals(SlashCommand.ANNOUNCEMENT.getCommand())) {
                announce(event);
            } else if (event.getFullCommandName().equals(SubcommandEnum.REACTION_ROLE_ADD.getCommand())) {
                addReactionRole(event);
            } else if (event.getFullCommandName().equals(SubcommandEnum.REACTION_ROLE_TEXT.getCommand())) {
                addTextReactionRole(event);
            } else if (event.getFullCommandName().equals(SubcommandEnum.MEMBER_LOG_ON.getCommand())) {
                memberLoggingOn(event);
            } else if (event.getFullCommandName().equals(SubcommandEnum.MEMBER_LOG_OFF.getCommand())) {
                memberLoggingOff(event);
            } else if (event.getFullCommandName().equals(SubcommandEnum.GUILD_LOG_ON.getCommand())) {
                guildLogOn(event);
            } else if (event.getFullCommandName().equals(SubcommandEnum.GUILD_LOG_OFF.getCommand())) {
                guildLogOff(event);
            } else if (event.getFullCommandName().equals(SlashCommand.DEFAULT_ROLE.getCommand())) {
                setDefaultRole(event);
            } else if (event.getFullCommandName().equals(SlashCommand.LEVEL.getCommand())) {
                getLevel(event);
            } else if (event.getFullCommandName().equals(SlashCommand.GENERATE_IMAGE.getCommand())) {
                try {
                    Thread imageThread = new Thread(() -> generateImage(event));
                    imageThread.start();
                } catch (Exception exception) {
                    log.error(exception.getMessage());
                    TextChannel textChannel = event.getChannel().asTextChannel();
                    textChannel.sendMessage("Your picture cannot be created. Try changing your request or wait until the next day").queue();
                }
            } else if (event.getFullCommandName().equals(SlashCommand.ABOUT_BOT.getCommand())) {
                aboutBot(event);
            } else if (event.getFullCommandName().equals(SlashCommand.HELP.getCommand())) {
                help(event);
            } else if (event.getFullCommandName().equals(SubcommandEnum.WELCOME_PERSONAL.getCommand())) {
                addWelcomePersonalMessage(event);
            } else if (event.getFullCommandName().equals(SubcommandEnum.WELCOME_GUILD.getCommand())) {
                addWelcomeGuildMessage(event);
            }
        }
    }

    private void addWelcomeGuildMessage(SlashCommandInteractionEvent event) {
        if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            String welcomeChannel = event.getOption("channel").getAsChannel().getId();
            String welcomeMessage = event.getOption("message").getAsString();

            Guild guild = event.getGuild();
            DiscordGuild discordGuild = guildService.findGuildById(guild.getId()).orElseGet(() -> utils.createDiscordGuild(guild));
            discordGuild.setGuildWelcome(welcomeMessage);
            discordGuild.setWelcomeChannel(welcomeChannel);
            guildService.saveGuild(discordGuild);
            event.reply("Welcome message has added").queue();
        } else {
            event.reply("For this action, you need administrator rights").queue();
        }
    }

    private void addWelcomePersonalMessage(SlashCommandInteractionEvent event) {
        if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            String welcomeMessage = event.getOption("message").getAsString();

            Guild guild = event.getGuild();
            DiscordGuild discordGuild = guildService.findGuildById(guild.getId()).orElseGet(() -> utils.createDiscordGuild(guild));
            discordGuild.setPersonalWelcome(welcomeMessage);
            guildService.saveGuild(discordGuild);
            event.reply("Welcome message has added").queue();
        } else {
            event.reply("For this action, you need administrator rights").queue();
        }
    }

    private void announce(SlashCommandInteractionEvent event) {
        if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            TextChannel textChannel;
            if (event.getOption("channel") == null) {
                textChannel = event.getChannel().asTextChannel();
            } else {
                textChannel = event.getOption("channel").getAsChannel().asTextChannel();
            }

            textChannel
                    .sendMessage("")
                    .setEmbeds(MessageEmbedCreator.createMessageEmbed(event.getOption("title").getAsString().replace("\\n", "\n"), event.getOption("text").getAsString().replace("\\n", "\n")))
                    .queue();
            event.reply("The announcement is published").queue();
        } else {
            event.reply("For this action, you need administrator rights").queue();
        }
    }

    private void leaveTimerAddRole(SlashCommandInteractionEvent event) {
        if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            DiscordGuild guild = guildService.findGuildById(event.getGuild().getId())
                    .orElseGet(() -> utils.createDiscordGuild(event.getGuild()));

            guild.setHaveLeaveTimer(true);

            Role role = event.getOption("role").getAsRole();
            DiscordRole discordRole = roleService.findRoleById(role.getId())
                    .orElseGet(() -> new DiscordRole(role, guild));
            discordRole.setVip(true);
            roleService.saveRole(discordRole);

//            guild.addRole(role);
            guildService.saveGuild(guild);

            event.getGuild().getMembers().forEach(member -> {
                if (member.getRoles().stream().anyMatch(r -> r.getId().equals(discordRole.getId()))) {
                    DiscordUser discordUser = userService.findByIdDiscordUser(member.getUser().getId())
                            .orElseGet(() -> utils.createDiscordUser(member.getUser()));

                    GuildMember guildMember = memberService.findGuildMemberByMemberAndGuild(
                            discordUser,
                            guild
                    ).orElseGet(() -> utils.createGuildMember(discordUser, guild));

                    guildMember.setLeaveTimer(true);

                    memberService.saveGuildMember(guildMember);
                }
            });

            event.reply("Role added").queue();
        } else {
            event.reply("For this action, you need administrator rights").queue();
        }
    }

    private void leaveTimerDelRole(SlashCommandInteractionEvent event) {
        if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            DiscordGuild guild = guildService.findGuildById(event.getGuild().getId())
                    .orElseGet(() -> utils.createDiscordGuild(event.getGuild()));

            if (!guild.isHaveLeaveTimer()) {
                event.reply("You do not have an exception for inactivity enabled").queue();
                return;
            }
            Role role = event.getOption("role").getAsRole();
            DiscordRole discordRole = roleService.findRoleById(role.getId())
                    .orElseGet(() -> new DiscordRole(role, guild));
            discordRole.setVip(false);
            roleService.saveRole(discordRole);

            event.getGuild().getMembers().forEach(member -> {
                if (member.getRoles().stream().anyMatch(r -> r.getId().equals(discordRole.getId()))) {
                    DiscordUser discordUser = userService.findByIdDiscordUser(member.getUser().getId())
                            .orElseGet(() -> utils.createDiscordUser(member.getUser()));

                    GuildMember guildMember = memberService.findGuildMemberByMemberAndGuild(
                            discordUser,
                            guild
                    ).orElseGet(() -> utils.createGuildMember(discordUser, guild));

                    guildMember.setLeaveTimer(false);

                    memberService.saveGuildMember(guildMember);
                }
            });

            event.reply("Role removed").queue();
        } else {
            event.reply("For this action, you need administrator rights").queue();
        }
    }

    private void leaveTimerList(SlashCommandInteractionEvent event) {
        if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            DiscordGuild guild = guildService.findGuildById(event.getGuild().getId())
                    .orElseGet(() -> utils.createDiscordGuild(event.getGuild()));

            if (!guild.isHaveLeaveTimer()) {
                event.reply("You do not have an exception for inactivity enabled").queue();
                return;
            }

            List<DiscordRole> vipRoles = roleService.getAllProtectionRolesByGuild(guild);

            StringBuilder message = new StringBuilder("Roles:");
            JDA jda = event.getJDA();
            vipRoles.forEach(role -> {
                String roleName = jda.getRoleById(role.getId()).getName();
                message.append("\n").append(roleName);
            });

            event.reply(message.toString()).queue();
        } else {
            event.reply("For this action, you need administrator rights").queue();
        }
    }

    private void addReactionRole(SlashCommandInteractionEvent event) {
        if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            DiscordGuild guild = guildService.findGuildById(event.getGuild().getId())
                    .orElseGet(() -> utils.createDiscordGuild(event.getGuild()));

            DiscordRole role = roleService.findRoleById(event.getOption("role").getAsRole().getId()).orElseGet(() -> new DiscordRole(event.getOption("role").getAsRole(), guild));
            String reaction = event.getOption("reaction").getAsString();
            String messageId = event.getOption("message_id").getAsString();

            for (TextChannel textChannel : event.getGuild().getTextChannels()) {
                if (textChannel.getIterableHistory().stream().anyMatch(message -> message.getId().equals(messageId))) {
                    textChannel.retrieveMessageById(messageId).queue(message -> message.addReaction(Emoji.fromFormatted(reaction)).queue());
                    role.setReaction(reaction);
                    roleService.saveRole(role);
                    event.reply("Reaction added").queue();
                    return;
                }
            }
            throw new NullChannelException("Reaction Message did not find");
        } else {
            event.reply("For this action, you need administrator rights").queue();
        }
    }

    private void addTextReactionRole(SlashCommandInteractionEvent event) {
        if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            DiscordGuild guild = guildService.findGuildById(event.getGuild().getId())
                    .orElseGet(() -> utils.createDiscordGuild(event.getGuild()));

            String text = event.getOption("text").getAsString();
            TextChannel textChannel = event.getChannel().asTextChannel();
            String title = event.getOption("title") == null ? null : event.getOption("title").getAsString();

            if (title == null) {
                textChannel.sendMessage(text).queue();
            } else {
                String messageId = textChannel
                        .sendMessage("")
                        .setEmbeds(MessageEmbedCreator.createMessageEmbed(title.replace("\\n", "\n"), text.replace("\\n", "\n"))).complete().getId();
                guild.setMessageId(messageId);
                guildService.saveGuild(guild);
            }

            event.reply("Reaction message added").queue();
        } else {
            event.reply("For this action, you need administrator rights").queue();
        }
    }

    private void leaveTimerOff(SlashCommandInteractionEvent event) {
        if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            DiscordGuild guild = guildService.findGuildById(event.getGuild().getId())
                    .orElseGet(() -> utils.createDiscordGuild(event.getGuild()));

            guild.setHaveLeaveTimer(false);
            guildService.saveGuild(guild);

            event.reply("Leave timer is off").queue();
        } else {
            event.reply("For this action, you need administrator rights").queue();
        }
    }

    private void leaveTimerOn(SlashCommandInteractionEvent event) {
        if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            DiscordGuild guild = guildService.findGuildById(event.getGuild().getId())
                    .orElseGet(() -> utils.createDiscordGuild(event.getGuild()));

            guild.setHaveLeaveTimer(true);
            guildService.saveGuild(guild);

            event.reply("Leave timer is on").queue();
        } else {
            event.reply("For this action, you need administrator rights").queue();
        }
    }

    private void memberLoggingOn(SlashCommandInteractionEvent event) {
        if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            DiscordGuild guild = guildService.findGuildById(event.getGuild().getId())
                    .orElseGet(() -> utils.createDiscordGuild(event.getGuild()));

            guild.setLogMemberChannel(event.getOption("channel").getAsString());
            guild.setHaveLogMember(true);
            guildService.saveGuild(guild);

            event.reply("Member logging is on").queue();
        } else {
            event.reply("For this action, you need administrator rights").queue();
        }
    }

    private void memberLoggingOff(SlashCommandInteractionEvent event) {
        if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            DiscordGuild guild = guildService.findGuildById(event.getGuild().getId())
                    .orElseGet(() -> utils.createDiscordGuild(event.getGuild()));

            guild.setLogMemberChannel(null);
            guild.setHaveLogMember(false);
            guildService.saveGuild(guild);

            event.reply("Member logging is off").queue();
        } else {
            event.reply("For this action, you need administrator rights").queue();
        }
    }

    private void guildLogOn(SlashCommandInteractionEvent event) {
        if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            DiscordGuild guild = guildService.findGuildById(event.getGuild().getId())
                    .orElseGet(() -> utils.createDiscordGuild(event.getGuild()));

            guild.setLogGuildChannel(event.getOption("channel").getAsString());
            guild.setHaveLogGuild(true);
            guildService.saveGuild(guild);

            event.reply("Guild logging is on").queue();
        } else {
            event.reply("For this action, you need administrator rights").queue();
        }
    }

    private void guildLogOff(SlashCommandInteractionEvent event) {
        if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            DiscordGuild guild = guildService.findGuildById(event.getGuild().getId())
                    .orElseGet(() -> utils.createDiscordGuild(event.getGuild()));

            guild.setLogGuildChannel(null);
            guild.setHaveLogGuild(false);
            guildService.saveGuild(guild);

            event.reply("Guild logging is off").queue();
        } else {
            event.reply("For this action, you need administrator rights").queue();
        }
    }

    private void setDefaultRole(SlashCommandInteractionEvent event) {
        if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            DiscordGuild guild = guildService.findGuildById(event.getGuild().getId())
                    .orElseGet(() -> utils.createDiscordGuild(event.getGuild()));

            if (event.getOption("role") != null) {
                DiscordRole role = roleService.findRoleById(event.getOption("role").getAsRole().getId())
                        .orElseGet(() -> new DiscordRole(event.getOption("role").getAsRole(), guild));

                guild.setHaveBasicRole(true);
                role.setBasic(true);

                roleService.saveRole(role);
                event.reply("Default role added").queue();
            } else {
                roleService.getAllBasicsRolesByGuild(guild).forEach(role -> {
                    role.setBasic(false);
                    roleService.saveRole(role);
                });

                guild.setHaveBasicRole(false);
                event.reply("Default roles removed").queue();
            }

            guildService.saveGuild(guild);

        } else {
            event.reply("For this action, you need administrator rights").queue();
        }
    }

    private void getLevel(SlashCommandInteractionEvent event) {
        DiscordGuild guild = guildService.findGuildById(event.getGuild().getId())
                .orElseGet(() -> utils.createDiscordGuild(event.getGuild()));

        DiscordUser user = userService.findByIdDiscordUser(event.getUser().getId())
                .orElseGet(() -> utils.createDiscordUser(event.getMember().getUser()));

        GuildMember guildMember = memberService.findGuildMemberByMemberAndGuild(user, guild)
                .orElseGet(() -> utils.createGuildMember(user, guild));

        MessageEmbed messageEmbed = MessageEmbedCreator.createMessage(
                MessageEmbedCreator.createAuthorInfo(user.getUsername(), null, event.getUser().getEffectiveAvatarUrl(), null),
                "Level",
                "Your level is " + guildMember.getLevel() + " lvl",
                MessageEmbedCreator.createField("Time in voice chat", String.format("%.1f", guildMember.getVoiceTime()) + "h", false)
        );
        MessageCreateData message = MessageCreateBuilder.fromEditData(MessageEditData.fromEmbeds(messageEmbed)).build();
        event.reply(message).queue();
    }

    private void generateImage(SlashCommandInteractionEvent event) {
        event.reply("Wait, your picture is being created").queue();
        KandinskyBody kandinskyBody = new KandinskyBody(event.getOption("prompt").getAsString());
        TextChannel textChannel = event.getChannel().asTextChannel();
        try {
            textChannel
                    .sendMessage(MessageCreateData.fromFiles(FileUpload.fromData(kandinskyService.generateImage(kandinskyBody), event.getUser().getEffectiveName() + ".jpg")))
                    .queue();
        } catch (IOException e) {
            log.error(e.toString());
            textChannel.sendMessage("Your picture cannot be created. Try changing your request or wait until the next day").queue();
        }
    }

    private void aboutBot(SlashCommandInteractionEvent event) {
        JDA jda = event.getJDA();
        MessageEmbed messageEmbed = MessageEmbedCreator.createMessage(
                MessageEmbedCreator.createAuthorInfo(jda.getUserById("1116872667811823698").getEffectiveName(), null, jda.getUserById("1116872667811823698").getEffectiveAvatarUrl(), null),
                "About",
                "Hi! My name is Eva. I am a manager bot in your amazing server.\n" +
                        "I can help you track user actions and events on the server, expel those who do not participate in the life of the server, " +
                        "create an arbitrary rating of server participants by assigning them levels.\n" +
                        "The list of commands is available when you type \"/help\" \n" +
                        "You can find my code and more information about me here: https://github.com/GypsyJR777/DiscordManager"
        );
        MessageCreateData message = MessageCreateBuilder.fromEditData(MessageEditData.fromEmbeds(messageEmbed)).build();
        event.reply(message).queue();
    }

    private void help(SlashCommandInteractionEvent event) {
        List<MessageEmbed.Field> fields = new ArrayList<>();
        Arrays.stream(SlashCommand.values()).forEach(command -> {
            fields.add(MessageEmbedCreator.createField("/" + command.getSlashCommandData().getName(), command.getSlashCommandData().getDescription(), false));
        });
        MessageEmbed messageEmbed = MessageEmbedCreator.createMessage(
                "Help commands",
                "Commands list:",
                fields
        );

        MessageCreateData message = MessageCreateBuilder.fromEditData(MessageEditData.fromEmbeds(messageEmbed)).build();
        event.reply(message).queue();
    }
}
