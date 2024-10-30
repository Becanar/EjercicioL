package com.example.ejerciciol.dao;

import com.example.ejerciciol.db.ConectorDB;
import com.example.ejerciciol.model.Direccion;

import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class direccionDao {
    public static Direccion getDireccion(int id) {
        ConectorDB connection;
        Direccion direccion = null;
        try {
            connection = new ConectorDB();
            String consulta = "SELECT id,pais,ciudad,calle,numero FROM direcciones WHERE id = ?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String pais = rs.getString("pais");
                String ciudad = rs.getString("ciudad");
                String calle = rs.getString("calle");
                int numero = rs.getInt("numero");
                direccion = new Direccion(id, pais, ciudad, calle, numero);
            }
            rs.close();
            connection.closeConexion();
        } catch (SQLException | FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
        return direccion;
    }

    public static boolean modificar(Direccion direccion, Direccion direccionNueva) {
        ConectorDB connection;
        PreparedStatement pstmt;
        try {
            connection = new ConectorDB();
            String consulta = "UPDATE direcciones SET pais = ?,ciudad = ?,calle = ?,numero = ? WHERE id = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setString(1, direccionNueva.getPais());
            pstmt.setString(2, direccionNueva.getCalle());
            pstmt.setString(3, direccionNueva.getCalle());
            pstmt.setInt(4, direccionNueva.getNumero());
            pstmt.setInt(5, direccion.getId());
            int filasAfectadas = pstmt.executeUpdate();
            pstmt.close();
            connection.closeConexion();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public  static int insertar(Direccion direccion) {
        ConectorDB connection;
        PreparedStatement pstmt;
        try {
            connection = new ConectorDB();
            String consulta = "INSERT INTO direcciones (pais,ciudad,calle,numero) VALUES (?,?,?,?) ";
            pstmt = connection.getConnection().prepareStatement(consulta, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, direccion.getPais());
            pstmt.setString(2, direccion.getCiudad());
            pstmt.setString(3, direccion.getCalle());
            pstmt.setInt(4, direccion.getNumero());
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    pstmt.close();
                    connection.closeConexion();
                    return id;
                }
            }
            pstmt.close();
            connection.closeConexion();
            return -1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return -1;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public  static boolean eliminar(Direccion direccion){
        ConectorDB connection;
        PreparedStatement pstmt;
        try {
            connection = new ConectorDB();
            String consulta = "DELETE FROM direcciones WHERE id = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, direccion.getId());
            int filasAfectadas = pstmt.executeUpdate();
            pstmt.close();
            connection.closeConexion();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
