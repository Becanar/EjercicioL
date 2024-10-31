module com.example.ejerciciol {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;


    exports com.example.ejerciciol.app;
    opens com.example.ejerciciol.model to javafx.base;
    opens com.example.ejerciciol.app to javafx.fxml;
    exports com.example.ejerciciol.controladores;
    opens com.example.ejerciciol.controladores to javafx.fxml;
}