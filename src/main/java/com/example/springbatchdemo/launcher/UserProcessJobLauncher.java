package com.example.springbatchdemo.launcher;

import com.example.springbatchdemo.model.UserCsv;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class UserProcessJobLauncher {

    @Autowired
    private JobLauncher jobLauncher;

    @Qualifier("persistUserJob")
    @Autowired
    private Job userJob;

    @Autowired
    private FlatFileItemReader<UserCsv> flatFileItemReader;

    @Async
    public void startUserJob(String csvPath) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        flatFileItemReader.setResource(new FileSystemResource(csvPath));
        jobLauncher.run(userJob, new JobParameters(Map.of("start_time",new JobParameter(Timestamp.valueOf(LocalDateTime.now())))));
    }

}
