module com.example.ejerciciol {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.ejerciciol to javafx.fxml;
    exports com.example.ejerciciol;
    exports com.example.ejerciciol.app;
    opens com.example.ejerciciol.app to javafx.fxml;
}