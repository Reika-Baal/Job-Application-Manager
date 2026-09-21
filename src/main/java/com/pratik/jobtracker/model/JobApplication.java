package com.pratik.jobtracker.model;

import java.time.LocalDate;

public class JobApplication {

    private int id;

    private String company;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    private String notes;

    private String role;

    private double salary;

    private String location;

    private LocalDate applicationDate;

    private ApplicationStatus status;

    private String jobDescription;

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "JobApplication{" +
                "id=" + id +
                ", company='" + company + '\'' +
                ", notes='" + notes + '\'' +
                ", role='" + role + '\'' +
                ", salary=" + salary +
                ", location='" + location + '\'' +
                ", applicationDate=" + applicationDate +
                ", status=" + status +
                ", jobDescription='" + jobDescription + '\'' +
                '}';
    }

    public JobApplication(
            String company,
            String role,
            double salary,
            String location,
            LocalDate applicationDate,
            ApplicationStatus status,
            String jobDescription,
            String notes
    ) {
        this.company = company;
        this.role = role;
        this.salary = salary;
        this.location = location;
        this.applicationDate = applicationDate;
        this.status = status;
        this.jobDescription = jobDescription;
        this.notes = notes;
    }
}