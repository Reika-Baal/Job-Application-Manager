package com.pratik.jobtracker;

import com.pratik.jobtracker.model.Interview;
import com.pratik.jobtracker.service.InterviewService;

import java.util.List;

public class TestInterviewService {

    public static void main(String[] args) {

        InterviewService interviewService = new InterviewService();

        List<Interview> upcoming =
                interviewService.getUpcomingInterviews(7);

        System.out.println("Upcoming interviews:");

        for (Interview interview : upcoming) {
            System.out.println(interview);
        }
    }
}