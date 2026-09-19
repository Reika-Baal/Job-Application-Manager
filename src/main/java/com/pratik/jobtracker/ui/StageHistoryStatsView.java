package com.pratik.jobtracker.ui;

import com.pratik.jobtracker.model.ApplicationStatus;
import com.pratik.jobtracker.service.ApplicationService;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StageHistoryStatsView {

    private final ApplicationService service;

    public StageHistoryStatsView(ApplicationService service) {
        this.service = service;
    }

    public void show() {

        Stage stage = new Stage();

        Label title = new Label("Stage History Statistics");

        Label appliedLabel = new Label(
                "Total Applied: " +
                        service.countApplicationsThatReachedStatus(
                                ApplicationStatus.APPLIED
                        )
        );

        Label onlineTestLabel = new Label(
                "Ever reached Online Test: " +
                        service.countApplicationsThatReachedStatus(
                                ApplicationStatus.ONLINE_TEST
                        )
        );

        Label interviewLabel = new Label(
                "Ever reached Interview: " +
                        service.countApplicationsThatReachedStatus(
                                ApplicationStatus.INTERVIEW
                        )
        );

        Label offerLabel = new Label(
                "Ever reached Offer: " +
                        service.countApplicationsThatReachedStatus(
                                ApplicationStatus.OFFER
                        )
        );

        Label rejectedLabel = new Label(
                "Total Rejected: " +
                        service.countApplicationsThatReachedStatus(
                                ApplicationStatus.REJECTED
                        )
        );

        VBox root = new VBox(
                10,
                title,
                appliedLabel,
                onlineTestLabel,
                interviewLabel,
                offerLabel,
                rejectedLabel
        );

        Scene scene = new Scene(root, 350, 300);

        stage.setTitle("Stage History Statistics");
        stage.setScene(scene);
        stage.show();
    }
}