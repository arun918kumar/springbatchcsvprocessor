package com.example.springbatchdemo.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class UserProcessJobListener implements JobExecutionListener {
    private final Logger LOGGER = LoggerFactory.getLogger(UserProcessJobListener.class);

    @Override
    public void beforeJob(JobExecution jobExecution) {
        LOGGER.info("\nStarted processing user csv file at {} ", Timestamp.valueOf(LocalDateTime.now()));
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        LOGGER.info("\nUsers successfully saved to database at {} ",Timestamp.valueOf(LocalDateTime.now()));
    }
}
