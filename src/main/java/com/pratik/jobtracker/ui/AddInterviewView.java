package com.pratik.jobtracker.ui;

import com.pratik.jobtracker.model.Interview;
import com.pratik.jobtracker.model.JobApplication;
import com.pratik.jobtracker.service.InterviewService;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AddInterviewView {

    private final JobApplication application;
    private final InterviewService interviewService;
    private final Runnable onInterviewAdded;

    public AddInterviewView(
            JobApplication application,
            InterviewService interviewService,
            Runnable onInterviewAdded
    ) {
        this.application = application;
        this.interviewService = interviewService;
        this.onInterviewAdded = onInterviewAdded;
    }

    public void show() {

        Stage stage = new Stage();

        DatePicker datePicker = new DatePicker(LocalDate.now());

        TextField timeField = new TextField();

        timeField.setPromptText("Time (HH:MM)");

        timeField.textProperty().addListener((observable, oldValue, newValue) -> {

            String digits = newValue.replaceAll("\\D", "");

            if (digits.length() > 4) {
                digits = digits.substring(0, 4);
            }

            String formatted;

            if (digits.length() >= 3) {
                formatted = digits.substring(0, 2)
                        + ":"
                        + digits.substring(2);
            } else {
                formatted = digits;
            }

            if (!newValue.equals(formatted)) {
                timeField.setText(formatted);
                timeField.positionCaret(formatted.length());
            }
        });

        TextField typeField = new TextField();
        typeField.setPromptText("Interview Type");

        TextField locationField = new TextField();
        locationField.setPromptText("Location or meeting link");

        TextArea notesArea = new TextArea();
        notesArea.setPromptText("Interview Notes");

        notesArea.setWrapText(true);
        notesArea.setPrefHeight(70);
        notesArea.setMinHeight(70);
        notesArea.setMaxHeight(70);

        Button saveButton = new Button("Save Interview");

        Label messageLabel = new Label();

        saveButton.setOnAction(event -> {

            try {
                LocalDate date = datePicker.getValue();

                LocalTime time =
                        LocalTime.parse(timeField.getText());

                LocalDateTime interviewDateTime =
                        LocalDateTime.of(date, time);

                Interview interview = new Interview(
                        application.getId(),
                        interviewDateTime,
                        typeField.getText(),
                        locationField.getText(),
                        notesArea.getText()
                );

                interviewService.addInterview(interview);

                onInterviewAdded.run();

                stage.close();

            } catch (Exception e) {
                messageLabel.setText(
                        "Please enter a valid date and time. Time format: HH:MM"
                );
            }
        });

        VBox root = new VBox(
                10,
                new Label("Add Interview"),
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

        root.getStyleClass().add("panel");

        Scene scene = new Scene(root, 400, 500);

        scene.getStylesheets().add(
                getClass().getResource("/styles.css").toExternalForm()
        );

        stage.setTitle("Add Interview");
        stage.setScene(scene);
        stage.show();
    }
}