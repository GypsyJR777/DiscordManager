package com.github.gypsyjr777.discordmanager.repository;

import com.github.gypsyjr777.discordmanager.entity.DiscordRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscordRoleRepository extends JpaRepository<DiscordRole, String> {
}
