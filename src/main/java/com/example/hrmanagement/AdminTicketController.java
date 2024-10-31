package com.example.hrmanagement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AdminTicketController {


    public Label statusMessage;


    public TableView<Ticket> ticketTable;
    public TableColumn<Ticket, String> columnTicketId;
    public TableColumn<Ticket, String> columnTicketDate;
    public TableColumn<Ticket, String> columnTicketRequest;
    public TextField employeeId;
    public TableView<Employee> employeeTable;
    public TableColumn<Employee, String> columnEmployeeId;
    public TableColumn<Employee, String> columnEmployeeName;
    public TableColumn<Employee, String> columnEmployeeEmail;
    public TableColumn<Employee, String> columnEmployeeDept;
    public TableColumn<Employee, String> columnEmployeeSalary;
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

        // Set cell value factories
        columnTicketId.setCellValueFactory(cellData -> cellData.getValue().ticketIDProperty());
        columnTicketDate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        columnTicketRequest.setCellValueFactory(cellData -> cellData.getValue().requestProperty());

//        For employees
        columnEmployeeId.setCellValueFactory(cellData -> cellData.getValue().employeeIDProperty());
        columnEmployeeName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        columnEmployeeEmail.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        columnEmployeeDept.setCellValueFactory(cellData -> cellData.getValue().departmentProperty());
        columnEmployeeSalary.setCellValueFactory(cellData -> cellData.getValue().salaryProperty());


    }
    
    public void onLogOutClick(ActionEvent actionEvent) {
        LoginManager.logout(statusMessage);
    }

    public void onExitClick(ActionEvent actionEvent) {
        LoginManager.exit();
    }


    public void onBackClick(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        SceneManager.changeScene(stage, "/com/example/hrmanagement/admin-dashboard-view.fxml", statusMessage);
    }


    public void onDeleteTicket(ActionEvent actionEvent) {
        Ticket ticket = ticketTable.getSelectionModel().getSelectedItem();
        if (ticket == null) {
            statusMessage.setText("Please select a ticket to delete");
            statusMessage.setStyle("-fx-text-fill: red");
            return;
        }
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM employee_tickets WHERE ticketID = ?")) {
            preparedStatement.setString(1, ticket.getTicketID());
            preparedStatement.executeUpdate();
            statusMessage.setText("Ticket deleted successfully");
            statusMessage.setStyle("-fx-text-fill: green");
            ticketTable.getItems().remove(ticket);
        } catch (Exception e) {
            statusMessage.setText("Error deleting ticket");
            statusMessage.setStyle("-fx-text-fill: red");
            e.printStackTrace();
        }
    }

    public void viewTickets(ActionEvent actionEvent) {
//        View ticket of specific employee based on employeeID
        String id = employeeId.getText();
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM employee_tickets WHERE employeeID = ?")) {
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            ticketTable.getItems().clear();
            while (resultSet.next()) {
                Ticket ticket = new Ticket(resultSet.getString("ticketID"), resultSet.getString("date"), resultSet.getString("request"));
                ticketTable.getItems().add(ticket);
            }

            if (ticketTable.getItems().isEmpty()) {
                statusMessage.setText("No tickets found for employee with ID: " + id);
                statusMessage.setStyle("-fx-text-fill: red");
            } else {
                statusMessage.setText("Tickets found for employee: " + id);
                statusMessage.setStyle("-fx-text-fill: green");
            }

        } catch (Exception e) {
            statusMessage.setText("Error loading ticket table");
            statusMessage.setStyle("-fx-text-fill: red");
            e.printStackTrace();
        }

//        Also view the employee based on the id
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM employee WHERE id = ?")) {
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            employeeTable.getItems().clear();
            while (resultSet.next()) {
                Employee employee = new Employee(resultSet.getString("id"), resultSet.getString("name"), resultSet.getString("department"), resultSet.getString("email"), resultSet.getString("password"), resultSet.getString("salary"));
                employeeTable.getItems().add(employee);
            }

            if (employeeTable.getItems().isEmpty()) {
                statusMessage.setText("No employee found with ID: " + id);
                statusMessage.setStyle("-fx-text-fill: red");
            } else {
                statusMessage.setText("Employee found with ID: " + id);
                statusMessage.setStyle("-fx-text-fill: green");
            }

        } catch (Exception e) {
            statusMessage.setText("Error loading employee table");
            statusMessage.setStyle("-fx-text-fill: red");
            e.printStackTrace();
        }

    }
}