package com.example.ejerciciol.dao;

import com.example.ejerciciol.db.ConectorDB;
import com.example.ejerciciol.model.Aeropuerto;
import com.example.ejerciciol.model.Avion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class avionDao {

    public static com.example.ejerciciol.model.Avion getAvion(int id) {
        ConectorDB connection;
        Avion avion = null;
        try {
            connection = new ConectorDB();
            String consulta = "SELECT id,modelo,numero_asientos,velocidad_maxima,activado,id_aeropuerto FROM aviones WHERE id = ?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id_avion = rs.getInt("id");
                String modelo = rs.getString("modelo");
                int numero_asientos = rs.getInt("numero_asientos");
                int velocidad_maxima = rs.getInt("velocidad_maxima");
                boolean activado = rs.getBoolean("activado");
                int id_aeropuerto = rs.getInt("id_aeropuerto");
                Aeropuerto aeropuerto = aeropuertoDao.getAeropuerto(id_aeropuerto);
                avion = new Avion(id_avion,modelo,numero_asientos,velocidad_maxima,activado,aeropuerto);
            }
            rs.close();
            connection.closeConexion();
        } catch (SQLException | FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
        return avion;
    }

    public static ObservableList<Avion> cargarListado(Aeropuerto aeropuerto) {
        ConectorDB connection;
        ObservableList<Avion> airplaneList = FXCollections.observableArrayList();
        try{
            connection = new ConectorDB();
            String consulta = "SELECT id,modelo,numero_asientos,velocidad_maxima,activado,id_aeropuerto FROM aviones WHERE id_aeropuerto = ?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1,aeropuerto.getId());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String modelo = rs.getString("modelo");
                int numero_asientos = rs.getInt("numero_asientos");
                int velocidad_maxima = rs.getInt("velocidad_maxima");
                boolean activado = rs.getBoolean("activado");
                int id_aeropuerto = rs.getInt("id_aeropuerto");
                Aeropuerto aeropuerto_db = aeropuertoDao.getAeropuerto(id_aeropuerto);
                Avion avion = new Avion(id,modelo,numero_asientos,velocidad_maxima,activado,aeropuerto_db);
                airplaneList.add(avion);
            }
            rs.close();
            connection.closeConexion();
        }catch (SQLException | FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
        return airplaneList;
    }

    public static ObservableList<Avion> cargarListado() {
        ConectorDB connection;
        ObservableList<Avion> airplaneList = FXCollections.observableArrayList();
        try{
            connection = new ConectorDB();
            String consulta = "SELECT id,modelo,numero_asientos,velocidad_maxima,activado,id_aeropuerto FROM aviones";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String modelo = rs.getString("modelo");
                int numero_asientos = rs.getInt("numero_asientos");
                int velocidad_maxima = rs.getInt("velocidad_maxima");
                boolean activado = rs.getBoolean("activado");
                int id_aeropuerto = rs.getInt("id_aeropuerto");
                Aeropuerto aeropuerto = aeropuertoDao.getAeropuerto(id_aeropuerto);
                Avion avion = new Avion(id,modelo,numero_asientos,velocidad_maxima,activado,aeropuerto);
                airplaneList.add(avion);
            }
            rs.close();
            connection.closeConexion();
        }catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return airplaneList;
    }

    public static boolean modificar(Avion avion, Avion avionNuevo) {
        ConectorDB connection;
        PreparedStatement pstmt;
        try {
            connection = new ConectorDB();
            String consulta = "UPDATE aviones SET modelo = ?,numero_asientos = ?,velocidad_maxima = ?,activado = ?,id_aeropuerto = ? WHERE id = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setString(1, avionNuevo.getModelo());
            pstmt.setInt(2, avionNuevo.getNumero_asientos());
            pstmt.setInt(3, avionNuevo.getVelocidad_maxima());
            pstmt.setBoolean(4, avionNuevo.isActivado());
            pstmt.setInt(5, avionNuevo.getAeropuerto().getId());
            pstmt.setInt(6, avion.getId());
            int filasAfectadas = pstmt.executeUpdate();
            System.out.println("Actualizada avion");
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

    public  static int insertar(Avion avion) {
        ConectorDB connection;
        PreparedStatement pstmt;
        try {
            connection = new ConectorDB();
            // INSERT INTO `DNI`.`dni` (`dni`) VALUES ('el nuevo');
            String consulta = "INSERT INTO aviones (modelo,numero_asientos,velocidad_maxima,activado,id_aeropuerto) VALUES (?,?,?,?,?) ";
            pstmt = connection.getConnection().prepareStatement(consulta, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, avion.getModelo());
            pstmt.setInt(2, avion.getNumero_asientos());
            pstmt.setInt(3, avion.getVelocidad_maxima());
            pstmt.setBoolean(4, avion.isActivado());
            pstmt.setInt(5, avion.getAeropuerto().getId());
            int filasAfectadas = pstmt.executeUpdate();
            System.out.println("Nueva entrada en avion");
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

    public  static boolean eliminar(Avion avion){
        ConectorDB connection;
        PreparedStatement pstmt;
        try {
            connection = new ConectorDB();
            String consulta = "DELETE FROM aviones WHERE id = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, avion.getId());
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

    public  static boolean eliminarPorAeropuerto(Aeropuerto aeropuerto){
        ConectorDB connection;
        PreparedStatement pstmt;
        try {
            connection = new ConectorDB();
            String consulta = "DELETE FROM aviones WHERE id_aeropuerto = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, aeropuerto.getId());
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
