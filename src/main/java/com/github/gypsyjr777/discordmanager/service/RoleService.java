package com.github.gypsyjr777.discordmanager.service;

import com.github.gypsyjr777.discordmanager.entity.DiscordRole;
import com.github.gypsyjr777.discordmanager.repository.DiscordRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class RoleService {
    private final DiscordRoleRepository roleRepository;

    @Autowired
    public RoleService(DiscordRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void saveRole(DiscordRole role) {
        roleRepository.save(role);
    }

    public Optional<DiscordRole> findRoleById(String roleId) {
        return roleRepository.findById(roleId);
    }

    public Optional<DiscordRole> findRoleByReaction(String reaction) {
        return roleRepository.findByReaction(reaction);
    }
}
