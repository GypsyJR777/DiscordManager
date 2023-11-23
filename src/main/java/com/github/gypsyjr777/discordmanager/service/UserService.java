package com.github.gypsyjr777.discordmanager.service;

import com.github.gypsyjr777.discordmanager.entity.DiscordGuild;
import com.github.gypsyjr777.discordmanager.entity.DiscordUser;
import com.github.gypsyjr777.discordmanager.entity.GuildMember;
import com.github.gypsyjr777.discordmanager.repository.DiscordUserRepository;
import net.dv8tion.jda.api.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private DiscordUserRepository guildUserRepository;

    @Autowired
    private GuildMemberService guildMemberService;

    public void updateDateUser(User user, DiscordGuild guild) {
        DiscordUser guildUser = findByIdDiscordUser(user.getId()).orElseThrow();
        GuildMember guildMember = guildMemberService.findGuildMemberByMemberAndGuild(guildUser, guild).orElseGet(() -> new GuildMember(guildUser, guild));
        guildMember.addXp(1);
        guildMember.setLastOut(LocalDateTime.now());
        saveGuildUser(guildUser);
        guildMemberService.saveGuildMember(guildMember);
    }

    public void updateDateUser(User user, DiscordGuild guild, boolean isLeft) {
        DiscordUser guildUser = findByIdDiscordUser(user.getId()).orElseThrow();
        GuildMember guildMember = guildMemberService.findGuildMemberByMemberAndGuild(guildUser, guild).orElseGet(() -> new GuildMember(guildUser, guild));
        if (isLeft) {
            LocalDateTime in = guildMember.getLastVoiceTime();
            LocalDateTime out = LocalDateTime.now();

            guildMember.addVoiceTime(ChronoUnit.MINUTES.between(in, out));
        }

        guildMember.setLastVoiceTime(LocalDateTime.now());
        guildMember.setLastOut(LocalDateTime.now());
        saveGuildUser(guildUser);
        guildMemberService.saveGuildMember(guildMember);
    }

    public void createNewUser(User user, boolean isLeaveTimer, DiscordGuild guild) {
        DiscordUser discordUser = findByIdDiscordUser(user.getId()).orElseGet(() -> new DiscordUser(user));
        GuildMember guildMember = guildMemberService.findGuildMemberByMemberAndGuild(discordUser, guild).orElseGet(() -> new GuildMember(discordUser, guild));

        if (guildMember.getLastOut() == null) {
            guildMember.setLastOut(LocalDateTime.now());
        }

        guildMember.setLeaveTimer(isLeaveTimer);

        saveGuildUser(discordUser);
        guildMemberService.saveGuildMember(guildMember);
    }

    public Optional<DiscordUser> findByIdDiscordUser(String id) {
        return guildUserRepository.findById(id);
    }

    public void saveGuildUser(DiscordUser guildUser) {
        guildUserRepository.save(guildUser);
    }

    public List<DiscordUser> findAllDiscordUsers() {
        return guildUserRepository.findAll();
    }

    public void deleteGuildUser(String id) {
        guildUserRepository.deleteById(id);
    }

}
