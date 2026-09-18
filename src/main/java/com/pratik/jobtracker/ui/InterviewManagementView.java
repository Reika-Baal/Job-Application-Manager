package com.pratik.jobtracker.ui;

import com.pratik.jobtracker.model.Interview;
import com.pratik.jobtracker.model.JobApplication;
import com.pratik.jobtracker.service.InterviewService;
import com.pratik.jobtracker.ui.AddInterviewView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class InterviewManagementView {

    private final JobApplication application;
    private final InterviewService interviewService;

    private final ObservableList<Interview> interviews =
            FXCollections.observableArrayList();

    public InterviewManagementView(JobApplication application) {
        this.application = application;
        this.interviewService = new InterviewService();
    }

    public void show() {

        Stage stage = new Stage();

        Label title = new Label(
                "Interviews - " +
                        application.getCompany() +
                        " - " +
                        application.getRole()
        );

        TableView<Interview> table = new TableView<>();

        TableColumn<Interview, String> dateColumn =
                new TableColumn<>("Date / Time");

        dateColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue()
                                .getInterviewDateTime()
                                .toString()
                )
        );

        TableColumn<Interview, String> typeColumn =
                new TableColumn<>("Type");

        typeColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getType()
                )
        );

        TableColumn<Interview, String> locationColumn =
                new TableColumn<>("Location");

        locationColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getLocation()
                )
        );

        table.getColumns().addAll(
                dateColumn,
                typeColumn,
                locationColumn
        );

        refreshInterviews();

        table.setItems(interviews);

        Button addButton = new Button("Add Interview");
        Button editButton = new Button("Edit Interview");
        Button deleteButton = new Button("Delete Interview");

        editButton.setDisable(true);
        deleteButton.setDisable(true);

        table.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldSelection, newSelection) -> {

                    boolean nothingSelected = newSelection == null;

                    editButton.setDisable(nothingSelected);
                    deleteButton.setDisable(nothingSelected);
                });

        TextArea notesArea = new TextArea();
        notesArea.setEditable(false);
        notesArea.setWrapText(true);
        notesArea.setPromptText("Interview notes");

        addButton.setOnAction(event -> {

            AddInterviewView addView =
                    new AddInterviewView(
                            application,
                            interviewService,
                            this::refreshInterviews
                    );

            addView.show();
        });

        editButton.setOnAction(event -> {

            Interview selectedInterview =
                    table.getSelectionModel().getSelectedItem();

            if (selectedInterview == null) {
                return;
            }

            EditInterviewView editView =
                    new EditInterviewView(
                            selectedInterview,
                            interviewService,
                            this::refreshInterviews
                    );

            editView.show();
        });

        deleteButton.setOnAction(event -> {

            Interview selectedInterview =
                    table.getSelectionModel().getSelectedItem();

            if (selectedInterview == null) {
                return;
            }

            Alert confirmation =
                    new Alert(Alert.AlertType.CONFIRMATION);

            confirmation.setTitle("Delete Interview");
            confirmation.setHeaderText("Delete this interview?");
            confirmation.setContentText(
                    "This action cannot be undone."
            );

            confirmation.showAndWait().ifPresent(response -> {

                if (response == ButtonType.OK) {

                    boolean deleted =
                            interviewService.deleteInterview(
                                    selectedInterview.getId()
                            );

                    if (deleted) {
                        refreshInterviews();
                        notesArea.clear();
                    }
                }
            });
        });

        table.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldSelection, newSelection) -> {

                    if (newSelection != null) {
                        notesArea.setText(newSelection.getNotes());
                    } else {
                        notesArea.clear();
                    }
                });

        HBox buttons = new HBox(
                10,
                addButton,
                editButton,
                deleteButton
        );

        VBox root = new VBox(
                10,
                title,
                buttons,
                table,
                new Label("Interview Notes"),
                notesArea
        );

        Scene scene = new Scene(root, 700, 500);

        stage.setTitle("Interview Management");
        stage.setScene(scene);
        stage.show();
    }

    private void refreshInterviews() {
        interviews.setAll(
                interviewService.getInterviewsForApplication(
                        application.getId()
                )
        );
    }
}