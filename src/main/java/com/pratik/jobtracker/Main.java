package com.pratik.jobtracker;

import java.util.List;
import java.time.format.DateTimeFormatter;

import com.pratik.jobtracker.database.Database;
import com.pratik.jobtracker.model.JobApplication;
import com.pratik.jobtracker.model.ApplicationStatus;
import com.pratik.jobtracker.model.Interview;
import com.pratik.jobtracker.model.ApplicationStatusHistory;
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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class Main extends Application {

    private static final int ITEMS_PER_PAGE = 5;

    private final ApplicationService service = new ApplicationService();

    private final InterviewService interviewService = new InterviewService();

    @Override
    public void start(Stage stage) {

        Database.initialiseDatabase();
        showInterviewReminder();

        Label title = new Label("Job Application Manager");

        title.getStyleClass().add("title");

        TableView<JobApplication> table = new TableView<>();

        TableColumn<JobApplication, String> idColumn =
                new TableColumn<>("Entry #");

        idColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        String.valueOf(data.getValue().getId())
                )
        );

        idColumn.setPrefWidth(65);

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
                idColumn,
                companyColumn,
                roleColumn,
                salaryColumn,
                locationColumn,
                dateColumn,
                statusColumn
        );

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        ObservableList<JobApplication> applications =
                FXCollections.observableArrayList(
                        service.getAllApplications()
                );

        table.setItems(applications);

        Pagination applicationPages = new Pagination();
        applicationPages.setMaxPageIndicatorCount(5);

        table.setFixedCellSize(30);

        double tableHeight = 175;

        table.setPrefHeight(tableHeight);
        table.setMinHeight(tableHeight);
        table.setMaxHeight(tableHeight);

        TextField searchField = new TextField();
        searchField.setPromptText("Search entry #, company or location...");
        searchField.setPrefWidth(260);

        searchField.setPrefHeight(36);
        searchField.setMinHeight(36);
        searchField.setMaxHeight(36);


        ComboBox<String> statusFilter = new ComboBox<>();

        statusFilter.getItems().add("All Status");

        for (ApplicationStatus status : ApplicationStatus.values()) {
            statusFilter.getItems().add(status.name());
        }

        statusFilter.setValue("All Status");

        statusFilter.setPrefHeight(36);
        statusFilter.setMinHeight(36);
        statusFilter.setMaxHeight(36);

        statusFilter.setPrefWidth(155);
        statusFilter.setMinWidth(155);
        statusFilter.setMaxWidth(155);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {

            List<JobApplication> filtered =
                    getFilteredApplications(
                            newValue,
                            statusFilter.getValue()
                    );

            applicationPages.setCurrentPageIndex(0);

            showPage(
                    table,
                    filtered,
                    applicationPages,
                    0
            );
        });

        statusFilter.valueProperty().addListener((observable, oldValue, newValue) -> {

            List<JobApplication> filtered =
                    getFilteredApplications(
                            searchField.getText(),
                            newValue
                    );

            applicationPages.setCurrentPageIndex(0);

            showPage(
                    table,
                    filtered,
                    applicationPages,
                    0
            );
        });

        applicationPages.currentPageIndexProperty()
                .addListener((observable, oldPage, newPage) -> {

                    List<JobApplication> filtered =
                            getFilteredApplications(
                                    searchField.getText(),
                                    statusFilter.getValue()
                            );

                    showPage(
                            table,
                            filtered,
                            applicationPages,
                            newPage.intValue()
                    );
                });

        Button addButton = new Button("Add Application");

        addButton.getStyleClass().add("primary-button");

        Button editButton = new Button("Edit Application");

        Button deleteButton = new Button("Delete Application");

        deleteButton.getStyleClass().add("danger-button");

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

        totalLabel.getStyleClass().add("stat-label");
        appliedLabel.getStyleClass().add("stat-label");
        onlineTestLabel.getStyleClass().add("stat-label");
        interviewLabel.getStyleClass().add("stat-label");
        offerLabel.getStyleClass().add("stat-label");
        rejectedLabel.getStyleClass().add("stat-label");

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

        descriptionArea.setPrefRowCount(3);
        descriptionArea.setPrefHeight(90);
        descriptionArea.setMinHeight(90);
        descriptionArea.setMaxHeight(90);

        TextArea notesArea = new TextArea();
        notesArea.setEditable(false);
        notesArea.setWrapText(true);
        notesArea.setPromptText("Application notes");
        notesArea.setPrefRowCount(3);
        notesArea.setPrefHeight(90);
        notesArea.setMinHeight(90);
        notesArea.setMaxHeight(90);

        GridPane statusHistoryGrid = new GridPane();
        statusHistoryGrid.setHgap(15);
        statusHistoryGrid.setVgap(8);
        statusHistoryGrid.setPrefHeight(95);
        statusHistoryGrid.setMinHeight(95);
        statusHistoryGrid.setMaxWidth(Double.MAX_VALUE);

        ColumnConstraints firstColumn = new ColumnConstraints();
        firstColumn.setPercentWidth(50);

        ColumnConstraints secondColumn = new ColumnConstraints();
        secondColumn.setPercentWidth(50);

        statusHistoryGrid.getColumnConstraints().addAll(
                firstColumn,
                secondColumn
        );

        statusHistoryGrid.getStyleClass().add("status-history");

        ListView<String> upcomingInterviewList = new ListView<>();
        upcomingInterviewList.setPrefHeight(70);
        upcomingInterviewList.setMaxHeight(70);

        refreshUpcomingInterviews(upcomingInterviewList);

        addButton.setOnAction(event -> {
            AddApplicationView addView = new AddApplicationView(
                    service,
                    () -> {
                        refreshTable(
                                applications,
                                searchField.getText(),
                                statusFilter.getValue(),
                                table,
                                applicationPages
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
                                statusFilter.getValue(),
                                table,
                                applicationPages
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
                        notesArea.clear();
                        statusHistoryGrid.getChildren().clear();
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
                                        statusFilter.getValue(),
                                        table,
                                        applicationPages
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
                                newSelection.getCompany()
                                        + " - "
                                        + newSelection.getRole()
                        );

                        descriptionArea.setText(
                                newSelection.getJobDescription()
                        );

                        notesArea.setText(
                                newSelection.getNotes()
                        );

                        statusHistoryGrid.getChildren().clear();

                        List<ApplicationStatusHistory> history =
                                service.getStatusHistoryForApplication(
                                        newSelection.getId()
                                );

                        DateTimeFormatter formatter =
                                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

                        for (int i = 0; i < history.size(); i++) {

                            ApplicationStatusHistory entry = history.get(i);

                            Label historyEntry = new Label(
                                    entry.getStatus()
                                            + " - "
                                            + entry.getReachedAt().format(formatter)
                            );


                            int row = i % 3;
                            int column = i / 3;

                            statusHistoryGrid.add(historyEntry, column, row);
                        }

                        if (history.isEmpty()) {
                            statusHistoryGrid.add(
                                    new Label("No status history recorded."),
                                    0,
                                    0
                            );
                        }
                    } else {

                        selectedCompany.setText(
                                "Select an application to view details"
                        );

                        descriptionArea.clear();
                        notesArea.clear();
                        statusHistoryGrid.getChildren().clear();
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

        filterBar.setSpacing(10);
        filterBar.setFillHeight(false);

        VBox.setMargin(
                filterBar,
                new javafx.geometry.Insets(0, 0, 12, 0)
        );

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

        upcomingSection.getStyleClass().add("panel");

        HBox headerBar = new HBox(title);
        headerBar.getStyleClass().add("top-banner");

        VBox topSection = new VBox(10, headerBar, statisticsBar, upcomingSection, buttonBar, filterBar);

        BorderPane root = new BorderPane();

        root.setPadding(
                new javafx.geometry.Insets(16)
        );

        root.setTop(topSection);

        VBox tableSection = new VBox(
                10,
                table,
                applicationPages
        );

        tableSection.setMaxHeight(
                javafx.scene.layout.Region.USE_PREF_SIZE
        );

        tableSection.getStyleClass().add("panel");

        BorderPane.setMargin(
                tableSection,
                new javafx.geometry.Insets(10, 0, 10, 0)
        );

        VBox descriptionSection = new VBox(
                5,
                new Label("Job Description"),
                descriptionArea
        );

        VBox notesSection = new VBox(
                5,
                new Label("Notes"),
                notesArea
        );

        HBox textSection = new HBox(
                10,
                descriptionSection,
                notesSection
        );

        descriptionArea.setMaxWidth(Double.MAX_VALUE);
        notesArea.setMaxWidth(Double.MAX_VALUE);

        descriptionSection.prefWidthProperty()
                .bind(textSection.widthProperty()
                        .subtract(10)
                        .divide(2));

        notesSection.prefWidthProperty()
                .bind(textSection.widthProperty()
                        .subtract(10)
                        .divide(2));

        VBox detailsSection = new VBox(
                10,
                selectedCompany,
                textSection,
                new Label("Status History"),
                statusHistoryGrid
        );

        detailsSection.setMaxHeight(
                javafx.scene.layout.Region.USE_PREF_SIZE
        );

        detailsSection.getStyleClass().add("details-panel");

        VBox mainContent = new VBox(
                10,
                tableSection,
                detailsSection
        );

        root.setCenter(mainContent);

        showPage(
                table,
                getFilteredApplications(
                        searchField.getText(),
                        statusFilter.getValue()
                ),
                applicationPages,
                0
        );

        Scene scene = new Scene(root, 900, 940);

        scene.getStylesheets().add(
                getClass().getResource("/styles.css").toExternalForm()
        );

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

        upcomingInterviewList.setPlaceholder(
                new Label("No upcoming Interviews in the next 7 days.")
        );

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
            String statusText,
            TableView<JobApplication> table,
            Pagination applicationPages
    ) {

        List<JobApplication> filtered =
                getFilteredApplications(
                        searchText,
                        statusText
                );

        showPage(
                table,
                filtered,
                applicationPages,
                applicationPages.getCurrentPageIndex()
        );
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

    private List<JobApplication> getFilteredApplications(
            String searchText,
            String statusText
    ) {

        List<JobApplication> results =
                service.getAllApplications();

        if (searchText != null && !searchText.isBlank()) {

            String query = searchText.toLowerCase();

            results = results.stream()
                    .filter(app ->
                            app.getCompany()
                                    .toLowerCase()
                                    .contains(query)
                                    ||
                                    app.getLocation()
                                            .toLowerCase()
                                            .contains(query)
                                    ||
                                    String.valueOf(app.getId())
                                            .equals(query)
                    )
                    .toList();
        }

        if (statusText != null &&
                !statusText.equals("All Status")) {

            ApplicationStatus status =
                    ApplicationStatus.valueOf(statusText);

            results = results.stream()
                    .filter(app ->
                            app.getStatus() == status
                    )
                    .toList();
        }

        return results;
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

    private void showPage(
            TableView<JobApplication> table,
            List<JobApplication> applications,
            Pagination applicationPages,
            int pageIndex
    ) {

        int pageCount = Math.max(
                1,
                (int) Math.ceil(
                        (double) applications.size() / ITEMS_PER_PAGE
                )
        );

        applicationPages.setPageCount(pageCount);

        if (pageIndex >= pageCount) {
            pageIndex = pageCount - 1;
        }

        if (applications.isEmpty()) {
            table.setItems(
                    FXCollections.observableArrayList()
            );
            return;
        }

        int fromIndex =
                pageIndex * ITEMS_PER_PAGE;

        int toIndex =
                Math.min(
                        fromIndex + ITEMS_PER_PAGE,
                        applications.size()
                );

        table.setItems(
                FXCollections.observableArrayList(
                        applications.subList(
                                fromIndex,
                                toIndex
                        )
                )
        );
    }

    public static void main(String[] args) {
        launch(args);
    }
}