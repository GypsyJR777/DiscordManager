package com.github.gypsyjr777.discordmanager.job.afkJob;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Date;

@Configuration
@EnableScheduling
@Slf4j
public class JobConfiguration {
    @Autowired
    private ApplicationContext context;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Scheduled(fixedRate = 30000000)
    public void launchJob() throws Exception {
        Date date = new Date();
        log.debug("scheduler starts at " + date);
        JobExecution jobExecution = jobLauncher.run(job(jobRepository, transactionManager),
                new JobParametersBuilder()
                        .addDate("launchDate", date)
                        .toJobParameters());
        log.debug("Batch job ends with status as " + jobExecution.getStatus());
        log.debug("scheduler ends ");
    }

    @Bean
    public Job job(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("AfkChecker", jobRepository)
                .start(afkCheck(jobRepository, transactionManager))
                .build();
    }

    @Bean
    protected Step afkCheck(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("afkTasklet", jobRepository)
                .tasklet(afkTasklet(), transactionManager)
                .build();
    }

    @Bean
    public AfkTasklet afkTasklet() {
        return new AfkTasklet();
    }
}
