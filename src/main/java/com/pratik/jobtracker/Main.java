package com.pratik.jobtracker;

import com.pratik.jobtracker.database.Database;
import com.pratik.jobtracker.model.ApplicationStatus;
import com.pratik.jobtracker.model.JobApplication;
import com.pratik.jobtracker.database.JobApplicationSNL;

import java.time.LocalDate;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        Database.initialiseDatabase();

        JobApplication application = new JobApplication(
                "Test Job App",
                "Graduate Software Eng",
                30000,
                "London",
                LocalDate.now(),
                ApplicationStatus.APPLIED,
                "Graduate Software Eng with training."
        );

        JobApplicationSNL snl = new JobApplicationSNL();

        snl.insert(application);

        System.out.println("Saved application with ID: " + application.getId());

        List<JobApplication> applications = snl.findAll();

        for (JobApplication app : applications) {
            System.out.println(app);
        }

        application.setStatus(ApplicationStatus.INTERVIEW);

        snl.update(application);

        System.out.println("Updated Application");
        System.out.println(application);
    }
}
