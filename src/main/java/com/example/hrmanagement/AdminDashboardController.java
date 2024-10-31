package com.example.hrmanagement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AdminDashboardController {


    public Label statusMessage;
    @FXML
    private Label adminName;
    @FXML
    private Label dateTime;
    @FXML
    private Label adminEmail;

    public void initialize(){
        AdminSession adminSession = AdminSession.getInstance();
        adminName.setText("Name: " + adminSession.getAdminName());
        adminEmail.setText("Email: " + adminSession.getAdminEmail());
        // Get current date and time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy: hh:mma");
        String formattedDateTime = now.format(formatter);
        dateTime.setText(formattedDateTime);
    }
    
    public void onLogOutClick(ActionEvent actionEvent) {
        LoginManager.logout(statusMessage);
    }

    public void onExitClick(ActionEvent actionEvent) {
        LoginManager.exit();
    }

    public void onViewEmployee(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        SceneManager.changeScene(stage, "/com/example/hrmanagement/admin-employee-view.fxml", statusMessage);
    }



    public void onViewTicket(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        SceneManager.changeScene(stage, "/com/example/hrmanagement/admin-ticket-view.fxml", statusMessage);
    }
}