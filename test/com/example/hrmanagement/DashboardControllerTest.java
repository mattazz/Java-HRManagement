package com.example.hrmanagement;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DashboardControllerTest {

    @Test
    void computeAnnualSalary() {
        DashboardController dashboardController = new DashboardController();
        float salary = 1000;
        float annualSalary = dashboardController.computeAnnualSalary(salary);
        assertEquals(12000, annualSalary);

        salary = -1000;
        annualSalary = dashboardController.computeAnnualSalary(salary);
        assertEquals(0, annualSalary);

    }

    @Test
    void computeAnnualSalaryForVariousSalaries() {
        DashboardController dashboardController = new DashboardController();

        //Valid monthly salary
        float salary = 1000;
        float annualSalary = dashboardController.computeAnnualSalary(salary);
        assertEquals(12000, annualSalary);

        //Negative monthly salary
        salary = -1000;
        annualSalary = dashboardController.computeAnnualSalary(salary);
        assertEquals(0, annualSalary);

        //Zero monthly salary
        salary = 0;
        annualSalary = dashboardController.computeAnnualSalary(salary);
        assertEquals(0, annualSalary);

        //High monthly salary
        salary = 10000;
        annualSalary = dashboardController.computeAnnualSalary(salary);
        assertEquals(120000, annualSalary);
    }
}