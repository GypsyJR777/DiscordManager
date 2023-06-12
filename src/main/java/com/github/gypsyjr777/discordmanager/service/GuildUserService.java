package com.github.gypsyjr777.discordmanager.service;

import com.github.gypsyjr777.discordmanager.entity.DiscordGuildUser;
import com.github.gypsyjr777.discordmanager.repository.DiscordGuildUserRepository;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GuildUserService {
    private final DiscordGuildUserRepository guildUserRepository;

    @Autowired
    public GuildUserService(DiscordGuildUserRepository guildUserRepository) {
        this.guildUserRepository = guildUserRepository;
    }

    public void updateDateUser(User user, boolean isVip) {
        Optional<DiscordGuildUser> findGuildUser = findByIdGuildUser(user.getId());
        DiscordGuildUser guildUser;

        if (findGuildUser.isEmpty()) {
            guildUser = new DiscordGuildUser();
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

    public Optional<DiscordGuildUser> findByIdGuildUser(String id) {
        return guildUserRepository.findById(id);
    }

    public void saveGuildUser(DiscordGuildUser guildUser) {
        guildUserRepository.save(guildUser);
    }

    public List<DiscordGuildUser> findAllGuildUsers() {
        return guildUserRepository.findAll();
    }

    public void deleteGuildUser(String id) {
        guildUserRepository.deleteById(id);
    }
}
