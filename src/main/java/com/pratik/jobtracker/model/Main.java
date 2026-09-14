package com.pratik.jobtracker.model;

import com.pratik.jobtracker.model.ApplicationStatus;
import com.pratik.jobtracker.model.JobApplication;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        JobApplication application = new JobApplication(
                "Test Job App",
                "Graduate Software Eng",
                30000,
                "London",
                LocalDate.now(),
                ApplicationStatus.APPLIED,
                "Graduate Software Eng with training."
        );
        System.out.println(application);
    }
}
