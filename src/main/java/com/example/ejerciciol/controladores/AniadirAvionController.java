package com.example.ejerciciol.controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

public class AniadirAvionController {

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

    @FXML
    void cancelar(ActionEvent event) {

    }

    @FXML
    void guardar(ActionEvent event) {

    }

}
