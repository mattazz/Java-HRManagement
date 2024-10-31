package com.example.hrmanagement;

import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.stage.Stage;


/**
 * The LoginManager class is responsible for handling the authentication logic for user logins.
 */
public class LoginManager {

    /**
     * Authenticates the login credentials of a user.
     *
     * @param email the email address of the user
     * @param password the password of the user
     * @param noticeMessage the label to display messages to the user
     * @param isAdmin flag to indicate if the user is an admin
     * @return true if the authentication is successful, false otherwise
     */
    static boolean authenticateLogin(String email, String password, Label noticeMessage, boolean isAdmin) {
        String query = isAdmin ? "SELECT * FROM admin WHERE email = ? AND password = ?" : "SELECT * FROM employee WHERE email = ? AND password = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                if (isAdmin) {
                    // Store admin information in AdminSession
                    AdminSession adminSession = AdminSession.getInstance();
                    adminSession.setAdminName(resultSet.getString("name"));
                    adminSession.setAdminEmail(email);
                    System.out.println("Admin " + resultSet.getString("name") + " has logged in.");
                } else {
                    // Store employee information in EmployeeSession
                    EmployeeSession employeeSession = EmployeeSession.getInstance();
                    employeeSession.setEmployeeId(resultSet.getInt("id"));
                    employeeSession.setEmployeeName(resultSet.getString("name"));
                    employeeSession.setEmployeeDepartment(resultSet.getString("department"));
                    employeeSession.setEmployeeEmail(email);
                    employeeSession.setEmployeeSalary(resultSet.getFloat("salary"));
                    System.out.println("Employee " + resultSet.getString("name") + " has logged in.");
                }
                return true;
            } else {
                noticeMessage.setText("Invalid email or password.");
                noticeMessage.setStyle("-fx-text-fill: red");
                return false;
            }
        } catch (SQLException e) {
            noticeMessage.setText("Database Error: " + e.getMessage());
        }
        return false;
    }

//    Logout and return to login dashboard
    static void logout(Label noticeMessage) {
        AdminSession.getInstance().clearSession();
        EmployeeSession.getInstance().clearSession();
        noticeMessage.setText("Logged out successfully.");
        noticeMessage.setStyle("-fx-text-fill: green");

        Stage stage = (Stage) noticeMessage.getScene().getWindow();
        String scene = "/com/example/hrmanagement/login-view.fxml";
        SceneManager.changeScene(stage,scene, noticeMessage);
    }

    static void exit(){
        System.exit(0);
    }
}