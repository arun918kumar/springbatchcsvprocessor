package com.example.springbatchdemo.controller;

import com.example.springbatchdemo.launcher.UserProcessJobLauncher;
import com.example.springbatchdemo.model.CsvProcessRequest;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserProcessController {

    @Autowired
    private UserProcessJobLauncher userProcessJobLauncher;

    private static final String USER_PROCESS = "USER_PROCESS";

    @PostMapping("/startjob")
    @Operation(description = "Rest api to start the csv processing \n\nsample request : {\n" +
            "  \"jobName\": \"USER_PROCESS\",\n" +
            "  \"csvLocation\": \"C:\\\\\\springbatchdemo\\\\\\springbatchdemo\\\\\\inboundCsvFolder\\\\\\user.csv\"\n" +
            "}")
    public ResponseEntity startJob(@RequestBody CsvProcessRequest csvProcessRequest) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        if (csvProcessRequest.getJobName().toUpperCase().equals(USER_PROCESS)){
            userProcessJobLauncher.startUserJob(csvProcessRequest.getCsvLocation());
            return ResponseEntity.ok("Job "+csvProcessRequest.getJobName()+" has been started.");
        }
        return new ResponseEntity("Please provide the correct Job name", HttpStatus.BAD_REQUEST);
       }

}
