package com.example.ejerciciol.controladores;

import com.example.ejerciciol.dao.aeropuertoDao;
import com.example.ejerciciol.dao.avionDao;
import com.example.ejerciciol.model.Aeropuerto;
import com.example.ejerciciol.model.Avion;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class BorrarAvionController implements Initializable {

    @FXML
    private Button btCancelar;

    @FXML
    private Button btGuardar;

    @FXML
    private ComboBox<Aeropuerto> comboAeropuerto;

    @FXML
    private ComboBox<Avion> comboAvion;

    @FXML
    private Label lblAeropuertos;

    @FXML
    private Label lblAviones;

    @FXML
    private Label lblBorrar;

    @FXML
    private FlowPane panelBotones;

    @FXML
    private GridPane rootPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Aeropuertos
        ObservableList<Aeropuerto> aeropuertos = aeropuertoDao.cargarListado();
        comboAeropuerto.setItems(aeropuertos);
        comboAeropuerto.getSelectionModel().select(0);
        comboAeropuerto.valueProperty().addListener(new ChangeListener<Aeropuerto>() {
            @Override
            public void changed(ObservableValue<? extends Aeropuerto> observableValue, Aeropuerto oldValue, Aeropuerto newValue) {
                cambioAeropuerto(newValue);
            }
        });
        cambioAeropuerto(comboAeropuerto.getSelectionModel().getSelectedItem());
    }

    public void cambioAeropuerto(Aeropuerto aeropuerto) {
        if (aeropuerto != null) {
            ObservableList<Avion> aviones = avionDao.cargarListado(aeropuerto);
            comboAvion.setItems(aviones);
            comboAvion.getSelectionModel().select(0);
        }
    }

    @FXML
    void cancelar(ActionEvent event) {
        Stage stage = (Stage)comboAeropuerto.getScene().getWindow();
        stage.close();
    }

    @FXML
    void guardar(ActionEvent event) {
        Avion avion = comboAvion.getSelectionModel().getSelectedItem();
        boolean resultado = avionDao.eliminar(avion);
        if (resultado) {
            confirmacion("Avión eliminado correctamente");
            Stage stage = (Stage)comboAeropuerto.getScene().getWindow();
            stage.close();
        } else {
            alerta("Ha habido un error eliminado el avión. Por favor inténtelo de nuevo");
        }
    }

    public void alerta(String texto) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setHeaderText(null);
        alerta.setTitle("ERROR");
        alerta.setContentText(texto);
        alerta.showAndWait();
    }

    public void confirmacion(String texto) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle("Info");
        alerta.setContentText(texto);
        alerta.showAndWait();
    }

}
