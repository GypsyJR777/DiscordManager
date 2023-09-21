package com.github.gypsyjr777.discordmanager.service;

import com.github.gypsyjr777.discordmanager.entity.DiscordRole;
import com.github.gypsyjr777.discordmanager.entity.DiscordUser;
import com.github.gypsyjr777.discordmanager.entity.UserRole;
import com.github.gypsyjr777.discordmanager.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserRoleService {
    @Autowired
    private final UserRoleRepository userRoleRepository;

    public UserRoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public void saveUserRole(UserRole userRole){
        userRoleRepository.save(userRole);
    }

    public Optional<UserRole> getUserRoleById(UUID id) {
        return userRoleRepository.findById(id);
    }

    public Set<UserRole> getAllByRole(DiscordRole role) {
        return userRoleRepository.findAllByRole(role);
    }

    public Set<UserRole> getAllByUser(DiscordUser user) {
        return userRoleRepository.findAllByUser(user);
    }

    public void deleteUserRole(UserRole userRole) {
        userRoleRepository.delete(userRole);
    }

    public Optional<UserRole> getByRoleAndUser(DiscordRole discordRole, DiscordUser discordUser) {
        return userRoleRepository.findByUserAndRole(discordUser, discordRole);
    }
}
