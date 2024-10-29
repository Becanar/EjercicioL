package com.example.ejerciciol.dao;

import com.example.ejerciciol.db.ConectorDB;
import com.example.ejerciciol.model.User;

import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class userDao {

    public static User getUsuario(String usuario) {
        ConectorDB connection;
        User user = null;
        try {
            connection = new ConectorDB();
            String consulta = "SELECT usuario,password FROM usuarios WHERE usuario = ?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setString(1, usuario);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String nom_usuario = rs.getString("usuario");
                String password = rs.getString("password");
                user = new User(nom_usuario, password);
            }
            rs.close();
            connection.closeConexion();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

}
