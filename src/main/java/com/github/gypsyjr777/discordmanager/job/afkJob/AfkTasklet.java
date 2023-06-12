package com.github.gypsyjr777.discordmanager.job.afkJob;

import com.github.gypsyjr777.discordmanager.service.GuildService;
import com.github.gypsyjr777.discordmanager.service.GuildUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
public class AfkTasklet implements Tasklet {
    @Autowired
    private GuildUserService guildUserService;

    @Autowired
    private GuildService guildService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("ALL IS GOOD!!!");
        guildService.findAndKickAfk(guildUserService.findAllGuildUsers());
        return RepeatStatus.FINISHED;
    }
}
