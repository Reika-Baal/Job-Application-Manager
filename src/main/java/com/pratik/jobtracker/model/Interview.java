package com.pratik.jobtracker.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Interview {

    private int id;
    private int applicationId;
    private LocalDateTime interviewDateTime;
    private String type;
    private String location;
    private String notes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public LocalDateTime getInterviewDateTime() {
        return interviewDateTime;
    }

    public void setInterviewDateTime(LocalDateTime interviewDateTime) {
        this.interviewDateTime = interviewDateTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Interview{" +
                "id=" + id +
                ", applicationId=" + applicationId +
                ", interviewDateTime=" + interviewDateTime +
                ", type='" + type + '\'' +
                ", location='" + location + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }

    public Interview(
            int applicationId,
            LocalDateTime interviewDateTime,
            String type,
            String location,
            String notes
    ) {
        this.applicationId = applicationId;
        this.interviewDateTime = interviewDateTime;
        this.type = type;
        this.location = location;
        this.notes = notes;
    }
}
