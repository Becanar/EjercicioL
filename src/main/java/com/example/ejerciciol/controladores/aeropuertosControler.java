package com.example.ejerciciol.controladores;

import com.example.ejerciciol.dao.aeropuertoDao;
import com.example.ejerciciol.dao.aeropuertoPrivadoDao;
import com.example.ejerciciol.dao.aeropuertoPublicoDao;
import com.example.ejerciciol.dao.avionDao;
import com.example.ejerciciol.model.Aeropuerto;
import com.example.ejerciciol.model.AeropuertoPrivado;
import com.example.ejerciciol.model.AeropuertoPublico;
import com.example.ejerciciol.model.Avion;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

public class aeropuertosControler implements Initializable{

    @FXML
    private MenuItem actDesAvion, aniadirAeropuerto, aniadirAvion, borrarAeropuerto, borrarAvion, editarAeropuerto, infoAeropuerto;
    @FXML
    private Menu menuAeropuertos, menuAviones, menuAyuda;
    @FXML
    private RadioButton btPrivados, btPublicos;
    @FXML
    private ToggleGroup rbGroup;
    @FXML
    private Label lblListado, lblNombre;
    @FXML
    private MenuBar barraMenu;
    @FXML
    private TextField txtNombre;
    @FXML
    private HBox panelBotones, panelHueco;
    @FXML
    private TilePane panelCentro;
    @FXML
    private FlowPane panelListado;
    @FXML
    private VBox rootPane;
    @FXML
    private TableView tablaVista;

