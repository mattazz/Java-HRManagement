package com.example.hrmanagement;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Employee {
    private final StringProperty employeeID;
    private final StringProperty name;
    private final StringProperty department;
    private final StringProperty email;
    private final StringProperty password;
    private final StringProperty salary;

    public Employee(String employeeID, String name, String department, String email, String password, String salary) {
        this.employeeID = new SimpleStringProperty(employeeID);
        this.name = new SimpleStringProperty(name);
        this.department = new SimpleStringProperty(department);
        this.email = new SimpleStringProperty(email);
        this.password = new SimpleStringProperty(password);
        this.salary = new SimpleStringProperty(salary);
    }

    public String getEmployeeID() {
        return employeeID.get();
    }

    public StringProperty employeeIDProperty() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID.set(employeeID);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getDepartment() {
        return department.get();
    }

    public StringProperty departmentProperty() {
        return department;
    }

    public void setDepartment(String department) {
        this.department.set(department);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getPassword() {
        return password.get();
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getSalary() {
        return salary.get();
    }

    public StringProperty salaryProperty() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary.set(salary);
    }
}