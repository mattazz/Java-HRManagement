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

public class AdminEmployeeController {

    public Label statusMessage;
    public TableView<Employee> employeeTable;
    public TableColumn<Employee, String> columnEmployeeId;
    public TableColumn<Employee, String> columnEmployeeName;
    public TableColumn<Employee, String> columnEmployeeEmail;
    public TableColumn<Employee, String> columnEmployeeSalary;
    public TableColumn<Employee, String> columnEmployeeDept;
    public TextField employeeID;
    public TextField employeeName;
    public TextField employeeSalary;
    public TextField employeeEmail;
    public TextField employeeDepartment;
    public TextField employeePassword;
    public TableView<Ticket> ticketTable;
    public TableColumn<Ticket, String> columnTicketId;
    public TableColumn<Ticket, String> columnTicketDate;
    public TableColumn<Ticket, String> columnTicketRequest;
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
        columnEmployeeId.setCellValueFactory(cellData -> cellData.getValue().employeeIDProperty());
        columnEmployeeName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        columnEmployeeEmail.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        columnEmployeeSalary.setCellValueFactory(cellData -> cellData.getValue().salaryProperty());
        columnEmployeeDept.setCellValueFactory(cellData -> cellData.getValue().departmentProperty());

//        set ticket table
        columnTicketId.setCellValueFactory(cellData -> cellData.getValue().ticketIDProperty());
        columnTicketDate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        columnTicketRequest.setCellValueFactory(cellData -> cellData.getValue().requestProperty());


        viewEmployeeTable();

    }
    
    public void onLogOutClick(ActionEvent actionEvent) {
        LoginManager.logout(statusMessage);
    }

    public void onExitClick(ActionEvent actionEvent) {
        LoginManager.exit();
    }

    public void viewEmployeeTable() {
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM employee")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            employeeTable.getItems().clear();
            while (resultSet.next()) {
                Employee employee = new Employee( resultSet.getString("id"), resultSet.getString("name"), resultSet.getString("department"), resultSet.getString("email"), resultSet.getString("password"), resultSet.getString("salary"));
                employeeTable.getItems().add(employee);
            }
        } catch (Exception e) {
            statusMessage.setText("Error loading employee table");
            statusMessage.setStyle("-fx-text-fill: red");
            e.printStackTrace();
        }
    }

    public void addUserPanel(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        SceneManager.changeScene(stage, "/com/example/hrmanagement/admin-add-user.fxml", statusMessage);
    }

    public void addEmployee(ActionEvent actionEvent) {
        String name = employeeName.getText();
        String email = employeeEmail.getText();
        String salary = employeeSalary.getText();
        String department = employeeDepartment.getText();


        if (employeeName.getText().isEmpty() || employeeEmail.getText().isEmpty() || employeeSalary.getText().isEmpty()) {
            statusMessage.setText("Please fill in all fields");
            statusMessage.setStyle("-fx-text-fill: red");
            return;
        }

        //        check if already duplicate email
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM employee WHERE email = ?")) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                statusMessage.setText("User already exists");
                statusMessage.setStyle("-fx-text-fill: red");

                clearEmployeeFields();
                return;
            }
        } catch (Exception e) {
            statusMessage.setText("Error checking email");
            statusMessage.setStyle("-fx-text-fill: red");
            e.printStackTrace();
        }


        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO employee (name, department, email, salary, password ) VALUES (?, ?, ?, ?, ?)")) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, department);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, salary);
            preparedStatement.setString(5, "password");

            preparedStatement.executeUpdate();

            statusMessage.setText("Employee added successfully");
            statusMessage.setStyle("-fx-text-fill: green");

            clearEmployeeFields();

            viewEmployeeTable();


        } catch (Exception e) {
            statusMessage.setText("Error adding employee: " + e.getMessage());
        }
    }

    public void onBackClick(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        SceneManager.changeScene(stage, "/com/example/hrmanagement/admin-dashboard-view.fxml", statusMessage);
    }

    public void onDeleteEmployee(ActionEvent actionEvent) {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM employee WHERE id = ?")) {
                preparedStatement.setString(1, selectedEmployee.getEmployeeID());
                preparedStatement.executeUpdate();
                employeeTable.getItems().remove(selectedEmployee);

                statusMessage.setText("Employee deleted successfully");
                statusMessage.setStyle("-fx-text-fill: green");
            } catch (Exception e) {
                statusMessage.setText("Error deleting employee");
                statusMessage.setStyle("-fx-text-fill: red");
                e.printStackTrace();
            }
        }
    }

    public void onViewEmployee(ActionEvent actionEvent) {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
//      validate employeeID not null
        if (selectedEmployee == null) {
            statusMessage.setText("Please select an employee to view");
            statusMessage.setStyle("-fx-text-fill: red");
            return;
        }
//      Then populate the TextFields based on the employeeID
        employeeID.setText(selectedEmployee.getEmployeeID());
        employeeName.setText(selectedEmployee.getName());
        employeeEmail.setText(selectedEmployee.getEmail());
        employeeSalary.setText(selectedEmployee.getSalary());
        employeeDepartment.setText(selectedEmployee.getDepartment());
        employeePassword.setText(selectedEmployee.getPassword());
//    Then populate the ticket table based on the employeeID
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM employee_tickets WHERE employeeID = ?")) {
            preparedStatement.setString(1, selectedEmployee.getEmployeeID());
            ResultSet resultSet = preparedStatement.executeQuery();
            ticketTable.getItems().clear();
            while (resultSet.next()) {
                Ticket ticket = new Ticket(resultSet.getString("ticketID"), resultSet.getString("date"), resultSet.getString("request"));
                ticketTable.getItems().add(ticket);
            }
        } catch (Exception e) {
            statusMessage.setText("Error loading ticket table");
            statusMessage.setStyle("-fx-text-fill: red");
            e.printStackTrace();
        }
    }


    public void editEmployee(ActionEvent actionEvent) {
        String id = employeeID.getText();
        String name = employeeName.getText();
        String email = employeeEmail.getText();
        String salary = employeeSalary.getText();
        String department = employeeDepartment.getText();
        String password = employeePassword.getText();


        if (id.isEmpty() || name.isEmpty() || email.isEmpty() || salary.isEmpty() || department.isEmpty()) {
            statusMessage.setText("Please select an employee to edit");
            statusMessage.setStyle("-fx-text-fill: red");
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE employee SET name = ?, department = ?, email = ?, salary = ?, password = ? WHERE id = ?")) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, department);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, salary);
            preparedStatement.setString(5, password);
            preparedStatement.setString(6, id);
            preparedStatement.executeUpdate();

            statusMessage.setText("Employee updated successfully");
            statusMessage.setStyle("-fx-text-fill: green");

            viewEmployeeTable();

        } catch (Exception e) {
            statusMessage.setText("Error updating employee");
            statusMessage.setStyle("-fx-text-fill: red");
            e.printStackTrace();
        }
    }

    public void clearEmployeeFields(){
        employeeID.clear();
        employeeName.clear();
        employeeEmail.clear();
        employeeSalary.clear();
        employeeDepartment.clear();
        employeePassword.clear();
    }
}