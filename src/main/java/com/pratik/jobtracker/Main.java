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

        // create entry test
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

        // update ID test
        int idToUpdate = 6;

        JobApplication applicationToUpdate = snl.findById(idToUpdate);

        if (applicationToUpdate != null) {

            applicationToUpdate.setStatus(ApplicationStatus.INTERVIEW);

            boolean updated = snl.update(applicationToUpdate);

            if (updated) {
                System.out.println("Application updated successfully:");
                System.out.println(applicationToUpdate);
            } else {
                System.out.println("Application could not be updated.");
            }

        } else {
            System.out.println("Application with ID " + idToUpdate + " was not found.");
        }

        // delete ID test
        int idToDelete = 5;

        JobApplication applicationToDelete = snl.findById(idToDelete);

        if (applicationToDelete != null) {

            boolean deleted = snl.delete(idToDelete);

            if (deleted) {
                System.out.println("Application deleted successfully:");
                System.out.println(applicationToDelete);
            } else {
                System.out.println("Application could not be deleted.");
            }

        } else {
            System.out.println("Application with ID " + idToDelete + " was not found.");
        }


        System.out.println("\nAll applications:");

        List<JobApplication> applications = snl.findAll();

        for (JobApplication app : applications) {
            System.out.println(app);
        }
    }
}