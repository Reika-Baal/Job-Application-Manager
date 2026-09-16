package com.pratik.jobtracker.service;

import com.pratik.jobtracker.model.JobApplication;
import com.pratik.jobtracker.database.JobApplicationSNL;
import com.pratik.jobtracker.model.ApplicationStatus;

import java.util.List;

public class ApplicationService {
    private final JobApplicationSNL snl;

    public ApplicationService() {
        this.snl = new JobApplicationSNL();
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

        return snl.update(application);
    }

    public boolean deleteApplication(int id) {

        if (id <=0) {
            throw new IllegalArgumentException("Application ID is invalid.");
        }

        return snl.delete(id);
    }

    // search company
    public List<JobApplication> searchByCompany(String company) {
        return snl.findAll()
                .stream()
                .filter(app -> app.getCompany()
                        .toLowerCase()
                        .contains(company.toLowerCase()))
                .toList();
    }

    // search role
    public List<JobApplication> searchByRole(String role) {
        return snl.findAll()
                .stream()
                .filter(app -> app.getRole()
                        .toLowerCase()
                        .contains(role.toLowerCase()))
                .toList();
    }

    // search status
    public List<JobApplication> filterByStatus(ApplicationStatus status) {
        return snl.findAll()
                .stream()
                .filter(app -> app.getStatus() == status)
                .toList();
    }
}
