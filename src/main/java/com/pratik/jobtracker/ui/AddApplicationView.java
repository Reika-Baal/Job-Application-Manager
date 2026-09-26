package com.pratik.jobtracker.ui;

import com.pratik.jobtracker.model.ApplicationStatus;
import com.pratik.jobtracker.model.JobApplication;
import com.pratik.jobtracker.service.ApplicationService;

import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AddApplicationView {
    private final ApplicationService service;
    private final Runnable onApplicationAdded;
    private static Stage openStage;

    public AddApplicationView(ApplicationService service, Runnable onApplicationAdded) {
        this.service = service;
        this.onApplicationAdded = onApplicationAdded;
    }

    public void show() {

        if (openStage != null && openStage.isShowing()) {
            openStage.toFront();
            openStage.requestFocus();
            return;
        }

        Stage stage = new Stage();

        openStage = stage;

        TextField companyField = new TextField();
        companyField.setPromptText("Company");

        TextField roleField = new TextField();
        roleField.setPromptText("Role");

        TextField salaryField = new TextField();
        salaryField.setPromptText("Salary");

        Label poundLabel = new Label("£");

        poundLabel.getStyleClass().add("currency-symbol");

        HBox salaryBox = new HBox(
                2,
                poundLabel,
                salaryField
        );

        salaryBox.getStyleClass().add("currency-field");

        salaryBox.setAlignment(
                javafx.geometry.Pos.CENTER_LEFT
        );

        HBox.setHgrow(
                salaryField,
                javafx.scene.layout.Priority.ALWAYS
        );

        TextField locationField = new TextField();
        locationField.setPromptText("Location");

        DatePicker datePicker = new DatePicker(LocalDate.now());

        ComboBox<ApplicationStatus> statusBox =
                new ComboBox<>(FXCollections.observableArrayList(
                        ApplicationStatus.values()
                ));

        statusBox.setValue(ApplicationStatus.APPLIED);

        statusBox.setPrefHeight(36);
        statusBox.setMinHeight(36);
        statusBox.setMaxHeight(36);

        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Job Description");

        descriptionArea.setPrefHeight(120);
        descriptionArea.setMinHeight(120);
        descriptionArea.setMaxHeight(120);

        TextArea notesArea = new TextArea();
        notesArea.setPromptText("Notes");
        notesArea.setWrapText(true);
        notesArea.setPrefRowCount(3);

        Button saveButton = new Button("Save");

        Label messageLabel = new Label();

        saveButton.setOnAction(event -> {

            try {
                double salary = Double.parseDouble(salaryField.getText());

                JobApplication application = new JobApplication(
                        companyField.getText(),
                        roleField.getText(),
                        salary,
                        locationField.getText(),
                        datePicker.getValue(),
                        statusBox.getValue(),
                        descriptionArea.getText(),
                        notesArea.getText()
                );

                service.addApplication(application);

                onApplicationAdded.run();

                stage.close();

            } catch (NumberFormatException e) {

                messageLabel.setText("Salary must be a number.");

            } catch (IllegalArgumentException e) {

                messageLabel.setText(e.getMessage());
            }
        });

        VBox root = new VBox(
                10,
                new Label("Add Application"),
                companyField,
                roleField,
                salaryBox,
                locationField,
                datePicker,
                statusBox,
                new Label("Job Description"),
                descriptionArea,
                new Label("Notes"),
                notesArea,
                saveButton,
                messageLabel
        );

        root.getStyleClass().add("panel");

        Scene scene = new Scene(root, 400, 650);

        scene.getStylesheets().add(
                getClass().getResource("/styles.css").toExternalForm()
        );

        stage.setTitle("Add Application");
        stage.setScene(scene);
        stage.setOnHidden(event -> openStage = null);
        stage.show();
    }
}
