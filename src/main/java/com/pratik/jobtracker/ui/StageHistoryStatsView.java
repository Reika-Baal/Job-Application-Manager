package com.pratik.jobtracker.ui;

import com.pratik.jobtracker.model.ApplicationStatus;
import com.pratik.jobtracker.service.ApplicationService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StageHistoryStatsView {

    private final ApplicationService service;

    public StageHistoryStatsView(ApplicationService service) {
        this.service = service;
    }

    public void show() {

        Stage stage = new Stage();

        int totalApplications = service.getTotalApplications();

        long applied = service.countByStatus(ApplicationStatus.APPLIED);

        long onlineTest = service.countByStatus(ApplicationStatus.ONLINE_TEST);

        long interview = service.countByStatus(ApplicationStatus.INTERVIEW);

        long offer = service.countByStatus(ApplicationStatus.OFFER);

        long rejected = service.countByStatus(ApplicationStatus.REJECTED);


        long reachedApplied =
                service.countApplicationsThatReachedStatus(
                        ApplicationStatus.APPLIED
                );

        long reachedOnlineTest =
                service.countApplicationsThatReachedStatus(
                        ApplicationStatus.ONLINE_TEST
                );

        long reachedInterview =
                service.countApplicationsThatReachedStatus(
                        ApplicationStatus.INTERVIEW
                );

        long reachedOffer =
                service.countApplicationsThatReachedStatus(
                        ApplicationStatus.OFFER
                );

        long reachedRejected =
                service.countApplicationsThatReachedStatus(
                        ApplicationStatus.REJECTED
                );


        Label title = new Label("Application Statistics");

        title.getStyleClass().add("title");


        Label totalLabel = new Label("Total Applications: " + totalApplications);

        totalLabel.getStyleClass().add("statistics-total");


        ObservableList<PieChart.Data> pieData =
                FXCollections.observableArrayList();

        if (applied > 0) {
            pieData.add(new PieChart.Data("Applied", applied));
        }

        if (onlineTest > 0) {
            pieData.add(new PieChart.Data("Online Test", onlineTest));
        }

        if (interview > 0) {
            pieData.add(new PieChart.Data("Interview", interview));
        }

        if (offer > 0) {
            pieData.add(new PieChart.Data("Offer", offer));
        }

        if (rejected > 0) {
            pieData.add(new PieChart.Data("Rejected", rejected));
        }


        PieChart pieChart = new PieChart(pieData);

        pieChart.setTitle("Current Application Status");

        pieChart.setLegendVisible(true);

        pieChart.setLabelsVisible(false);

        pieChart.setPrefSize(430, 350);


        Label appliedLabel = new Label("Applied: " + applied);

        Label onlineTestLabel = new Label("Online Test: " + onlineTest);

        Label interviewLabel = new Label("Interview: " + interview);

        Label offerLabel = new Label("Offer: " + offer);

        Label rejectedLabel = new Label("Rejected: " + rejected);


        appliedLabel.getStyleClass().add("stat-label");

        onlineTestLabel.getStyleClass().add("stat-label");

        interviewLabel.getStyleClass().add("stat-label");

        offerLabel.getStyleClass().add("stat-label");

        rejectedLabel.getStyleClass().add("stat-label");


        VBox currentStatsBox = new VBox(
                10,
                appliedLabel,
                onlineTestLabel,
                interviewLabel,
                offerLabel,
                rejectedLabel
        );

        currentStatsBox.setAlignment(Pos.CENTER_LEFT);


        HBox chartSection = new HBox(
                25,
                pieChart,
                currentStatsBox
        );

        chartSection.setAlignment(Pos.CENTER);


        Label reachedTitle =
                new Label("Applications That Reached Each Stage");

        reachedTitle.getStyleClass().add("section-title");


        Label reachedAppliedLabel =
                new Label("Applied: " + reachedApplied);

        Label reachedOnlineTestLabel =
                new Label("Online Test: " + reachedOnlineTest);

        Label reachedInterviewLabel =
                new Label("Interview: " + reachedInterview);

        Label reachedOfferLabel =
                new Label("Offer: " + reachedOffer);

        Label reachedRejectedLabel =
                new Label("Rejected: " + reachedRejected);


        reachedAppliedLabel.getStyleClass().add("stat-label");

        reachedOnlineTestLabel.getStyleClass().add("stat-label");

        reachedInterviewLabel.getStyleClass().add("stat-label");

        reachedOfferLabel.getStyleClass().add("stat-label");

        reachedRejectedLabel.getStyleClass().add("stat-label");


        HBox reachedStats = new HBox(
                10,
                reachedAppliedLabel,
                reachedOnlineTestLabel,
                reachedInterviewLabel,
                reachedOfferLabel,
                reachedRejectedLabel
        );

        reachedStats.setAlignment(Pos.CENTER_LEFT);


        VBox reachedSection = new VBox(
                10,
                reachedTitle,
                reachedStats
        );

        reachedSection.getStyleClass().add("panel");


        VBox root = new VBox(
                15,
                title,
                totalLabel,
                chartSection,
                reachedSection
        );

        root.getStyleClass().add("statistics-page");


        Scene scene = new Scene(root, 750, 600);

        scene.getStylesheets().add(
                getClass().getResource("/styles.css").toExternalForm()
        );


        stage.setTitle("Application Statistics");

        stage.setScene(scene);

        stage.show();
    }
}