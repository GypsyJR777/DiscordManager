package com.github.gypsyjr777.discordmanager.service;

import com.github.gypsyjr777.discordmanager.entity.DiscordGuild;
import com.github.gypsyjr777.discordmanager.entity.DiscordUser;
import com.github.gypsyjr777.discordmanager.entity.GuildMember;
import com.github.gypsyjr777.discordmanager.repository.DiscordUserRepository;
import net.dv8tion.jda.api.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private DiscordUserRepository guildUserRepository;

    @Autowired
    private GuildMemberService guildMemberService;


    public void updateDateUser(User user, boolean isVip, DiscordGuild guild) {
        DiscordUser guildUser = findByIdGuildUser(user.getId()).orElse(new DiscordUser(user));
        GuildMember guildMember = guildMemberService.findGuildMemberByMemberAndGuild(guildUser, guild).orElse(new GuildMember(guildUser, guild));
        guildMember.setVip(isVip);
        guildMember.setLastOut(LocalDateTime.now());
        saveGuildUser(guildUser);
        guildMemberService.saveGuildMember(guildMember);
    }

    public Optional<DiscordUser> findByIdGuildUser(String id) {
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
