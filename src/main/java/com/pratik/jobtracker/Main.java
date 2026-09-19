package com.pratik.jobtracker;

import java.util.List;
import java.time.format.DateTimeFormatter;

import com.pratik.jobtracker.database.Database;
import com.pratik.jobtracker.model.JobApplication;
import com.pratik.jobtracker.model.ApplicationStatus;
import com.pratik.jobtracker.model.Interview;
import com.pratik.jobtracker.service.ApplicationService;
import com.pratik.jobtracker.service.InterviewService;
import com.pratik.jobtracker.ui.AddApplicationView;
import com.pratik.jobtracker.ui.EditApplicationView;
import com.pratik.jobtracker.ui.StageHistoryStatsView;
import com.pratik.jobtracker.ui.InterviewManagementView;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private final ApplicationService service = new ApplicationService();

    private final InterviewService interviewService = new InterviewService();

    @Override
    public void start(Stage stage) {

        Database.initialiseDatabase();
        showInterviewReminder();

        Label title = new Label("Job Application Manager");

        TableView<JobApplication> table = new TableView<>();

        TableColumn<JobApplication, String> companyColumn =
                new TableColumn<>("Company");

        companyColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getCompany()
                )
        );

        TableColumn<JobApplication, String> roleColumn =
                new TableColumn<>("Role");

        roleColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getRole()
                )
        );

        TableColumn<JobApplication, String> salaryColumn =
                new TableColumn<>("Salary");

        salaryColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        String.valueOf(data.getValue().getSalary())
                )
        );

        TableColumn<JobApplication, String> locationColumn =
                new TableColumn<>("Location");

        locationColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getLocation()
                )
        );

        TableColumn<JobApplication, String> dateColumn =
                new TableColumn<>("Date Applied");

        dateColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getApplicationDate().toString()
                )
        );

        TableColumn<JobApplication, String> statusColumn =
                new TableColumn<>("Status");

        statusColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getStatus().toString()
                )
        );

        table.getColumns().addAll(
                companyColumn,
                roleColumn,
                salaryColumn,
                locationColumn,
                dateColumn,
                statusColumn
        );

        ObservableList<JobApplication> applications =
                FXCollections.observableArrayList(
                        service.getAllApplications()
                );

        table.setItems(applications);

        TextField searchField = new TextField();
        searchField.setPromptText("Search company or location...");

        ComboBox<String> statusFilter = new ComboBox<>();

        statusFilter.getItems().add("All Statuses");

        for (ApplicationStatus status : ApplicationStatus.values()) {
            statusFilter.getItems().add(status.name());
        }

        statusFilter.setValue("All Statuses");

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            applyFilters(
                    applications,
                    newValue,
                    statusFilter.getValue()
            );
        });

        statusFilter.valueProperty().addListener((observable, oldValue, newValue) -> {
            applyFilters(
                    applications,
                    searchField.getText(),
                    newValue
            );
        });

        Button addButton = new Button("Add Application");

        Button editButton = new Button("Edit Application");

        Button deleteButton = new Button("Delete Application");

        Button interviewButton = new Button("Manage Interviews");

        Button historyStatsButton = new Button("Stage History Stats");

        editButton.setDisable(true);
        deleteButton.setDisable(true);
        interviewButton.setDisable(true);

        Label selectedCompany =
                new Label("Select an application to view details");

        Label totalLabel = new Label();
        Label appliedLabel = new Label();
        Label onlineTestLabel = new Label();
        Label interviewLabel = new Label();
        Label offerLabel = new Label();
        Label rejectedLabel = new Label();

        refreshStatistics(
                totalLabel,
                appliedLabel,
                onlineTestLabel,
                interviewLabel,
                offerLabel,
                rejectedLabel
        );

        TextArea descriptionArea = new TextArea();
        descriptionArea.setEditable(false);
        descriptionArea.setWrapText(true);
        descriptionArea.setPromptText("Job description");

        descriptionArea.setPrefRowCount(5);

        ListView<String> upcomingInterviewList = new ListView<>();
        upcomingInterviewList.setPrefHeight(120);

        refreshUpcomingInterviews(upcomingInterviewList);

        addButton.setOnAction(event -> {
            AddApplicationView addView = new AddApplicationView(
                    service,
                    () -> {
                        refreshTable(
                                applications,
                                searchField.getText(),
                                statusFilter.getValue()
                        );

                        refreshStatistics(
                                totalLabel,
                                appliedLabel,
                                onlineTestLabel,
                                interviewLabel,
                                offerLabel,
                                rejectedLabel
                        );
                    }
            );

            addView.show();
        });

        deleteButton.setOnAction(event -> {

            JobApplication selectedApplication =
                    table.getSelectionModel().getSelectedItem();

            if (selectedApplication == null) {
                return;
            }

            Alert confirmation = new Alert(
                    Alert.AlertType.CONFIRMATION
            );

            confirmation.setTitle("Delete Application");

            confirmation.setHeaderText(
                    "Delete " + selectedApplication.getCompany() + "?"
            );

            confirmation.setContentText(
                    "This action cannot be undone."
            );

            confirmation.showAndWait().ifPresent(response -> {

                if (response == ButtonType.OK) {

                    boolean deleted =
                            service.deleteApplication(
                                    selectedApplication.getId()
                            );

                    if (deleted) {
                        refreshTable(
                                applications,
                                searchField.getText(),
                                statusFilter.getValue()
                        );

                        refreshStatistics(
                                totalLabel,
                                appliedLabel,
                                onlineTestLabel,
                                interviewLabel,
                                offerLabel,
                                rejectedLabel
                        );

                        selectedCompany.setText(
                                "Select an application to view details"
                        );

                        descriptionArea.clear();
                    }
                }
            });
        });

        editButton.setOnAction(event -> {

            JobApplication selectedApplication =
                    table.getSelectionModel().getSelectedItem();

            if (selectedApplication == null) {
                return;
            }

            EditApplicationView editView =
                    new EditApplicationView(
                            service,
                            selectedApplication,
                            () -> {
                                refreshTable(
                                        applications,
                                        searchField.getText(),
                                        statusFilter.getValue()
                                );

                                refreshStatistics(
                                        totalLabel,
                                        appliedLabel,
                                        onlineTestLabel,
                                        interviewLabel,
                                        offerLabel,
                                        rejectedLabel
                                );
                            }
                    );

            editView.show();
        });

        interviewButton.setOnAction(event -> {

            JobApplication selectedApplication =
                    table.getSelectionModel().getSelectedItem();

            if (selectedApplication == null) {
                return;
            }

            InterviewManagementView interviewView =
                    new InterviewManagementView(
                            selectedApplication,
                            () -> refreshUpcomingInterviews(
                                    upcomingInterviewList
                            )
                    );

            interviewView.show();
        });

        historyStatsButton.setOnAction(event -> {

            StageHistoryStatsView statsView =
                    new StageHistoryStatsView(service);

            statsView.show();
        });

        table.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldSelection, newSelection) -> {

                    boolean nothingSelected = newSelection == null;

                    editButton.setDisable(nothingSelected);
                    deleteButton.setDisable(nothingSelected);
                    interviewButton.setDisable(nothingSelected);

                    if (newSelection != null) {
                        selectedCompany.setText(
                                newSelection.getCompany() +
                                        " - " +
                                        newSelection.getRole()
                        );

                        descriptionArea.setText(
                                newSelection.getJobDescription()
                        );

                    } else {
                        selectedCompany.setText(
                                "Select an application to view details"
                        );

                        descriptionArea.clear();
                    }
                });

        HBox buttonBar = new HBox(
                10,
                addButton,
                editButton,
                deleteButton,
                interviewButton,
                historyStatsButton
        );

        HBox filterBar = new HBox(10, searchField, statusFilter);

        HBox statisticsBar = new HBox(
                15,
                totalLabel,
                appliedLabel,
                onlineTestLabel,
                interviewLabel,
                offerLabel,
                rejectedLabel
        );

        VBox upcomingSection = new VBox(
                5,
                new Label("Upcoming Interviews"),
                upcomingInterviewList
        );

        VBox topSection = new VBox(10, title, statisticsBar, upcomingSection, buttonBar, filterBar);

        BorderPane root = new BorderPane();

        root.setTop(topSection);

        VBox centreSection = new VBox(
                10,
                table,
                selectedCompany,
                new Label("Job Description"),
                descriptionArea
        );

        root.setCenter(centreSection);

        Scene scene = new Scene(root, 900, 600);

        stage.setTitle("Job Application Manager");
        stage.setScene(scene);
        stage.show();
    }

    private void refreshUpcomingInterviews(ListView<String> upcomingInterviewList) {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        List<Interview> upcoming =
                interviewService.getUpcomingInterviews(7);

        upcomingInterviewList.getItems().clear();

        if (upcoming.isEmpty()) {
            upcomingInterviewList.getItems().add(
                    "No upcoming interviews in the next 7 days."
            );
            return;
        }

        for (Interview interview : upcoming) {

            JobApplication application =
                    service.getApplicationById(
                            interview.getApplicationId()
                    );

            String company = application != null
                    ? application.getCompany()
                    : "Unknown Company";

            upcomingInterviewList.getItems().add(
                    company
                            + " - "
                            + interview.getInterviewDateTime().format(formatter)
                            + " - "
                            + interview.getType()
            );
        }
    }

    private void refreshTable(
            ObservableList<JobApplication> applications,
            String searchText,
            String statusText
    ) {
        applyFilters(applications, searchText, statusText);
    }

    private void refreshStatistics(
            Label totalLabel,
            Label appliedLabel,
            Label onlineTestLabel,
            Label interviewLabel,
            Label offerLabel,
            Label rejectedLabel
    ) {

        totalLabel.setText("Total Applications: " + service.getTotalApplications());

        appliedLabel.setText("Applied: " + service.countByStatus(ApplicationStatus.APPLIED));

        onlineTestLabel.setText("Online Test: " + service.countByStatus(ApplicationStatus.ONLINE_TEST));

        interviewLabel.setText("Interview: " + service.countByStatus(ApplicationStatus.INTERVIEW));

        offerLabel.setText("Offer: " + service.countByStatus(ApplicationStatus.OFFER));

        rejectedLabel.setText("Rejected: " + service.countByStatus(ApplicationStatus.REJECTED));
    }

    private void applyFilters(
            ObservableList<JobApplication> applications,
            String searchText,
            String statusText
    ) {
        List<JobApplication> results = service.getAllApplications();

        if (searchText != null && !searchText.isBlank()) {
            String query = searchText.toLowerCase();

            results = results.stream()
                    .filter(app ->
                            app.getCompany().toLowerCase().contains(query)
                                    || app.getLocation()
                                    .toLowerCase()
                                    .contains(query)
                    )
                    .toList();
        }

        if (statusText != null && !statusText.equals("All Statuses")) {

            ApplicationStatus status = ApplicationStatus.valueOf(statusText);

            results = results.stream()
                    .filter(app ->
                            app.getStatus() == status
                    )
                    .toList();
        }

        applications.setAll(results);
    }

    private void showInterviewReminder() {

        List<Interview> upcoming =
                interviewService.getUpcomingInterviews(1);

        if (upcoming.isEmpty()) {
            return;
        }

        StringBuilder message = new StringBuilder();

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Interview interview : upcoming) {

            JobApplication application =
                    service.getApplicationById(
                            interview.getApplicationId()
                    );

            String company = application !=null
                    ? application.getCompany()
                    : "Unknown Company";

            message.append(company)
                    .append(" - ")
                    .append(interview.getInterviewDateTime().format(formatter))
                    .append(" - ")
                    .append(interview.getType())
                    .append("\n");
        }

        Alert reminder = new Alert(Alert.AlertType.INFORMATION);

        reminder.setTitle("Upcoming Interview Reminder");
        reminder.setHeaderText("You have an interview within the next 24 hours");
        reminder.setContentText(message.toString());

        reminder.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}