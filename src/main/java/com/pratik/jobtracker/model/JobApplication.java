package com.pratik.jobtracker.model;

import java.time.LocalDate;

public class JobApplication {

    private int id;

    private String company;
    private String role;

    private double salary;

    private String location;

    private LocalDate applicationDate;

    private ApplicationStatus status;

    private String jobDescription;

    public JobApplication(
            String company,
            String role,
            double salary,
            String location,
            LocalDate applicationDate,
            ApplicationStatus status,
            String jobDescription
    ) {
        this.company = company;
        this.role = role;
        this.salary = salary;
        this.location = location;
        this.applicationDate = applicationDate;
        this.status = status;
        this.jobDescription = jobDescription;
    }
}