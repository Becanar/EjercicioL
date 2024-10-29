package com.example.ejerciciol.controladores;

import com.example.ejerciciol.dao.aeropuertoDao;
import com.example.ejerciciol.dao.avionDao;
import com.example.ejerciciol.model.Aeropuerto;
import com.example.ejerciciol.model.Avion;
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

public class AniadirAvionController implements Initializable {

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
    private Label lblAeropuerto;

    @FXML
    private Label lblAsiento;

    @FXML
    private Label lblDatos;

    @FXML
    private Label lblModelo;

    @FXML
    private Label lblVelMax;

    @FXML
    private FlowPane panelBotones;

    @FXML
    private ToggleGroup rbGroup;

    @FXML
    private GridPane rootPane;

    @FXML
    private TextField txtAsiento;

    @FXML
    private TextField txtModelo;

    @FXML
    private TextField txtVelMax;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Aeropuerto> aeropuertos = aeropuertoDao.cargarListado();
        comboAeropuerto.setItems(aeropuertos);
        comboAeropuerto.getSelectionModel().select(0);
    }

    @FXML
    void cancelar(ActionEvent event) {
        Stage stage = (Stage)txtAsiento.getScene().getWindow();
        stage.close();
    }

    @FXML
    void guardar(ActionEvent event) {
        String error = "";
        int num_asientos = 0;
        int vel_max = 0;
        if (txtModelo.getText().isEmpty()) {
            error = "El modelo del avión no puede estar vacío";
        }
        if (txtAsiento.getText().isEmpty()) {
            if (!error.isEmpty()) {
                error += "\n";
            }
            error += "El número de asientos del avión no puede estar vacío";
        } else {
            try {
                num_asientos = Integer.parseInt(txtAsiento.getText());
            } catch (NumberFormatException e) {
                if (!error.isEmpty()) {
                    error += "\n";
                }
                error += "El número de asientos del avión tiene que ser un número entero";
            }
        }
        if (txtVelMax.getText().isEmpty()) {
            if (!error.isEmpty()) {
                error += "\n";
            }
            error += "La velocidad máxima del avión no puede estar vacío";
        } else {
            try {
                vel_max = Integer.parseInt(txtVelMax.getText());
            } catch (NumberFormatException e) {
                if (!error.isEmpty()) {
                    error += "\n";
                }
                error += "La velocidad máxima del avión tiene que ser un número entero";
            }
        }
        if (!error.isEmpty()) {
            alerta(error);
        } else {
            Avion avion = new Avion();
            avion.setModelo(txtModelo.getText());
            avion.setNumero_asientos(num_asientos);
            avion.setVelocidad_maxima(vel_max);
            avion.setActivado(btActivado.isSelected());
            avion.setAeropuerto(comboAeropuerto.getSelectionModel().getSelectedItem());
            ObservableList<Avion> aviones = avionDao.cargarListado();
            if (aviones.contains(avion)) {
                alerta("Este modelo ya existe en el aeropuerto. Elige otro modelo u otro aeropuerto");
            } else {
                int resultado = avionDao.insertar(avion);
                if (resultado == -1) {
                    alerta("Ha habido un error insertando el avión. Por favor inténtelo de nuevo");
                } else {
                    confirmacion("Avión insertado correctamente!");
                    Stage stage = (Stage)txtAsiento.getScene().getWindow();
                    stage.close();
                }
            }
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
