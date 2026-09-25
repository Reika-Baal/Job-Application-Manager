package com.pratik.jobtracker.ui;

import com.pratik.jobtracker.model.ApplicationStatus;
import com.pratik.jobtracker.model.JobApplication;
import com.pratik.jobtracker.service.ApplicationService;

import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EditApplicationView {

    private final ApplicationService service;
    private final JobApplication application;
    private final Runnable onApplicationUpdated;

    public EditApplicationView(
            ApplicationService service,
            JobApplication application,
            Runnable onApplicationUpdated
    ) {
        this.service = service;
        this.application = application;
        this.onApplicationUpdated = onApplicationUpdated;
    }

    public void show() {

        Stage stage = new Stage();

        TextField companyField = new TextField(application.getCompany());
        TextField roleField = new TextField(application.getRole());
        TextField salaryField =
                new TextField(String.valueOf(application.getSalary()));
        TextField locationField = new TextField(application.getLocation());

        DatePicker datePicker =
                new DatePicker(application.getApplicationDate());

        ComboBox<ApplicationStatus> statusBox =
                new ComboBox<>(FXCollections.observableArrayList(
                        ApplicationStatus.values()
                ));

        statusBox.setValue(application.getStatus());

        TextArea descriptionArea =
                new TextArea(application.getJobDescription());

        TextArea notesArea = new TextArea();
        notesArea.setWrapText(true);
        notesArea.setPrefRowCount(3);
        notesArea.setText(application.getNotes());

        Button saveButton = new Button("Save Changes");

        Label messageLabel = new Label();

        saveButton.setOnAction(event -> {

            try {
                double salary =
                        Double.parseDouble(salaryField.getText());

                application.setCompany(companyField.getText());
                application.setRole(roleField.getText());
                application.setSalary(salary);
                application.setLocation(locationField.getText());
                application.setApplicationDate(datePicker.getValue());
                application.setStatus(statusBox.getValue());
                application.setJobDescription(descriptionArea.getText());
                application.setNotes(notesArea.getText());

                boolean updated =
                        service.updateApplication(application);

                if (updated) {
                    onApplicationUpdated.run();
                    stage.close();
                } else {
                    messageLabel.setText(
                            "Application could not be updated."
                    );
                }

            } catch (NumberFormatException e) {

                messageLabel.setText(
                        "Salary must be a number."
                );

            } catch (IllegalArgumentException e) {

                messageLabel.setText(e.getMessage());
            }
        });

        VBox root = new VBox(
                10,
                new Label("Edit Application"),
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

        root.getStyleClass().add("panel");

        Scene scene = new Scene(root, 400, 500);

        scene.getStylesheets().add(
                getClass().getResource("/styles.css").toExternalForm()
        );

        stage.setTitle("Edit Application");
        stage.setScene(scene);
        stage.show();
    }
}