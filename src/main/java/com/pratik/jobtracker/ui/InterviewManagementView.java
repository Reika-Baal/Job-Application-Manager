package com.pratik.jobtracker.ui;

import com.pratik.jobtracker.model.Interview;
import com.pratik.jobtracker.model.JobApplication;
import com.pratik.jobtracker.service.InterviewService;

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
    private final Runnable onInterviewsChanged;
    private static Stage openStage;

    private final ObservableList<Interview> interviews =
            FXCollections.observableArrayList();

    public InterviewManagementView(
            JobApplication application,
            Runnable onInterviewsChanged
    ) {
        this.application = application;
        this.interviewService = new InterviewService();
        this.onInterviewsChanged = onInterviewsChanged;
    }

    public void show() {

        if (openStage != null && openStage.isShowing()) {
            openStage.toFront();
            openStage.requestFocus();
            return;
        }

        Stage stage = new Stage();

        openStage = stage;

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
        onInterviewsChanged.run();

        table.setItems(interviews);

        Button addButton = new Button("Add Interview");
        Button editButton = new Button("Edit Interview");
        Button deleteButton = new Button("Delete Interview");

        deleteButton.getStyleClass().add("danger-button");

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

        notesArea.setPrefHeight(70);
        notesArea.setMinHeight(70);
        notesArea.setMaxHeight(70);

        addButton.setOnAction(event -> {

            AddInterviewView addView =
                    new AddInterviewView(
                            application,
                            interviewService,
                            () -> {
                                refreshInterviews();
                                onInterviewsChanged.run();
                            }
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
                            () -> {
                                refreshInterviews();
                                onInterviewsChanged.run();
                            }
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

            confirmation.getDialogPane()
                    .getStylesheets()
                    .add(
                            getClass()
                                    .getResource("/styles.css")
                                    .toExternalForm()
                    );

            confirmation.getDialogPane()
                    .getStyleClass()
                    .add("custom-dialog");

            confirmation.showAndWait().ifPresent(response -> {

                if (response == ButtonType.OK) {

                    boolean deleted =
                            interviewService.deleteInterview(
                                    selectedInterview.getId()
                            );

                    if (deleted) {
                        refreshInterviews();
                        onInterviewsChanged.run();
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

        root.getStyleClass().add("panel");

        Scene scene = new Scene(root, 700, 500);

        scene.getStylesheets().add(
                getClass().getResource("/styles.css").toExternalForm()
        );

        stage.setTitle("Interview Management");
        stage.setScene(scene);
        stage.setOnHidden(event -> openStage = null);
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