package com.pratik.jobtracker.service;

import com.pratik.jobtracker.database.InterviewSNL;
import com.pratik.jobtracker.model.Interview;

import java.util.List;

public class InterviewService {

    private final InterviewSNL snl;

    public InterviewService() {
        this.snl = new InterviewSNL();
    }

    public void addInterview(Interview interview) {

        if (interview == null) {
            throw new IllegalArgumentException("Interview can not be null.");
        }

        if (interview.getApplicationId() <= 0) {
            throw new IllegalArgumentException("Application ID is invalid.");
        }

        if (interview.getInterviewDateTime() == null) {
            throw new IllegalArgumentException("Interview date and time must be entered.");
        }

        if (interview.getType() == null || interview.getType().isBlank()) {
            throw new IllegalArgumentException("Interview type must be entered.");
        }

        if (interview.getLocation() == null || interview.getLocation().isBlank()) {
            throw new IllegalArgumentException("Interview location must be entered.");
        }

        snl.insert(interview);
    }

    public List<Interview> getInterviewsForApplication(int applicationId) {

        if (applicationId <= 0) {
            throw new IllegalArgumentException("Application ID is invalid.");
        }

        return snl.findByApplicationId(applicationId);
    }

    public boolean updateInterview(Interview interview) {

        if (interview == null) {
            throw new IllegalArgumentException("Interview can not be null.");
        }

        if (interview.getId() <= 0) {
            throw new IllegalArgumentException("Interview ID is invalid.");
        }

        if (interview.getInterviewDateTime() == null) {
            throw new IllegalArgumentException("Interview date and time must be entered.");
        }

        if (interview.getType() == null || interview.getType().isBlank()) {
            throw new IllegalArgumentException("Interview type must be entered.");
        }

        if (interview.getLocation() == null || interview.getLocation().isBlank()) {
            throw new IllegalArgumentException("Interview location must be entered.");
        }

        return snl.update(interview);
    }

    public boolean deleteInterview(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException("Interview ID is invalid.");
        }

        return snl.delete(id);
    }
}