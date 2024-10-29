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
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ActivarDesactivarAvionController implements Initializable {

    @FXML
    private RadioButton btActivado;

    @FXML
    private Button btCancelar;

    @FXML
    private RadioButton btDesactivado;

    @FXML
    private Button btGuardar;

    @FXML
    private ComboBox<Aeropuerto> comboAeropuerto;

    @FXML
    private ComboBox<Avion> comboAvion;

    @FXML
    private Label lblActDes;

    @FXML
    private Label lblAeropuerto;

    @FXML
    private Label lblAviones;

    @FXML
    private FlowPane panelBotones;

    @FXML
    private ToggleGroup rbGroup;

    @FXML
    private GridPane rootPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
        comboAvion.valueProperty().addListener(new ChangeListener<Avion>() {
            @Override
            public void changed(ObservableValue<? extends Avion> observableValue, Avion oldValue, Avion newValue) {
                cambioAvion(newValue);
            }
        });
    }

    public void cambioAeropuerto(Aeropuerto aeropuerto) {
        if (aeropuerto != null) {
            ObservableList<Avion> aviones = avionDao.cargarListado(aeropuerto);
            comboAvion.setItems(aviones);
            comboAvion.getSelectionModel().select(0);
        }
    }

    public void cambioAvion(Avion avion) {
        if (avion != null) {
            boolean activado = avion.isActivado();
            if (activado) {
                btActivado.setSelected(true);
                btDesactivado.setSelected(false);
            } else {
                btActivado.setSelected(false);
                btDesactivado.setSelected(true);
            }
        }
    }

    @FXML
    void cancelar(ActionEvent event) {
        Stage stage = (Stage)comboAeropuerto.getScene().getWindow();
        stage.close();
    }

    @FXML
    void guardar(ActionEvent event) {
        boolean activado = btActivado.isSelected();
        Avion avion = comboAvion.getSelectionModel().getSelectedItem();
        Avion avionNuevo = new Avion(avion.getId(),avion.getModelo(),avion.getNumero_asientos(),avion.getVelocidad_maxima(),activado,avion.getAeropuerto());
        boolean resultado = avionDao.modificar(avion, avionNuevo);
        if (resultado) {
            confirmacion("Avión modificado correctamente");
            Stage stage = (Stage)comboAeropuerto.getScene().getWindow();
            stage.close();
        } else {
            alerta("Ha habido un error actualizando el avión. Por favor intentalo de nuevo");
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
