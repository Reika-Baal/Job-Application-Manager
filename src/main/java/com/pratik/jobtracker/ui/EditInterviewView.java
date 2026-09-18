package com.pratik.jobtracker.ui;

import com.pratik.jobtracker.model.Interview;
import com.pratik.jobtracker.service.InterviewService;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class EditInterviewView {

    private final Interview interview;
    private final InterviewService interviewService;
    private final Runnable onInterviewUpdated;

    public EditInterviewView(
            Interview interview,
            InterviewService interviewService,
            Runnable onInterviewUpdated
    ) {
        this.interview = interview;
        this.interviewService = interviewService;
        this.onInterviewUpdated = onInterviewUpdated;
    }

    public void show() {

        Stage stage = new Stage();

        LocalDateTime currentDateTime = interview.getInterviewDateTime();

        DatePicker datePicker =
                new DatePicker(currentDateTime.toLocalDate());

        TextField timeField =
                new TextField(currentDateTime.toLocalTime().toString());

        TextField typeField =
                new TextField(interview.getType());

        TextField locationField =
                new TextField(interview.getLocation());

        TextArea notesArea =
                new TextArea(interview.getNotes());

        Button saveButton = new Button("Save Changes");

        Label messageLabel = new Label();

        saveButton.setOnAction(event -> {

            try {
                LocalDate date = datePicker.getValue();
                LocalTime time = LocalTime.parse(timeField.getText());

                interview.setInterviewDateTime(
                        LocalDateTime.of(date, time)
                );

                interview.setType(typeField.getText());
                interview.setLocation(locationField.getText());
                interview.setNotes(notesArea.getText());

                boolean updated =
                        interviewService.updateInterview(interview);

                if (updated) {
                    onInterviewUpdated.run();
                    stage.close();
                } else {
                    messageLabel.setText(
                            "Interview could not be updated."
                    );
                }

            } catch (Exception e) {
                messageLabel.setText(
                        "Please enter a valid date and time. Time format: HH:MM"
                );
            }
        });

        VBox root = new VBox(
                10,
                new Label("Edit Interview"),
                new Label("Date"),
                datePicker,
                new Label("Time"),
                timeField,
                new Label("Type"),
                typeField,
                new Label("Location"),
                locationField,
                new Label("Notes"),
                notesArea,
                saveButton,
                messageLabel
        );

        Scene scene = new Scene(root, 400, 550);

        stage.setTitle("Edit Interview");
        stage.setScene(scene);
        stage.show();
    }
}