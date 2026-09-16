package com.pratik.jobtracker;

import com.pratik.jobtracker.database.Database;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        Database.initialiseDatabase();

        Label title = new Label("Job Application Manager");

        VBox root = new VBox(title);

        Scene scene = new Scene(root, 900, 600);

        stage.setTitle("Job Application Manager");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}