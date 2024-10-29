package com.example.ejerciciol.dao;

import com.example.ejerciciol.db.ConectorDB;
import com.example.ejerciciol.model.Aeropuerto;
import com.example.ejerciciol.model.AeropuertoPublico;
import com.example.ejerciciol.model.Direccion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class aeropuertoPublicoDao {
    public static AeropuertoPublico getAeropuerto(int id) {
        ConectorDB connection;
        AeropuertoPublico aeropuerto = null;
        try {
            connection = new ConectorDB();
            String consulta = "SELECT id,nombre,anio_inauguracion,capacidad,id_direccion,imagen,financiacion,num_trabajadores FROM aeropuertos,aeropuertos_publicos WHERE id=id_aeropuerto AND id = ?";
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
                Aeropuerto airport = new Aeropuerto(id_aeropuerto,nombre,anio_inauguracion,capacidad,direccion,imagen);
                BigDecimal financiacion = rs.getBigDecimal("financiacion");
                int num_trabajadores = rs.getInt("num_trabajadores");
                aeropuerto = new AeropuertoPublico(airport,financiacion,num_trabajadores);
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

    public static ObservableList<AeropuertoPublico> cargarListado() {
        ConectorDB connection;
        ObservableList<AeropuertoPublico> airportList = FXCollections.observableArrayList();
        try{
            connection = new ConectorDB();
            String consulta = "SELECT id,nombre,anio_inauguracion,capacidad,id_direccion,imagen,financiacion,num_trabajadores FROM aeropuertos,aeropuertos_publicos WHERE id=id_aeropuerto";
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
                BigDecimal financiacion = rs.getBigDecimal("financiacion");
                int num_trabajadores = rs.getInt("num_trabajadores");
                AeropuertoPublico airport = new AeropuertoPublico(aeropuerto,financiacion,num_trabajadores);
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

    public static boolean modificar(AeropuertoPublico aeropuerto, AeropuertoPublico aeropuertoNuevo) {
        ConectorDB connection;
        PreparedStatement pstmt;
        try {
            connection = new ConectorDB();
            String consulta = "UPDATE aeropuertos_publicos SET financiacion = ?,num_trabajadores = ? WHERE id_aeropuerto = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setBigDecimal(1, aeropuertoNuevo.getFinanciacion());
            pstmt.setInt(2, aeropuertoNuevo.getNum_trabajadores());
            pstmt.setInt(3, aeropuerto.getAeropuerto().getId());
            int filasAfectadas = pstmt.executeUpdate();
            System.out.println("Actualizada aeropuerto");
            pstmt.close();
            connection.closeConexion();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("hello");
            System.err.println(e.getMessage());
            return false;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public  static boolean insertar(AeropuertoPublico aeropuerto) {
        ConectorDB connection;
        PreparedStatement pstmt;

        try {
            connection = new ConectorDB();


            String consulta = "INSERT INTO aeropuertos_publicos (id_aeropuerto,financiacion,num_trabajadores) VALUES (?,?,?) ";
            pstmt = connection.getConnection().prepareStatement(consulta);

            pstmt.setInt(1, aeropuerto.getAeropuerto().getId());
            pstmt.setBigDecimal(2, aeropuerto.getFinanciacion());
            pstmt.setInt(3, aeropuerto.getNum_trabajadores());

            int filasAfectadas = pstmt.executeUpdate();
            System.out.println("Nueva entrada en aeropuertos_publicos");
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


    public  static boolean eliminar(AeropuertoPublico aeropuerto){
        ConectorDB connection;
        PreparedStatement pstmt;
        try {
            connection = new ConectorDB();
            String consulta = "DELETE FROM aeropuertos_publicos WHERE id_aeropuerto = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, aeropuerto.getAeropuerto().getId());
            int filasAfectadas = pstmt.executeUpdate();
            pstmt.close();
            connection.closeConexion();
            System.out.println("Eliminado con éxito");
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
