package com.example.ejerciciol.controladores;

import com.example.ejerciciol.dao.aeropuertoDao;
import com.example.ejerciciol.dao.aeropuertoPrivadoDao;
import com.example.ejerciciol.dao.aeropuertoPublicoDao;
import com.example.ejerciciol.dao.direccionDao;
import com.example.ejerciciol.model.Aeropuerto;
import com.example.ejerciciol.model.AeropuertoPrivado;
import com.example.ejerciciol.model.AeropuertoPublico;
import com.example.ejerciciol.model.Direccion;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class DatosAeropuertoController implements Initializable {

    private Object aeropuerto=null;
    private Aeropuerto ap;
    @FXML
    private Button btCancelar;

    @FXML
    private Button btGuardar;

    @FXML
    private RadioButton btPrivado;

    @FXML
    private RadioButton btPublico;

    @FXML
    private Label lblAnio;

    @FXML
    private Label lblCalle;

    @FXML
    private Label lblCapacidad;

    @FXML
    private Label lblCiudad;

    @FXML
    private Label lblDatos;

    @FXML
    private Label lblFinanciacion;

    @FXML
    private Label lblNombre;

    @FXML
    private Label lblNumTrab;

    @FXML
    private Label lblNumero;

    @FXML
    private Label lblPais;

    @FXML
    private FlowPane panelBotones;

    @FXML
    private FlowPane panelRB;

    @FXML
    private ToggleGroup rbTipo;

    @FXML
    private GridPane rootPane;

    @FXML
    private TextField txtAnio;

    @FXML
    private TextField txtCalle;

    @FXML
    private TextField txtCapacidad;

    @FXML
    private TextField txtCiudad;

    @FXML
    private TextField txtFInanciacion;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtNumTrab;

    @FXML
    private TextField txtNumero;

    @FXML
    private TextField txtPais;
    public DatosAeropuertoController(Object aeropuerto) {
        this.aeropuerto = aeropuerto;
    }
    public DatosAeropuertoController() {}
    public void setAeropuerto(Object aeropuerto) {
        this.aeropuerto = aeropuerto;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        rbTipo.selectedToggleProperty().addListener(this::cambioTipo);

        if (aeropuerto == null) {

            System.out.println("Null");
        } else {

            System.out.println("Not Null");
            Aeropuerto airport;
            if (aeropuerto instanceof AeropuertoPublico) {

                AeropuertoPublico aeropuertoPublico = (AeropuertoPublico) aeropuerto;
                airport = aeropuertoPublico.getAeropuerto();
                txtFInanciacion.setText(aeropuertoPublico.getFinanciacion().toString());
                txtNumTrab.setText(aeropuertoPublico.getNum_trabajadores() + "");
            } else {

                AeropuertoPrivado aeropuertoPrivado = (AeropuertoPrivado) aeropuerto;
                airport = aeropuertoPrivado.getAeropuerto();
                rbTipo.selectToggle(btPrivado);
                txtFInanciacion.setText(aeropuertoPrivado.getNumero_socios() + "");
                txtNumTrab.setText("");
            }
            this.ap = airport;
            btPublico.setDisable(true);
            btPrivado.setDisable(true);

            txtNombre.setText(airport.getNombre());
            txtPais.setText(airport.getDireccion().getPais());
            txtCiudad.setText(airport.getDireccion().getCiudad());
            txtCalle.setText(airport.getDireccion().getCalle());
            txtNumero.setText(airport.getDireccion().getNumero() + "");
            txtAnio.setText(airport.getAnio_inauguracion() + "");
            txtCapacidad.setText(airport.getCapacidad() + "");
        }
    }

    @FXML
    void guardar(ActionEvent event) {
        String error = "";
        if (txtNombre.getText().isEmpty()) {
            error = "Campo nombre no puede estar vacío";
        }
        if (txtPais.getText().isEmpty()) {
            if (!error.isEmpty()) {
                error += "\n";
            }
            error += "Campo país no puede estar vacío";
        }
        if (txtCiudad.getText().isEmpty()) {
            if (!error.isEmpty()) {
                error += "\n";
            }
            error += "Campo ciudad no puede estar vacío";
        }
        if (txtCalle.getText().isEmpty()) {
            if (!error.isEmpty()) {
                error += "\n";
            }
            error += "Campo calle no puede estar vacío";
        }
        if (txtNumero.getText().isEmpty()) {
            if (!error.isEmpty()) {
                error += "\n";
            }
            error += "Campo número no puede estar vacío";
        } else {
            try {
                int numero = Integer.parseInt(txtNumero.getText());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                if (!error.isEmpty()) {
                    error += "\n";
                }
                error += "Campo número tiene que ser numérico";
            }
        }
        if (txtAnio.getText().isEmpty()) {
            if (!error.isEmpty()) {
                error += "\n";
            }
            error += "Campo año de inauguración no puede estar vacío";
        } else {
            try {
                int anio_inauguracion = Integer.parseInt(txtAnio.getText());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                if (!error.isEmpty()) {
                    error += "\n";
                }
                error += "Campo año de inauguración tiene que ser numérico";
            }
        }
        if (txtCapacidad.getText().isEmpty()) {
            if (!error.isEmpty()) {
                error += "\n";
            }
            error += "Campo capacidad no puede estar vacío";
        } else {
            try {
                int capacidad = Integer.parseInt(txtCapacidad.getText());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                if (!error.isEmpty()) {
                    error += "\n";
                }
                error += "Campo capacidad tiene que ser numérico";
            }
        }
        if (btPublico.isSelected()) {
            // Aeropuerto Público
            if (txtFInanciacion.getText().isEmpty()) {
                if (!error.isEmpty()) {
                    error += "\n";
                }
                error += "Campo financiación no puede estar vacío";
            } else {
                if (!txtFInanciacion.getText().matches("^-?[0-9]+([\\.,][0-9]+)?$")) {
                    if (!error.isEmpty()) {
                        error += "\n";
                    }
                    error += "Campo financiación tiene que ser decimal";
                }
            }
            if (txtNumTrab.getText().isEmpty()) {
                if (!error.isEmpty()) {
                    error += "\n";
                }
                error += "Campo número de trabajadores no puede estar vacío";
            } else {
                try {
                    int capacidad = Integer.parseInt(txtNumTrab.getText());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    if (!error.isEmpty()) {
                        error += "\n";
                    }
                    error += "Campo número de trabajadores tiene que ser numérico";
                }
            }
        } else {
            // Aeropuerto Privado
            if (txtFInanciacion.getText().isEmpty()) {
                if (!error.isEmpty()) {
                    error += "\n";
                }
                error += "Campo número de socios no puede estar vacío";
            } else {
                try {
                    int capacidad = Integer.parseInt(txtFInanciacion.getText());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    if (!error.isEmpty()) {
                        error += "\n";
                    }
                    error += "Campo número de socios tiene que ser numérico";
                }
            }
        }
        // Fin verificación de datos
        if (!error.isEmpty()) {
            alerta(error);
        } else {
            // Correcto
            boolean resultado;
            if (this.aeropuerto == null) {
                resultado = crearAeropuerto();
            } else {
                resultado = modificarAeropuerto();
            }
            if (resultado) {
                Stage stage = (Stage)txtNombre.getScene().getWindow();
                stage.close();
            }
        }
    }


    public boolean crearAeropuerto() {
        Direccion direccion = new Direccion();
        direccion.setPais(txtPais.getText());
        direccion.setCiudad(txtCiudad.getText());
        direccion.setCalle(txtCalle.getText());
        direccion.setNumero(Integer.parseInt(txtNumero.getText()));
        int id_direccion = direccionDao.insertar(direccion);
        if (id_direccion == -1) {
            alerta("Ha habido un error almacenando los datos. Por favor vuelva a intentarlo");
            return false;
        } else {
            direccion.setId(id_direccion);
            Aeropuerto airport = new Aeropuerto();
            airport.setNombre(txtNombre.getText());
            airport.setDireccion(direccion);
            airport.setAnio_inauguracion(Integer.parseInt(txtAnio.getText()));
            airport.setCapacidad(Integer.parseInt(txtCapacidad.getText()));
            airport.setImagen(null);
            int id_aeropuerto = aeropuertoDao.insertar(airport);
            if (id_aeropuerto == -1) {
                alerta("Ha habido un error almacenando los datos. Por favor vuelva a intentarlo");
                return false;
            } else {
                airport.setId(id_aeropuerto);
                if (btPublico.isSelected()) {
                    // Aeropuerto Público
                    AeropuertoPublico aeropuertoPublico = new AeropuertoPublico(airport, new BigDecimal(txtFInanciacion.getText()),Integer.parseInt(txtNumTrab.getText()));
                    if (!aeropuertoPublicoDao.insertar(aeropuertoPublico)) {
                        alerta("Ha habido un error almacenando los datos. Por favor vuelva a intentarlo");
                        return false;
                    }
                } else {
                    // Aeropuerto Privado
                    AeropuertoPrivado aeropuertoPrivado = new AeropuertoPrivado(airport,Integer.parseInt(txtFInanciacion.getText()));
                    if (!aeropuertoPrivadoDao.insertar(aeropuertoPrivado)) {
                        alerta("Ha habido un error almacenando los datos. Por favor vuelva a intentarlo");
                        return false;
                    }
                }
                confirmacion("Aeropuerto creado correctamente");
                return true;
            }
        }
    }

    public boolean modificarAeropuerto() {
        Aeropuerto airport = new Aeropuerto();
        Direccion direccion = new Direccion();
        direccion.setId(ap.getDireccion().getId());
        direccion.setPais(txtPais.getText());
        direccion.setCiudad(txtCiudad.getText());
        direccion.setCalle(txtCalle.getText());
        direccion.setNumero(Integer.parseInt(txtNumero.getText()));
        if (!direccionDao.modificar(this.ap.getDireccion(),direccion)) {
            alerta("Ha habido un error almacenando los datos. Por favor vuelva a intentarlo");
            return false;
        } else {
            airport.setDireccion(direccion);
            airport.setNombre(txtNombre.getText());
            airport.setAnio_inauguracion(Integer.parseInt(txtAnio.getText()));
            airport.setCapacidad(Integer.parseInt(txtCapacidad.getText()));
            airport.setImagen(null);
            if (!aeropuertoDao.modificar(ap,airport)) {
                alerta("Ha habido un error almacenando los datos. Por favor vuelva a intentarlo");
                return false;
            } else {
                if (this.aeropuerto instanceof AeropuertoPublico) {
                    AeropuertoPublico aeropuertoPublico = (AeropuertoPublico) this.aeropuerto;
                    AeropuertoPublico newAirport = new AeropuertoPublico(airport,new BigDecimal(txtFInanciacion.getText()),Integer.parseInt(txtNumTrab.getText()));
                    if (!aeropuertoPublicoDao.modificar(aeropuertoPublico,newAirport)) {
                        alerta("Ha habido un error almacenando los datos. Por favor vuelva a intentarlo");
                        return false;
                    }
                } else {
                    AeropuertoPrivado aeropuertoPrivado = (AeropuertoPrivado) this.aeropuerto;
                    AeropuertoPrivado newAirport = new AeropuertoPrivado(airport,Integer.parseInt(txtFInanciacion.getText()));
                    if (!aeropuertoPrivadoDao.modificar(aeropuertoPrivado,newAirport)) {
                        alerta("Ha habido un error almacenando los datos. Por favor vuelva a intentarlo");
                        return false;
                    }
                }
                confirmacion("Aeropuerto actualizado correctamente");
                return true;
            }
        }
    }


    public void cambioTipo(ObservableValue<? extends Toggle> observableValue, Toggle oldBtn, Toggle newBtn) {
        if (newBtn.equals(btPublico)) {
            // Aeropuerto Público
            lblFinanciacion.setText("Financiación:");
            lblNumTrab.setText("Número de trabajadores:");
            txtNumTrab.setVisible(true);
        } else {
            // Aeropuerto Privado
            lblFinanciacion.setText("Número de socios:");
            lblNumTrab.setText(null);
            txtNumTrab.setVisible(false);
        }
    }

    @FXML
    void cancelar(ActionEvent event) {
        Stage stage = (Stage)txtNombre.getScene().getWindow();
        stage.close();
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
