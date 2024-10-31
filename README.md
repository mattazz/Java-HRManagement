# HR Ticket Management System

## Overview
The HR Ticket Management System is a JavaFX-based application designed to manage employee information and HR tickets. It provides functionalities to add, edit, view, and delete employee records, as well as compute annual salaries based on monthly salaries.

<!-- add png -->
![HR Ticket Management System](sample.gif)
## Features
- Add, edit, view, and delete employee records.
- Compute annual salaries from monthly salaries.
- User authentication and session management.
- Responsive UI with JavaFX.
- External CSS for styling.

## Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── com/example/hrmanagement/
│   │       ├── HRApplication.java
│   │       ├── SceneManager.java
│   │       ├── DashboardController.java
│   │       └── ...
│   └── resources/
│       └── com/example/hrmanagement/
│           ├── login-view.fxml
│           ├── admin-employee-view.fxml
│           ├── styles.css
│           └── ...
└── test/
    └── java/
        └── com/example/hrmanagement/
            └── DashboardControllerTest.java
```

### Prerequisites
- Java 11 or higher
- Maven
- IntelliJ IDEA or any other Java IDE
