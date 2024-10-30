package com.example.ejerciciol.dao;

import com.example.ejerciciol.db.ConectorDB;
import com.example.ejerciciol.model.Aeropuerto;
import com.example.ejerciciol.model.AeropuertoPrivado;
import com.example.ejerciciol.model.Direccion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileNotFoundException;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class aeropuertoPrivadoDao {

    public static AeropuertoPrivado getAeropuerto(int id) {
        ConectorDB connection;
        AeropuertoPrivado aeropuerto = null;
        try {
            connection = new ConectorDB();
            String consulta = "SELECT id,nombre,anio_inauguracion,capacidad,id_direccion,imagen,numero_socios FROM aeropuertos,aeropuertos_privados WHERE id=id_aeropuerto AND id = ?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id_aeropuerto = rs.getInt("id");
                String nombre = rs.getString("nombre");
                int anio_inauguracion = rs.getInt("anio_inauguracion");
                int capacidad = rs.getInt("capacidad");
                int id_direccion = rs.getInt("id_direccion");
                Direccion direccion = direccionDao.getDireccion(id_direccion);
                Blob imagen = rs.getBlob("imagen");
                Aeropuerto airport = new Aeropuerto(id,nombre,anio_inauguracion,capacidad,direccion,imagen);
                int numero_socios = rs.getInt("numero_socios");
                aeropuerto = new AeropuertoPrivado(airport,numero_socios);
            }
            rs.close();
            connection.closeConexion();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return aeropuerto;
    }

    public static ObservableList<AeropuertoPrivado> cargarListado() {
        ConectorDB connection;
        ObservableList<AeropuertoPrivado> airportList = FXCollections.observableArrayList();
        try{
            connection = new ConectorDB();
            String consulta = "SELECT id,nombre,anio_inauguracion,capacidad,id_direccion,imagen,numero_socios FROM aeropuertos,aeropuertos_privados WHERE id=id_aeropuerto";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                int anio_inauguracion = rs.getInt("anio_inauguracion");
                int capacidad = rs.getInt("capacidad");
                int id_direccion = rs.getInt("id_direccion");
                Direccion direccion = direccionDao.getDireccion(id_direccion);
                Blob imagen = rs.getBlob("imagen");
                Aeropuerto aeropuerto = new Aeropuerto(id,nombre,anio_inauguracion,capacidad,direccion,imagen);
                int numero_socios = rs.getInt("numero_socios");
                AeropuertoPrivado airport = new AeropuertoPrivado(aeropuerto,numero_socios);
                airportList.add(airport);
            }
            rs.close();
            connection.closeConexion();
        }catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return airportList;
    }

    public static boolean modificar(AeropuertoPrivado aeropuerto, AeropuertoPrivado aeropuertoNuevo) {
        ConectorDB connection;
        PreparedStatement pstmt;
        try {
            connection = new ConectorDB();
            String consulta = "UPDATE aeropuertos_privados SET numero_socios = ? WHERE id_aeropuerto = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);

            pstmt.setInt(1, aeropuertoNuevo.getNumero_socios());
            pstmt.setInt(2, aeropuerto.getAeropuerto().getId());

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

    public static boolean insertar(AeropuertoPrivado aeropuerto) {
        ConectorDB connection;
        PreparedStatement pstmt;

        try {
            connection = new ConectorDB();

            String consulta = "INSERT INTO aeropuertos_privados (id_aeropuerto,numero_socios) VALUES (?,?) ";
            pstmt = connection.getConnection().prepareStatement(consulta);

            pstmt.setInt(1, aeropuerto.getAeropuerto().getId());
            pstmt.setInt(2, aeropuerto.getNumero_socios());

            int filasAfectadas = pstmt.executeUpdate();

            pstmt.close();
            connection.closeConexion();
            return (filasAfectadas > 0);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public  static boolean eliminar(AeropuertoPrivado aeropuerto){

        ConectorDB connection;
        PreparedStatement pstmt;
        try {
            connection = new ConectorDB();
            String consulta = "DELETE FROM aeropuertos_privados WHERE (id_aeropuerto = ?)";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, aeropuerto.getAeropuerto().getId());
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
