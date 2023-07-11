package com.github.gypsyjr777.discordmanager.service;

import com.github.gypsyjr777.discordmanager.entity.DiscordGuild;
import com.github.gypsyjr777.discordmanager.entity.DiscordUser;
import com.github.gypsyjr777.discordmanager.entity.GuildMember;
import com.github.gypsyjr777.discordmanager.repository.DiscordGuildRepository;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GuildService {
    @Autowired
    private ApplicationContext context;
    @Autowired
    private GuildMemberService memberService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private DiscordGuildRepository guildRepository;

    public void findAndKickAfk(DiscordGuild guild) {
        Set<GuildMember> guildMembers = new HashSet<>(memberService.getAllGuildMembers(guild));
        guildMembers.forEach(user -> {
            LocalDateTime userLastVisit = user.getLastOut();
            if (ChronoUnit.DAYS.between(userLastVisit, LocalDateTime.now()) > 30 && !user.isLeaveTimer()) {
                removeUser(user.getMember(), guild);
            }
        });
    }

    public void removeUser(DiscordUser user, DiscordGuild discordGuild) {
        //TODO поправить локальную переменную JDA, сделать глобалом
        JDA jda = context.getBean(JDA.class);
        Guild guild = jda.getGuildById(discordGuild.getId());
        assert guild != null;
        log.info("Kick {} from {}", user.getId(), discordGuild.getId());
        guild.kick(Objects.requireNonNull(jda.getUserById(user.getId()))).queue();
        memberService.deleteGuildUser(user, discordGuild);
    }

    public Optional<DiscordGuild> findGuildById(String id) {
        return guildRepository.findById(id);
    }

    public List<DiscordGuild> findAllGuildsWithLeaveTimers() {
        return guildRepository.findAllByHaveLeaveTimer(true);
    }

    public void saveGuild(DiscordGuild guild) {
        guildRepository.save(guild);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteGuild(DiscordGuild guild) {

        roleService.getAllRolesByGuild(guild).forEach(role ->
                roleService.deleteRole(role)
        );

        memberService.getAllGuildMembers(guild).forEach(guildMember ->
                memberService.deleteGuildMember(guildMember)
        );

        guildRepository.delete(guild);
    }
}
