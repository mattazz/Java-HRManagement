package com.example.hrmanagement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DashboardPreferencesController {

    public Label statusMessage;
    public Label employeeName;
    public Label employeeEmail;
    public Label employeeDept;
    public Label annualSalary;
    public Label dateTime;
    public TextField emailUpdate;
    public TextField passwordUpdate;
    public void initialize(){
        EmployeeSession employeeSession = EmployeeSession.getInstance();
        employeeName.setText("Name: " + employeeSession.getEmployeeName());
        employeeEmail.setText("Email: " + employeeSession.getEmployeeEmail());
        employeeDept.setText("Department: " + employeeSession.getEmployeeDepartment());
        annualSalary.setText(String.format("Annual Salary: $%,.2f", computeAnnualSalary((float) employeeSession.getEmployeeSalary())));

        // Get current date and time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy: hh:mma");
        String formattedDateTime = now.format(formatter);
        dateTime.setText("Log on Date and Time: " + formattedDateTime);
    }

    public Float computeAnnualSalary(Float monthlySalary){
        if(monthlySalary < 0){
            return 0.0f;
        }
        return monthlySalary * 12;
    }

    public void onLogOutClick(ActionEvent actionEvent) {
        LoginManager.logout(statusMessage);
    }

    public void onExitClick(ActionEvent actionEvent) {
        LoginManager.exit();
    }

    public void onUpdateEmail(ActionEvent actionEvent) {
        String newEmail = emailUpdate.getText();
        if(newEmail.isEmpty()){
            statusMessage.setText("Please enter a new email address.");
            statusMessage.setStyle("-fx-text-fill: red");
        } else {
            EmployeeSession employeeSession = EmployeeSession.getInstance();
            String query = "UPDATE employee SET email = ? WHERE id = ?";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, newEmail);
                preparedStatement.setInt(2, employeeSession.getEmployeeId());
                int result = preparedStatement.executeUpdate();
                if (result > 0) {
                    employeeSession.setEmployeeEmail(newEmail);
                    statusMessage.setText("Email updated successfully.");
                    statusMessage.setStyle("-fx-text-fill: green");

                    emailUpdate.clear();
                } else {
                    statusMessage.setText("Email update failed.");
                    statusMessage.setStyle("-fx-text-fill: red");
                }
            } catch (SQLException e) {
                statusMessage.setText("Database Error: " + e.getMessage());
            }
        }
    }

    public void onUpdatePassword(ActionEvent actionEvent) {
        String newPassword = passwordUpdate.getText();
        if(newPassword.isEmpty()){
            statusMessage.setText("Please enter a new password.");
            statusMessage.setStyle("-fx-text-fill: red");
        } else {
            EmployeeSession employeeSession = EmployeeSession.getInstance();
            String query = "UPDATE employee SET password = ? WHERE id = ?";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, newPassword);
                preparedStatement.setInt(2, employeeSession.getEmployeeId());
                int result = preparedStatement.executeUpdate();
                if (result > 0) {
                    statusMessage.setText("Password updated successfully.");
                    statusMessage.setStyle("-fx-text-fill: green");

                    passwordUpdate.clear();
                } else {
                    statusMessage.setText("Password update failed.");
                    statusMessage.setStyle("-fx-text-fill: red");
                }
            } catch (SQLException e) {
                statusMessage.setText("Database Error: " + e.getMessage());
            }
        }
    }

    public void onBackClick(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        SceneManager.changeScene(stage, "/com/example/hrmanagement/dashboard-view.fxml", statusMessage);

    }
}

