package com.github.gypsyjr777.discordmanager.service;

import com.github.gypsyjr777.discordmanager.entity.DiscordGuild;
import com.github.gypsyjr777.discordmanager.entity.DiscordRole;
import com.github.gypsyjr777.discordmanager.repository.DiscordRoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    @Autowired
    private DiscordRoleRepository roleRepository;

    public void saveRole(DiscordRole role) {
        roleRepository.save(role);
    }

    public Optional<DiscordRole> findRoleById(String roleId) {
        return roleRepository.findById(roleId);
    }

    public Optional<DiscordRole> findRoleByReaction(String reaction) {
        return roleRepository.findByReaction(reaction);
    }

    public void deleteRole(DiscordRole role) {
        roleRepository.delete(role);
    }

    public List<DiscordRole> getAllRolesByGuild(DiscordGuild guild) {
        return roleRepository.findAllByGuild(guild);
    }

    public List<DiscordRole> getAllProtectionRolesByGuild(DiscordGuild guild) {
        return roleRepository.findAllByGuildAndAndVip(guild, true);
    }
    public List<DiscordRole> getAllBasicsRolesByGuild(DiscordGuild guild) {
        return roleRepository.findAllByGuildAndBasic(guild, true);
    }
}
