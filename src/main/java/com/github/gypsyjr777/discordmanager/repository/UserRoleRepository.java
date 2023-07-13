package com.github.gypsyjr777.discordmanager.repository;

import com.github.gypsyjr777.discordmanager.entity.DiscordRole;
import com.github.gypsyjr777.discordmanager.entity.DiscordUser;
import com.github.gypsyjr777.discordmanager.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {
    Set<UserRole> findAllByRole(DiscordRole role);

    Set<UserRole> findAllByUser(DiscordUser user);

    Optional<UserRole> findByUserAndRole(DiscordUser user, DiscordRole role);
}