    private ObservableList lstEntera = FXCollections.observableArrayList();
    private ObservableList lstFiltrada = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tablaVista.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observableValue, Object oldValue, Object newValue) {
                if (newValue != null) {
                    deshabilitarMenus(false);
                } else {
                    deshabilitarMenus(true);
                }
            }
        });
        cargarPublicos();

        rbGroup.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> observable, Toggle oldBtn, Toggle newBtn) -> {
            if (newBtn.equals(btPublicos)) {
                cargarPublicos();
            } else {
                cargarPrivados();
            }
        });
        txtNombre.setOnKeyTyped(keyEvent -> filtrar());
    }

    @FXML
    void aniadirAeropuerto(ActionEvent event) {
        try {
            Window ventana = btPrivados.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/ejerciciol/fxml/DatosAeropuerto.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            try {
                Image img = new Image(getClass().getResource("/com/example/ejerciciol/images/plane.png").toString());
                stage.getIcons().add(img);
            } catch (Exception e) {
                ArrayList<String> lst=new ArrayList<>();
                lst.add("No se ha podido cargar la imagen.");
                alerta(lst);
            }
            stage.setTitle("AVIONES - AÑADIR AEROPUERTO");
            stage.initOwner(ventana);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            if (btPublicos.isSelected()) {
                cargarPublicos();
            } else {
                cargarPrivados();
            }
        } catch (IOException e) {
            ArrayList<String> lst=new ArrayList<>();
            lst.add("No se ha podido abrir la ventana.");
            alerta(lst);
        }
    }


    @FXML
    void editarAeropuerto(ActionEvent event) {
        Object aeropuerto = tablaVista.getSelectionModel().getSelectedItem();
        if (aeropuerto == null) {
            ArrayList<String> lst=new ArrayList<>();
            lst.add("No has seleccionado ningún aeropuerto.");
            alerta(lst);
        } else {
            try {
                Window ventana = btPrivados.getScene().getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/ejerciciol/fxml/DatosAeropuerto.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                try {
                    Image img = new Image(getClass().getResource("/com/example/ejerciciol/images/plane.png").toString());
                    stage.getIcons().add(img);
                } catch (Exception e) {
                    ArrayList<String> lst=new ArrayList<>();
                    lst.add("No se ha podido cargar la imagen.");
                    alerta(lst);
                }
                stage.setTitle("AVIONES - EDITAR AEROPUERTO");
                stage.initOwner(ventana);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
                if (btPublicos.isSelected()) {
                    cargarPublicos();
                } else {
                    cargarPrivados();
                }
            } catch (IOException e) {
                ArrayList<String> lst=new ArrayList<>();
                lst.add("No se ha podido abrir la ventana.");
                alerta(lst);
            }
        }
    }

    @FXML
    void borrarAeropuerto(ActionEvent event) {
        Object aeropuerto = tablaVista.getSelectionModel().getSelectedItem();
        if (aeropuerto == null) {
            ArrayList<String> lst=new ArrayList<>();
            lst.add("No has seleccionado ningún aeropuerto.");
            alerta(lst);
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initOwner(tablaVista.getScene().getWindow());
            alert.setHeaderText(null);
            alert.setTitle("Confirmación");
            alert.setContentText("¿Estás seguro que quieres eliminar ese aeropuerto? Esto también eliminara los aviones en este aeropuerto.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (aeropuerto instanceof AeropuertoPublico) {
                    AeropuertoPublico aeropuertoPublico = (AeropuertoPublico) aeropuerto;
                    ObservableList<Avion> aviones = avionDao.cargarListado(aeropuertoPublico.getAeropuerto());
                    for (Avion avion : aviones) {
                        if (!avionDao.eliminar(avion)) {
                            ArrayList<String> lst=new ArrayList<>();
                            lst.add("No se ha podido eliminar el avión.");
                            alerta(lst);
                            return;
                        }
                    }
                    Aeropuerto airport = aeropuertoPublico.getAeropuerto();
                    if (aeropuertoPublicoDao.eliminar(aeropuertoPublico)) {
                        if (aeropuertoDao.eliminar(airport)) {
                            cargarPublicos();
                            confirmacion("Aeropuerto eliminado correctamente");
                        } else {
                            ArrayList<String> lst=new ArrayList<>();
                            lst.add("No se ha podido eliminar el aeropuerto.");
                            alerta(lst);
                        }
                    } else {
                        ArrayList<String> lst=new ArrayList<>();
                        lst.add("No se ha podido eliminar el aeropuerto.");
                        alerta(lst);
                    }
                } else {
                    AeropuertoPrivado aeropuertoPrivado = (AeropuertoPrivado) aeropuerto;
                    ObservableList<Avion> aviones = avionDao.cargarListado(aeropuertoPrivado.getAeropuerto());
                    for (Avion avion : aviones) {
                        if (!avionDao.eliminar(avion)) {
                            ArrayList<String> lst=new ArrayList<>();
                            lst.add("No se ha podido eliminar el avion.");
                            alerta(lst);
                            return;
                        }
                    }
                    Aeropuerto airport = aeropuertoPrivado.getAeropuerto();
                    if (aeropuertoPrivadoDao.eliminar(aeropuertoPrivado)) {
                        if (aeropuertoDao.eliminar(airport)) {
                            cargarPrivados();
                            confirmacion("Aeropuerto eliminado correctamente");
                        } else {
                            ArrayList<String> lst=new ArrayList<>();
                            lst.add("No se ha podido eliminar el aeropuerto.");
                            alerta(lst);
                        }
                    } else {
                        ArrayList<String> lst=new ArrayList<>();
                        lst.add("No se ha podido eliminar el aeropuerto.");
                        alerta(lst);
                    }
                }
            }
        }
    }

    @FXML
    void infoAeropuerto(ActionEvent event) {
        Object aeropuerto = tablaVista.getSelectionModel().getSelectedItem();
        if (aeropuerto == null) {
            ArrayList<String> errores = new ArrayList<>();
            errores.add("Selecciona un aeropuerto antes de ver su información");
            alerta(errores);
        } else {
            ArrayList<String> info = new ArrayList<>();
            if (aeropuerto instanceof AeropuertoPublico) {
                AeropuertoPublico aeropuertoPublico = (AeropuertoPublico) aeropuerto;
                Aeropuerto airport = aeropuertoPublico.getAeropuerto();

                info.add("Nombre: " + airport.getNombre());
                info.add("País: " + airport.getDireccion().getPais());
                info.add("Dirección: C\\ " + airport.getDireccion().getCalle() + " " + airport.getDireccion().getNumero() + ", " + airport.getDireccion().getCiudad());
                info.add("Año de inauguración: " + airport.getAnio_inauguracion());
                info.add("Capacidad: " + airport.getCapacidad());
                info.add("Aviones:");

                ObservableList<Avion> aviones = avionDao.cargarListado(airport);
                for (Avion avion : aviones) {
                    info.add("\tModelo: " + avion.getModelo());
                    info.add("\tNúmero de asientos: " + avion.getNumero_asientos());
                    info.add("\tVelocidad máxima: " + avion.getVelocidad_maxima());
                    info.add("\t" + (avion.isActivado() ? "Activado" : "Desactivado"));
                }

                info.add("Público");
                info.add("Financiación: " + aeropuertoPublico.getFinanciacion());
                info.add("Número de trabajadores: " + aeropuertoPublico.getNum_trabajadores());

            } else if (aeropuerto instanceof AeropuertoPrivado) {
                AeropuertoPrivado aeropuertoPrivado = (AeropuertoPrivado) aeropuerto;
                Aeropuerto airport = aeropuertoPrivado.getAeropuerto();

                info.add("Nombre: " + airport.getNombre());
                info.add("País: " + airport.getDireccion().getPais());
                info.add("Dirección: C\\ " + airport.getDireccion().getCalle() + " " + airport.getDireccion().getNumero() + ", " + airport.getDireccion().getCiudad());
                info.add("Año de inauguración: " + airport.getAnio_inauguracion());
                info.add("Capacidad: " + airport.getCapacidad());
                info.add("Aviones:");

                ObservableList<Avion> aviones = avionDao.cargarListado(airport);
                for (Avion avion : aviones) {
                    info.add("\tModelo: " + avion.getModelo());
                    info.add("\tNúmero de asientos: " + avion.getNumero_asientos());
                    info.add("\tVelocidad máxima: " + avion.getVelocidad_maxima());
                    info.add("\t" + (avion.isActivado() ? "Activado" : "Desactivado"));
                }

                info.add("Privado");
                info.add("Número de socios: " + aeropuertoPrivado.getNumero_socios());
            }

            // Convertimos el ArrayList a un solo String, con saltos de línea
            String contenido = String.join("\n", info);
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setHeaderText(null);
            alerta.setTitle("Información");
            alerta.setContentText(contenido);
            alerta.showAndWait();
        }
    }

    @FXML
    void aniadirAvion(ActionEvent event) {
        try {
            Window ventana = btPrivados.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/ejerciciol/fxml/AniadirAvion.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            try {
                Image img = new Image(getClass().getResource("/com/example/ejerciciol/images/plane.png").toString());
                stage.getIcons().add(img);
            } catch (Exception e) {
                ArrayList<String> lst=new ArrayList<>();
                lst.add("No se ha podido cargar la imagen.");
                alerta(lst);
            }
            stage.setTitle("AVIONES - AÑADIR AVIÓN");
            stage.initOwner(ventana);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            ArrayList<String> lst=new ArrayList<>();
            lst.add("No se ha podido abrir la ventana.");
            alerta(lst);
        }
    }

    @FXML
    void activarDesactivarAvion(ActionEvent event) {
        try {
            Window ventana = btPrivados.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/ejerciciol/fxml/ActivarDesactivarAvion.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            try {
                Image img = new Image(getClass().getResource("/com/example/ejerciciol/images/plane.png").toString());
                stage.getIcons().add(img);
            } catch (Exception e) {
                ArrayList<String> lst=new ArrayList<>();
                lst.add("No se ha podido cargar la imagen.");
                alerta(lst);
            }
            stage.setTitle("AVIONES - ACTIVAR/DESACTIVAR AVIÓN");
            stage.initOwner(ventana);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            ArrayList<String> lst=new ArrayList<>();
            lst.add("No se ha podido abrir la ventana.");
            alerta(lst);
        }
    }

    @FXML
    void borrarAvion(ActionEvent event) {
        try {
            Window ventana = btPrivados.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/ejerciciol/fxml/BorrarAvion.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            try {
                Image img = new Image(getClass().getResource("/com/example/ejerciciol/images/plane.png").toString());
                stage.getIcons().add(img);
            } catch (Exception e) {
                ArrayList<String> lst=new ArrayList<>();
                lst.add("No se ha podido cargar la imagen.");
                alerta(lst);
            }
            stage.setTitle("AVIONES - BORRAR AVIÓN");
            stage.initOwner(ventana);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            ArrayList<String> lst=new ArrayList<>();
            lst.add("No se ha podido abrir la ventana.");
            alerta(lst);
        }
    }


    public void cargarPublicos() {
        try {
            tablaVista.getSelectionModel().clearSelection();
            txtNombre.setText(null);
            lstEntera.clear();
            lstFiltrada.clear();
            tablaVista.getItems().clear();
            tablaVista.getColumns().clear();

            TableColumn<AeropuertoPublico, Integer> colId = new TableColumn<>("ID");
            colId.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getAeropuerto().getId()));

            TableColumn<AeropuertoPublico, String> colNombre = new TableColumn<>("Nombre");
            colNombre.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getAeropuerto().getNombre()));

            TableColumn<AeropuertoPublico, String> colPais = new TableColumn<>("País");
            colPais.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getAeropuerto().getDireccion().getPais()));

            TableColumn<AeropuertoPublico, String> colCiudad = new TableColumn<>("Ciudad");
            colCiudad.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getAeropuerto().getDireccion().getCiudad()));

            TableColumn<AeropuertoPublico, String> colCalle = new TableColumn<>("Calle");
            colCalle.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getAeropuerto().getDireccion().getCalle()));

            TableColumn<AeropuertoPublico, Integer> colNumero = new TableColumn<>("Número");
            colNumero.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getAeropuerto().getDireccion().getNumero()));

            TableColumn<AeropuertoPublico, Integer> colAnio = new TableColumn<>("Año");
            colAnio.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getAeropuerto().getAnio_inauguracion()));

            TableColumn<AeropuertoPublico, Integer> colCapacidad = new TableColumn<>("Capacidad");
            colCapacidad.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getAeropuerto().getCapacidad()));

            TableColumn<AeropuertoPublico, BigDecimal> colFinanciacion = new TableColumn<>("Financiación");
            colFinanciacion.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getFinanciacion()));

            TableColumn<AeropuertoPublico, Integer> colTrabajadores = new TableColumn<>("Nº Trabajadores");
            colTrabajadores.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getNum_trabajadores()));

            tablaVista.getColumns().addAll(colId, colNombre, colPais, colCiudad, colCalle, colNumero, colAnio, colCapacidad, colFinanciacion, colTrabajadores);
            ObservableList<AeropuertoPublico> aeropuertos = aeropuertoPublicoDao.cargarListado();

            if (aeropuertos != null && !aeropuertos.isEmpty()) {
                lstEntera.setAll(aeropuertos);
                tablaVista.setItems(aeropuertos);
            } else {
                ArrayList<String> lst=new ArrayList<>();
                lst.add("No se encontraron Aeropuertos.");
                alerta(lst);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Manejo de errores
        }
    }


    public void cargarPrivados() {

        tablaVista.getSelectionModel().clearSelection();
        txtNombre.setText(null);
        lstEntera.clear();
        lstFiltrada.clear();
        tablaVista.getItems().clear();
        tablaVista.getColumns().clear();

        TableColumn<AeropuertoPrivado, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getAeropuerto().getId()));
        TableColumn<AeropuertoPrivado, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getAeropuerto().getNombre()));
        TableColumn<AeropuertoPrivado, String> colPais = new TableColumn<>("País");
        colPais.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getAeropuerto().getDireccion().getPais()));
        TableColumn<AeropuertoPrivado, String> colCiudad = new TableColumn<>("Ciudad");
        colCiudad.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getAeropuerto().getDireccion().getCiudad()));
        TableColumn<AeropuertoPrivado, String> colCalle = new TableColumn<>("Calle");
        colCalle.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getAeropuerto().getDireccion().getCalle()));
        TableColumn<AeropuertoPrivado, Integer> colNumero = new TableColumn<>("Número");
        colNumero.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getAeropuerto().getDireccion().getNumero()));
        TableColumn<AeropuertoPrivado, Integer> colAnio = new TableColumn<>("Año");
        colAnio.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getAeropuerto().getAnio_inauguracion()));
        TableColumn<AeropuertoPrivado, Integer> colCapacidad = new TableColumn<>("Capacidad");
        colCapacidad.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getAeropuerto().getCapacidad()));
        TableColumn<AeropuertoPrivado, Integer> colSocios = new TableColumn<>("Nº Socios");
        colSocios.setCellValueFactory(new PropertyValueFactory("numero_socios"));
        tablaVista.getColumns().addAll(colId, colNombre, colPais, colCiudad, colCalle, colNumero, colAnio, colCapacidad, colSocios);
        ObservableList<AeropuertoPrivado> aeropuertos = aeropuertoPrivadoDao.cargarListado();
        lstEntera.setAll(aeropuertos);
        tablaVista.setItems(aeropuertos);
    }

    public void deshabilitarMenus(boolean deshabilitado) {
        editarAeropuerto.setDisable(deshabilitado);
        borrarAeropuerto.setDisable(deshabilitado);
        infoAeropuerto.setDisable(deshabilitado);
    }

    public void filtrar() {
        String valor = txtNombre.getText();
        valor = valor.toLowerCase();
        if (valor.isEmpty()) {
            tablaVista.setItems(lstEntera);
        } else {
            lstFiltrada.clear();
            if (lstEntera.getFirst() instanceof AeropuertoPublico) {
                for (Object aeropuerto : lstEntera) {
                    AeropuertoPublico aeropuertoPublico = (AeropuertoPublico) aeropuerto;
                    String nombre = aeropuertoPublico.getAeropuerto().getNombre();
                    nombre = nombre.toLowerCase();
                    if (nombre.contains(valor)) {
                        lstFiltrada.add(aeropuertoPublico);
                    }
                }
            } else {
                for (Object aeropuerto : lstEntera) {
                    AeropuertoPrivado aeropuertoPrivado = (AeropuertoPrivado) aeropuerto;
                    String nombre = aeropuertoPrivado.getAeropuerto().getNombre();
                    nombre = nombre.toLowerCase();
                    if (nombre.startsWith(valor)) {
                        lstFiltrada.add(aeropuertoPrivado);
                    }
                }
            }
            tablaVista.setItems(lstFiltrada);
        }
    }

    public void alerta(ArrayList<String> textos) {
        String contenido = String.join("\n", textos);
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setHeaderText(null);
        alerta.setTitle("ERROR");
        alerta.setContentText(contenido);
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
