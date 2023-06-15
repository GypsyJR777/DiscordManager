package com.github.gypsyjr777.discordmanager.service;

import com.github.gypsyjr777.discordmanager.repository.GuildMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GuildMemberService {
    @Autowired
    private GuildMemberRepository guildMemberRepository;

}
