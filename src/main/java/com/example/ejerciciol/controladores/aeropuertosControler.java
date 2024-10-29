package com.example.ejerciciol.controladores;

import com.example.ejerciciol.dao.aeropuertoPrivadoDao;
import com.example.ejerciciol.dao.aeropuertoPublicoDao;
import com.example.ejerciciol.model.Aeropuerto;
import com.example.ejerciciol.model.AeropuertoPrivado;
import com.example.ejerciciol.model.AeropuertoPublico;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class aeropuertosControler {

    @FXML
    private MenuItem actDesAvion;

    @FXML
    private MenuItem aniadirAeropuerto;

    @FXML
    private MenuItem aniadirAvion;

    @FXML
    private MenuBar barraMenu;

    @FXML
    private MenuItem borrarAeropuerto;

    @FXML
    private MenuItem borrarAvion;

    @FXML
    private RadioButton btPrivados;

    @FXML
    private RadioButton btPublicos;

    @FXML
    private MenuItem editarAeropuerto;

    @FXML
    private MenuItem infoAeropuerto;

    @FXML
    private Label lblListado;

    @FXML
    private Label lblNombre;

    @FXML
    private Menu menuAeropuertos;

    @FXML
    private Menu menuAviones;

    @FXML
    private Menu menuAyuda;

    @FXML
    private HBox panelBotones;

    @FXML
    private TilePane panelCentro;

    @FXML
    private HBox panelHueco;

    @FXML
    private FlowPane panelListado;

    @FXML
    private ToggleGroup rbGroup;

    @FXML
    private VBox rootPane;

    @FXML
    private TableView<?> tablaVista;

    @FXML
    private TextField txtNombre;


    public void aniadirAeropuerto(ActionEvent actionEvent) {
    }

    public void editarAeropuerto(ActionEvent actionEvent) {
    }

    public void borrarAeropuerto(ActionEvent actionEvent) {
    }

    public void infoAeropuerto(ActionEvent actionEvent) {
    }

    public void aniadirAvion(ActionEvent actionEvent) {
    }

    public void activarDesactivarAvion(ActionEvent actionEvent) {
    }

    public void borrarAvion(ActionEvent actionEvent) {
    }
}
