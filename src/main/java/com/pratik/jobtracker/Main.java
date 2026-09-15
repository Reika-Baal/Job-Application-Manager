package com.pratik.jobtracker;

import com.pratik.jobtracker.database.Database;
import com.pratik.jobtracker.database.JobApplicationSNL;
import com.pratik.jobtracker.model.ApplicationStatus;
import com.pratik.jobtracker.model.JobApplication;

import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        Database.initialiseDatabase();

        JobApplicationSNL snl = new JobApplicationSNL();

        JobApplication application = new JobApplication(
                "Test Job App",
                "Graduate Software Eng",
                30000,
                "London",
                LocalDate.now(),
                ApplicationStatus.APPLIED,
                "Graduate Software Eng with training."
        );

        snl.insert(application);

        System.out.println("Saved application with ID: " + application.getId());

        // update
        JobApplication applicationToUpdate = snl.findById(6);

        if (applicationToUpdate != null) {

            applicationToUpdate.setStatus(ApplicationStatus.INTERVIEW);

            snl.update(applicationToUpdate);

            System.out.println("Updated Application:");
            System.out.println(applicationToUpdate);

        } else {
            System.out.println("Application with ID 6 was not found.");
        }

        System.out.println("\nAll applications:");

        List<JobApplication> applications = snl.findAll();

        for (JobApplication app : applications) {
            System.out.println(app);
        }
    }
}