package com.example.hrmanagement;

/**
 * Singleton class to store the current admin's session information.
 */
public class AdminSession {
    private static AdminSession instance;
    private String adminName;
    private String adminEmail;

    private AdminSession() {
        // Private constructor to prevent instantiation
    }

    public static AdminSession getInstance() {
        if (instance == null) {
            instance = new AdminSession();
        }
        return instance;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public void clearSession() {
        this.adminName = null;
        this.adminEmail = null;
    }
}