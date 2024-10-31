package com.example.hrmanagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HRApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HRApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 800);

        // Load and apply the external CSS file
        String css = Objects.requireNonNull(HRApplication.class.getResource("/com/example/hrmanagement/styles.css")).toExternalForm();
        scene.getStylesheets().add(css);

        stage.setTitle("HR Ticket Management 1.0");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}