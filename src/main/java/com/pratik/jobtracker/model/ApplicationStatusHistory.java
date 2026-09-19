package com.pratik.jobtracker.model;

import java.time.LocalDateTime;

public class ApplicationStatusHistory {

    private int id;
    private int applicationId;
    private ApplicationStatus status;
    private LocalDateTime reachedAt;

    public ApplicationStatusHistory(
            int applicationId,
            ApplicationStatus status,
            LocalDateTime reachedAt
    ) {
        this.applicationId = applicationId;
        this.status = status;
        this.reachedAt = reachedAt;
    }
}