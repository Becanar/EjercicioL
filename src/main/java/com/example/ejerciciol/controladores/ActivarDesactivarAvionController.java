package com.example.ejerciciol.controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

public class ActivarDesactivarAvionController {

    @FXML
    private RadioButton btActivado;

    @FXML
    private Button btCancelar;

    @FXML
    private RadioButton btDesactivado;

    @FXML
    private Button btGuardar;

    @FXML
    private ComboBox<?> comboAeropuerto;

    @FXML
    private ComboBox<?> comboAvion;

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

    @FXML
    void cancelar(ActionEvent event) {

    }

    @FXML
    void guardar(ActionEvent event) {

    }

}
