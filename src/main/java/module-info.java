module com.example.ejerciciol {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.ejerciciol to javafx.fxml;
    exports com.example.ejerciciol;
}