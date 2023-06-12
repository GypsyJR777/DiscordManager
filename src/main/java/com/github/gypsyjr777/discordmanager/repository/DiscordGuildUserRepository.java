package com.github.gypsyjr777.discordmanager.repository;

import com.github.gypsyjr777.discordmanager.entity.DiscordGuildUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscordGuildUserRepository extends JpaRepository<DiscordGuildUser, String> {
}
