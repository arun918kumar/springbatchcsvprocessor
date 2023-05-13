package com.example.springbatchdemo.model;

public class CsvProcessRequest {

    private String jobName;

    private String csvLocation;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getCsvLocation() {
        return csvLocation;
    }

    public void setCsvLocation(String csvLocation) {
        this.csvLocation = csvLocation;
    }
}
