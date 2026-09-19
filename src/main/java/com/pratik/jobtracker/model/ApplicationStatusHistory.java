package com.pratik.jobtracker.model;

import java.time.LocalDateTime;

public class ApplicationStatusHistory {

    private int id;
    private int applicationId;
    private ApplicationStatus status;
    private LocalDateTime reachedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getApplicationId() {
        return applicationId;
    }

    @Override
    public String toString() {
        return "ApplicationStatusHistory{" +
                "id=" + id +
                ", applicationId=" + applicationId +
                ", status=" + status +
                ", reachedAt=" + reachedAt +
                '}';
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public LocalDateTime getReachedAt() {
        return reachedAt;
    }

    public void setReachedAt(LocalDateTime reachedAt) {
        this.reachedAt = reachedAt;
    }

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