package com.pratik.jobtracker.service;

import com.pratik.jobtracker.model.JobApplication;
import com.pratik.jobtracker.database.JobApplicationSNL;

import java.util.List;

public class ApplicationService {
    private final JobApplicationSNL snl;

    public ApplicationService() {
        this.snl = new JobApplicationSNL();
    }

    public void addApplication(JobApplication application) {
        snl.insert(application);
    }

    public List<JobApplication> getAllApplications() {
        return snl.findAll();
    }

    public JobApplication getApplicationById(int id) {
        return snl.findById(id);
    }

    public boolean updateApplication(JobApplication application) {
        return snl.update(application);
    }

    public boolean deleteApplication(int id) {
        return snl.delete(id);
    }
}
