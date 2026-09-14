package com.pratik.jobtracker;

import com.pratik.jobtracker.database.Database;
import com.pratik.jobtracker.model.ApplicationStatus;
import com.pratik.jobtracker.model.JobApplication;
import com.pratik.jobtracker.database.JobApplicationSNL;

import java.time.LocalDate;

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
    }
}
