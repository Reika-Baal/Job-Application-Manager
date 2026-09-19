package com.pratik.jobtracker.service;

import com.pratik.jobtracker.database.ApplicationStatusHistorySNL;
import com.pratik.jobtracker.database.JobApplicationSNL;

import com.pratik.jobtracker.model.JobApplication;
import com.pratik.jobtracker.model.ApplicationStatusHistory;
import com.pratik.jobtracker.model.ApplicationStatus;


import java.time.LocalDateTime;

import java.util.List;

public class ApplicationService {

    private final JobApplicationSNL snl;
    private final ApplicationStatusHistorySNL statusHistorySnl;

    public ApplicationService() {
        this.snl = new JobApplicationSNL();
        this.statusHistorySnl = new ApplicationStatusHistorySNL();
    }


    public void addApplication(JobApplication application) {

        if (application == null) {
            throw new IllegalArgumentException("Application can not be null.");

        }

        if (application.getCompany() == null || application.getCompany().isBlank()) {
            throw new IllegalArgumentException("Company can not be empty.");

        }

        if (application.getRole() == null || application.getRole().isBlank()) {
            throw new IllegalArgumentException("Role can not be empty.");
        }

        if (application.getSalary() < 0) {
            throw new IllegalArgumentException("Salary can not be negative.");
        }

        if (application.getLocation() == null || application.getLocation().isBlank()) {
            throw new IllegalArgumentException("Location must be entered.");
        }

        snl.insert(application);

        ApplicationStatusHistory history =
                new ApplicationStatusHistory(
                        application.getId(),
                        application.getStatus(),
                        LocalDateTime.now()
                );

        statusHistorySnl.insert(history);
    }

    public List<JobApplication> getAllApplications() {
        return snl.findAll();
    }

    public JobApplication getApplicationById(int id) {
        return snl.findById(id);
    }

    public boolean updateApplication(JobApplication application) {

        if (application == null) {
            throw new IllegalArgumentException("Application can not be empty.");

        }

        if (application.getId() <= 0) {
            throw new IllegalArgumentException("Application ID must be 1+.");
        }

        if (application.getCompany() == null || application.getCompany().isBlank()) {
            throw new IllegalArgumentException("Company can not be empty.");

        }

        if (application.getRole() == null || application.getRole().isBlank()) {
            throw new IllegalArgumentException("Role can not be empty.");
        }

        if (application.getSalary() < 0) {
            throw new IllegalArgumentException("Salary can not be negative.");
        }

        if (application.getLocation() == null || application.getLocation().isBlank()) {
            throw new IllegalArgumentException("Location must be entered.");
        }

        boolean updated = snl.update(application);

        if (updated &&
                !statusHistorySnl.hasReachedStatus(
                        application.getId(),
                        application.getStatus()
                )) {

            ApplicationStatusHistory history =
                    new ApplicationStatusHistory(
                            application.getId(),
                            application.getStatus(),
                            LocalDateTime.now()
                    );

            statusHistorySnl.insert(history);
        }

        return updated;
    }

    public boolean deleteApplication(int id) {

        if (id <=0) {
            throw new IllegalArgumentException("Application ID is invalid.");
        }

        return snl.delete(id);
    }

    // search company
    public List<JobApplication> searchByCompany(String company) {

        if (company == null || company.isBlank()) {
            throw new IllegalArgumentException("Company search can not be empty.");
        }
        List<JobApplication> results =  snl.findAll()
                .stream()
                .filter(app -> app.getCompany()
                        .toLowerCase()
                        .contains(company.toLowerCase()))
                .toList();

        if (results.isEmpty()) {
            throw new IllegalArgumentException(
                    "Company '" + company + "' was not found."
            );
        }

        return results;

    }

    // search role
    public List<JobApplication> searchByRole(String role) {

        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("Role search can not be empty.");
        }
        List<JobApplication> results =  snl.findAll()
                .stream()
                .filter(app -> app.getRole()
                        .toLowerCase()
                        .contains(role.toLowerCase()))
                .toList();

        if (results.isEmpty()) {
            throw new IllegalArgumentException(
                    "role '" + role + "' was not found."
            );
        }

        return results;

    }

    // search status
    public List<JobApplication> filterByStatus(ApplicationStatus status) {

        if (status == null) {
            throw new IllegalArgumentException("Status cannot be empty.");
        }

        List<JobApplication> results = snl.findAll()
                .stream()
                .filter(app -> app.getStatus() == status)
                .toList();

        if (results.isEmpty()) {
            throw new IllegalArgumentException(
                    "No applications found with status: " + status
            );
        }

        return results;
    }

    public int getTotalApplications() {
        return snl.findAll().size();
    }

    public long countByStatus(ApplicationStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status can not be null.");
        }

        return snl.findAll()
                .stream()
                .filter(app -> app.getStatus() == status)
                .count();
    }

    public double getInterviewRate() {
        int total = getTotalApplications();

        if (total == 0) {
            return 0;
        }

        long interviews = countByStatus(ApplicationStatus.INTERVIEW);

        return (double) interviews / total * 100;
    }

    public double getOfferRate() {
        int total = getTotalApplications();

        if (total == 0) {
            return 0;
        }

        long offers = countByStatus(ApplicationStatus.OFFER);

        return (double) offers / total * 100;
    }

    public int countApplicationsThatReachedStatus(
            ApplicationStatus status
    ) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null.");
        }

        return statusHistorySnl.countApplicationsThatReachedStatus(status);
    }

}
