package com.example.hrmanagement;

/**
 * Singleton class to store the current employee's session information.
 */
public class EmployeeSession {
    private static EmployeeSession instance;
    private Integer employeeId;
    private String employeeName;
    private String employeeDepartment;
    private String employeeEmail;
    private float employeeSalary;

    private EmployeeSession() {
        // Private constructor to prevent instantiation
    }

    public static EmployeeSession getInstance() {
        if (instance == null) {
            instance = new EmployeeSession();
        }
        return instance;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeDepartment() {
        return employeeDepartment;
    }

    public void setEmployeeDepartment(String employeeDepartment) {
        this.employeeDepartment = employeeDepartment;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public double getEmployeeSalary() {
        return employeeSalary;
    }

    public void setEmployeeSalary(float employeeSalary) {
        this.employeeSalary = employeeSalary;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public void clearSession() {
        employeeId = null;
        employeeName = null;
        employeeDepartment = null;
        employeeEmail = null;
        employeeSalary = 0;
    }
}