package com.example.hrmanagement;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * The SceneManager class is responsible for changing scenes in the application.
 */
public class SceneManager {
    /**
     * Makes it easy to change the scene for me
     *
     * @param stage the stage on which to set the new scene
     * @param fxmlFile the path to the FXML file to load
     * @param errorMessageLabel the label to display an error message if the scene fails to load
     */
    public static void changeScene(Stage stage, String fxmlFile, Label errorMessageLabel){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(SceneManager.class.getResource(fxmlFile));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 1000, 800);
            // Load and apply the external CSS file
            String css = Objects.requireNonNull(SceneManager.class.getResource("/com/example/hrmanagement/styles.css")).toExternalForm();
            scene.getStylesheets().add(css);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e){
            if (errorMessageLabel != null){
                errorMessageLabel.setStyle("-fx-text-fill: red");
                errorMessageLabel.setText("Failed to load the scene");
            }
            e.printStackTrace();
        }
    }
}