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
            DatosAeropuertoController controlador = new DatosAeropuertoController();
            fxmlLoader.setController(controlador);
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            try {
                Image img = new Image(getClass().getResource("/com/example/ejerciciol/images/avion.png").toString());
                stage.getIcons().add(img);
            } catch (Exception e) {
                System.out.println("Error al cargar la imagen: " + e.getMessage());
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
            System.err.println(e.getMessage());
            alerta("Error abriendo ventana, por favor inténtelo de nuevo");
        }
    }


    @FXML
    void editarAeropuerto(ActionEvent event) {
        Object aeropuerto = tablaVista.getSelectionModel().getSelectedItem();
        if (aeropuerto == null) {
            alerta("Selecciona un aeropuerto antes de editarlo");
        } else {
            try {
                Window ventana = btPrivados.getScene().getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/ejerciciol/fxml/DatosAeropuerto.fxml"));
                DatosAeropuertoController controlador = new DatosAeropuertoController(aeropuerto);
                fxmlLoader.setController(controlador);
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                try {
                    Image img = new Image(getClass().getResource("/com/example/ejerciciol/images/avion.png").toString());
                    stage.getIcons().add(img);
                } catch (Exception e) {
                    System.out.println("Error al cargar la imagen: " + e.getMessage());
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
                System.err.println(e.getMessage());
                alerta("Error abriendo ventana, por favor inténtelo de nuevo");
            }
        }
    }

    @FXML
    void borrarAeropuerto(ActionEvent event) {
        Object aeropuerto = tablaVista.getSelectionModel().getSelectedItem();
        if (aeropuerto == null) {
            alerta("Selecciona un aeropuerto antes de eliminarlo");
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
                            alerta("No se pudo eliminar ese aeropuerto. Inténtelo de nuevo");
                            return;
                        }
                    }
                    Aeropuerto airport = aeropuertoPublico.getAeropuerto();
                    if (aeropuertoPublicoDao.eliminar(aeropuertoPublico)) {
                        if (aeropuertoDao.eliminar(airport)) {
                            cargarPublicos();
                            confirmacion("Aeropuerto eliminado correctamente");
                        } else {
                            alerta("No se pudo eliminar ese aeropuerto. Inténtelo de nuevo");
                        }
                    } else {
                        alerta("No se pudo eliminar ese aeropuerto. Inténtelo de nuevo");
                    }
                } else {
                    AeropuertoPrivado aeropuertoPrivado = (AeropuertoPrivado) aeropuerto;
                    ObservableList<Avion> aviones = avionDao.cargarListado(aeropuertoPrivado.getAeropuerto());
                    for (Avion avion : aviones) {
                        if (!avionDao.eliminar(avion)) {
                            alerta("No se pudo eliminar ese aeropuerto. Inténtelo de nuevo");
                            return;
                        }
                    }
                    Aeropuerto airport = aeropuertoPrivado.getAeropuerto();
                    if (aeropuertoPrivadoDao.eliminar(aeropuertoPrivado)) {
                        if (aeropuertoDao.eliminar(airport)) {
                            cargarPrivados();
                            confirmacion("Aeropuerto eliminado correctamente");
                        } else {
                            alerta("No se pudo eliminar ese aeropuerto. Inténtelo de nuevo");
                        }
                    } else {
                        alerta("No se pudo eliminar ese aeropuerto. Inténtelo de nuevo");
                    }
                }
            }
        }
    }

    @FXML
    void infoAeropuerto(ActionEvent event) {
        Object aeropuerto = tablaVista.getSelectionModel().getSelectedItem();
        if (aeropuerto == null) {
            alerta("Selecciona un aeropuerto antes de ver su información");
        } else {
            String info = "";
            if (aeropuerto instanceof AeropuertoPublico) {
                AeropuertoPublico aeropuertoPublico = (AeropuertoPublico) aeropuerto;
                Aeropuerto airport = aeropuertoPublico.getAeropuerto();
                info = "Nombre: " + airport.getNombre();
                info += "\nPaís:" + airport.getDireccion().getPais();
                info += "\nDirección: C\\ " + airport.getDireccion().getCalle() + " " + airport.getDireccion().getNumero() + ", " + airport.getDireccion().getCiudad();
                info += "\nAño de inauguración: " + airport.getAnio_inauguracion();
                info += "\nCapacidad: " + airport.getCapacidad();
                info += "\nAviones:";
                ObservableList<Avion> aviones = avionDao.cargarListado(airport);
                for (Avion avion : aviones) {
                    info += "\n\tModelo: " + avion.getModelo();
                    info += "\n\tNúmero de asientos: " + avion.getNumero_asientos();
                    info += "\n\tVelocidad máxima: " + avion.getVelocidad_maxima();
                    if (avion.isActivado()) {
                        info += "\n\tActivado";
                    } else {
                        info += "\n\tDesactivado";
                    }
                }
                info += "\nPúblico";
                info += "\nFinanciación: " + aeropuertoPublico.getFinanciacion();
                info += "\nNúmero de trabajadores: " + aeropuertoPublico.getNum_trabajadores();
            } else {
                // Aeropuerto Privado
                AeropuertoPrivado aeropuertoPrivado = (AeropuertoPrivado) aeropuerto;
                Aeropuerto airport = aeropuertoPrivado.getAeropuerto();
                info = "Nombre: " + airport.getNombre();
                info += "\nPaís:" + airport.getDireccion().getPais();
                info += "\nDirección: C\\ " + airport.getDireccion().getCalle() + " " + airport.getDireccion().getNumero() + ", " + airport.getDireccion().getCiudad();
                info += "\nAño de inauguración: " + airport.getAnio_inauguracion();
                info += "\nCapacidad: " + airport.getCapacidad();
                info += "\nAviones:";
                ObservableList<Avion> aviones = avionDao.cargarListado(airport);
                for (Avion avion : aviones) {
                    info += "\n\tModelo: " + avion.getModelo();
                    info += "\n\tNúmero de asientos: " + avion.getNumero_asientos();
                    info += "\n\tVelocidad máxima: " + avion.getVelocidad_maxima();
                    if (avion.isActivado()) {
                        info += "\n\tActivado";
                    } else {
                        info += "\n\tDesactivado";
                    }
                }
                info += "\nPrivado";
                info += "\nNúmero de socios: " + aeropuertoPrivado.getNumero_socios();
            }
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setHeaderText(null);
            alerta.setTitle("Información");
            alerta.setContentText(info);
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
                Image img = new Image(getClass().getResource("/com/example/ejerciciol/images/avion.png").toString());
                stage.getIcons().add(img);
            } catch (Exception e) {
                System.out.println("Error al cargar la imagen: " + e.getMessage());
            }
            stage.setTitle("AVIONES - AÑADIR AVIÓN");
            stage.initOwner(ventana);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            alerta("Error abriendo ventana, por favor inténtelo de nuevo");
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
                Image img = new Image(getClass().getResource("/com/example/ejerciciol/images/avion.png").toString());
                stage.getIcons().add(img);
            } catch (Exception e) {
                System.out.println("Error al cargar la imagen: " + e.getMessage());
            }
            stage.setTitle("AVIONES - ACTIVAR/DESACTIVAR AVIÓN");
            stage.initOwner(ventana);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            alerta("Error abriendo ventana, por favor inténtelo de nuevo");
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
                Image img = new Image(getClass().getResource("/com/example/ejerciciol/images/avion.png").toString());
                stage.getIcons().add(img);
            } catch (Exception e) {
                System.out.println("Error al cargar la imagen: " + e.getMessage());
            }
            stage.setTitle("AVIONES - BORRAR AVIÓN");
            stage.initOwner(ventana);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            alerta("Error abriendo ventana, por favor inténtelo de nuevo");
        }
    }


    public void cargarPublicos() {
        try {
            // Limpiar la tabla y las listas
            tablaVista.getSelectionModel().clearSelection();
            txtNombre.setText(null);
            lstEntera.clear();
            lstFiltrada.clear();
            tablaVista.getItems().clear();
            tablaVista.getColumns().clear();

            // Configuración de columnas
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
                System.out.println("No se encontraron aeropuertos.");
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
