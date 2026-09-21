package com.pratik.jobtracker.ui;

import com.pratik.jobtracker.model.ApplicationStatus;
import com.pratik.jobtracker.model.JobApplication;
import com.pratik.jobtracker.service.ApplicationService;

import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AddApplicationView {
    private final ApplicationService service;
    private final Runnable onApplicationAdded;

    public AddApplicationView(ApplicationService service, Runnable onApplicationAdded) {
        this.service = service;
        this.onApplicationAdded = onApplicationAdded;
    }

    public void show() {

        Stage stage = new Stage();

        TextField companyField = new TextField();
        companyField.setPromptText("Company");

        TextField roleField = new TextField();
        roleField.setPromptText("Role");

        TextField salaryField = new TextField();
        salaryField.setPromptText("Salary");

        TextField locationField = new TextField();
        locationField.setPromptText("Location");

        DatePicker datePicker = new DatePicker(LocalDate.now());

        ComboBox<ApplicationStatus> statusBox =
                new ComboBox<>(FXCollections.observableArrayList(
                        ApplicationStatus.values()
                ));

        statusBox.setValue(ApplicationStatus.APPLIED);

        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Job Description");

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
                salaryField,
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

        Scene scene = new Scene(root, 400, 500);

        stage.setTitle("Add Application");
        stage.setScene(scene);
        stage.show();
    }
}
