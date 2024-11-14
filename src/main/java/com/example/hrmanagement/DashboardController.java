package com.example.hrmanagement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardController {


    public Label StatusMessage;
    public TextArea ticketRequest;
    public TextField salaryUpdate;
    public Label employeeDept;
    @FXML
    private Label employeeName;
    @FXML
    private Label dateTime;
    @FXML
    private Label employeeEmail;
    @FXML
    private Label annualSalary;
    @FXML
    private TableView<Ticket> ticketTable;
    @FXML
    public TableColumn<Ticket, String> columnTicketId;
    @FXML
    private TableColumn<Ticket, String> columnDate;
    @FXML
    private TableColumn<Ticket, String> columnRequest;

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

        // Set cell value factories
        columnDate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        columnRequest.setCellValueFactory(cellData -> cellData.getValue().requestProperty());
        columnTicketId.setCellValueFactory(cellData -> cellData.getValue().ticketIDProperty());

        onViewSupportTicket(null);
    }

    public void onViewSupportTicket(ActionEvent actionEvent) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM employee_tickets WHERE employeeID = ?")) {
            preparedStatement.setString(1, String.valueOf(EmployeeSession.getInstance().getEmployeeId()));
            ResultSet resultSet = preparedStatement.executeQuery();

            ticketTable.getItems().clear();

            while (resultSet.next()) {
                Ticket ticket = new Ticket(resultSet.getString("ticketID"), resultSet.getString("date"), resultSet.getString("request"));
                ticketTable.getItems().add(ticket);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onDeleteSupportTicket(ActionEvent actionEvent) {
        Ticket selectedTicket = ticketTable.getSelectionModel().getSelectedItem();
        if (selectedTicket != null) {
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM employee_tickets WHERE ticketID = ?")) {
                preparedStatement.setString(1, selectedTicket.getTicketID());
                preparedStatement.executeUpdate();
                ticketTable.getItems().remove(selectedTicket);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void onCreateSupportTicket(ActionEvent actionEvent) {
//        2024-10-28 pattern

        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String request = ticketRequest.getText();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO employee_tickets (employeeID, date, request ) VALUES (?, ?, ?)")) {
            preparedStatement.setString(1, String.valueOf(EmployeeSession.getInstance().getEmployeeId()));
            preparedStatement.setString(2, date);
            preparedStatement.setString(3, request);
            preparedStatement.executeUpdate();

            onViewSupportTicket(actionEvent);

            StatusMessage.setText("Support ticket created successfully.");
            StatusMessage.setStyle("-fx-text-fill: green");

            // Clear it
            ticketRequest.setText("");

        } catch (SQLException e) {
            e.printStackTrace();
            StatusMessage.setText("Failed to create support ticket.");
            StatusMessage.setStyle("-fx-text-fill: red");
        }
    }

    public void onUpdateSalary(ActionEvent actionEvent) {
        Float newSalary;
        try {
            newSalary = Float.parseFloat(salaryUpdate.getText());
        } catch (NumberFormatException e) {
            StatusMessage.setText("Invalid salary amount. Please enter a valid number.");
            StatusMessage.setStyle("-fx-text-fill: red");
            return;
        }

        if (newSalary < 0) {
            StatusMessage.setText("Invalid salary amount. Salary cannot be negative.");
            StatusMessage.setStyle("-fx-text-fill: red");
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE employee SET salary = ? WHERE id = ?")) {
            preparedStatement.setFloat(1, newSalary);
            preparedStatement.setInt(2, EmployeeSession.getInstance().getEmployeeId());
            preparedStatement.executeUpdate();

            EmployeeSession.getInstance().setEmployeeSalary(newSalary);
            annualSalary.setText(String.format("Annual Salary: $%,.2f", computeAnnualSalary(newSalary)));

            // Clear it
            salaryUpdate.setText("");

            StatusMessage.setText("Salary updated successfully.");
            StatusMessage.setStyle("-fx-text-fill: green");

        } catch (SQLException e) {
            e.printStackTrace();
            StatusMessage.setText("Failed to update salary.");
            StatusMessage.setStyle("-fx-text-fill: red");
        }
    }

    public Float computeAnnualSalary(Float monthlySalary){
        if(monthlySalary < 0){
            return 0.0f;
        }
        return monthlySalary * 12;
    }

    public void onLogOutClick(ActionEvent actionEvent) {
        LoginManager.logout(StatusMessage);
    }

    public void onExitClick(ActionEvent actionEvent) {
        LoginManager.exit();
    }

    public void onViewPreferences(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        SceneManager.changeScene(stage, "/com/example/hrmanagement/dashboard-preferences-view.fxml", StatusMessage);
    }
}