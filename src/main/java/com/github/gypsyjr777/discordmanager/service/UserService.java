package com.github.gypsyjr777.discordmanager.service;

import com.github.gypsyjr777.discordmanager.entity.DiscordUser;
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


    public void updateDateUser(User user, boolean isVip) {
        Optional<DiscordUser> findGuildUser = findByIdGuildUser(user.getId());
        DiscordUser guildUser;

        if (findGuildUser.isEmpty()) {
            guildUser = new DiscordUser();
            guildUser.setId(user.getId());
            guildUser.setVip(isVip);
            guildUser.setLastOut(LocalDateTime.now());
            guildUser.setUsername(user.getName());
        } else {
            guildUser = findGuildUser.get();
            guildUser.setLastOut(LocalDateTime.now());
        }

        saveGuildUser(guildUser);
    }

    public Optional<DiscordUser> findByIdGuildUser(String id) {
        return guildUserRepository.findById(id);
    }

    public void saveGuildUser(DiscordUser guildUser) {
        guildUserRepository.save(guildUser);
    }

    public List<DiscordUser> findAllGuildUsers() {
        return guildUserRepository.findAll();
    }

    public void deleteGuildUser(String id) {
        guildUserRepository.deleteById(id);
    }
}
