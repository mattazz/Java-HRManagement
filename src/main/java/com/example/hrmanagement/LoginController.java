package com.example.hrmanagement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LoginController {
    public Label noticeMessage;
    public Button loginBtn;
    public Button adminLoginBtn;
    public TextField userEmail;
    public PasswordField userPassword;

    public void onLoginButtonClick(ActionEvent actionEvent) {
        String email = userEmail.getText();
        String password = userPassword.getText();

        if (email.isEmpty() || password.isEmpty()) {
            noticeMessage.setStyle("-fx-text-fill: red");
            noticeMessage.setText("Please fill in all the fields for login.");
        } else {
            boolean isAuthenticated = LoginManager.authenticateLogin(email, password, noticeMessage, false);

            if (isAuthenticated) {
                String fxmlURL = "/com/example/hrmanagement/dashboard-view.fxml";
                Stage stage = (Stage) loginBtn.getScene().getWindow();
                SceneManager.changeScene(stage, fxmlURL, noticeMessage);
            }
        }
    }

    public void onAdminLoginButtonClick(ActionEvent actionEvent) {
        noticeMessage.setText("Test login button");
        String fxmlURL = "/com/example/hrmanagement/admin-login-view.fxml";
        Stage stage = (Stage) adminLoginBtn.getScene().getWindow();
        SceneManager.changeScene(stage, fxmlURL, noticeMessage);
    }
}

