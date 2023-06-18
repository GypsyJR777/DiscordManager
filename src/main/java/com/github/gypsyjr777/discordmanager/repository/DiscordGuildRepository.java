package com.github.gypsyjr777.discordmanager.repository;

import com.github.gypsyjr777.discordmanager.entity.DiscordGuild;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscordGuildRepository extends JpaRepository<DiscordGuild, String> {
    List<DiscordGuild> findAllByHaveVips(boolean haveVips);
}
