module com.example.hrmanagement {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;
    requires java.sql;

    opens com.example.hrmanagement to javafx.fxml;
    exports com.example.hrmanagement;
}