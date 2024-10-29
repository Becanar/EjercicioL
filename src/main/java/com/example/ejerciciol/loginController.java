package com.example.ejerciciol;

import com.example.ejerciciol.dao.userDao;
import com.example.ejerciciol.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;

public class loginController {

    @FXML
    private Button btLogin;

    @FXML
    private Label lblPassword;

    @FXML
    private Label lblUsuario;

    @FXML
    private GridPane rootPane;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUsuario;

    @FXML
    void login(ActionEvent event) {
        ArrayList<String> lst=new ArrayList<>();
        String usuario = txtUsuario.getText();
        String password = txtPassword.getText();
        if (usuario.equals("")) {
            lst.add("El campo usuario no puede estar vacío.");
        }
        if (password.equals("")) {
            lst.add("El campo password no puede estar vacío");
        }
        if (!lst.isEmpty()) {
            error(lst);
        } else {
            User user = userDao.getUsuario(usuario);
            if (user == null) {
               lst.add("Usuario no válido");
                txtUsuario.setText("");
                txtPassword.setText("");
            } else {
                if (password.equals(user.getPassword())) {
                    System.out.println("Login correcto");
                   /* try {
                        FXMLLoader fxmlLoader = new FXMLLoader(loginController.class.getResource("/fxml/Aeropuertos.fxml"));
                        Scene scene = new Scene(fxmlLoader.load());
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.setTitle("AVIONES - AEROPUERTOS");
                        stage.show();
                        Stage actual = (Stage) txtUsuario.getScene().getWindow();
                        actual.close();

                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                        error("Error abriendo ventana, por favor inténtelo de nuevo");
                    }*/
                } else {
                    lst.add("Contraseña incorrecta");
                    txtPassword.setText("");
                }
            }
        }
    }

    private void error(ArrayList<String> lst) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(btLogin.getScene().getWindow());
        alert.setHeaderText(null);
        alert.setTitle("Error");
        String error = String.join("\n", lst);
        alert.setContentText(error);
        alert.showAndWait();
    }

}
