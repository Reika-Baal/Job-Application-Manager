package com.pratik.jobtracker;

import java.util.List;

import com.pratik.jobtracker.database.Database;
import com.pratik.jobtracker.model.JobApplication;
import com.pratik.jobtracker.service.ApplicationService;
import com.pratik.jobtracker.ui.AddApplicationView;
import com.pratik.jobtracker.ui.EditApplicationView;
import com.pratik.jobtracker.model.ApplicationStatus;

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

    @Override
    public void start(Stage stage) {

        Database.initialiseDatabase();

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

        editButton.setDisable(true);
        deleteButton.setDisable(true);

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

        table.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldSelection, newSelection) -> {

                    boolean nothingSelected = newSelection == null;

                    editButton.setDisable(nothingSelected);
                    deleteButton.setDisable(nothingSelected);

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

        HBox buttonBar = new HBox(10, addButton, editButton, deleteButton);

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

        VBox topSection = new VBox(10, title, statisticsBar, buttonBar, filterBar);

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

    public static void main(String[] args) {
        launch(args);
    }
}