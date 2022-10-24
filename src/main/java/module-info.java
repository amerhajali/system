module com.example.students_system {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.students_system to javafx.fxml;
    opens com.example.students_system.model;
    exports com.example.students_system;
}