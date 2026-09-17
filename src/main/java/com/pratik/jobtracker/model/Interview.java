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
