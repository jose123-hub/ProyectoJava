package DAO;

import Conexión.Conexión;
import Modelos.ProductoTecnologico;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoTecDAOImple implements ProductoTecnologicoDAO {
    @Override
    public boolean agregar(ProductoTecnologico p) {
        String sql = "INSERT INTO producto (nombre, precio, cantidad) VALUES (?, ?, ?)";
        try (Connection conn = Conexión.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setDouble(2, p.getPrecio());
            ps.setInt(3, p.getCantidad());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error agregar: " + e.getMessage());
            return false;
        }
    }
    @Override
    public List<ProductoTecnologico> listar() {
        List<ProductoTecnologico> lista = new ArrayList<>();
        String sql = "SELECT * FROM producto";
        try (Connection conn = Conexión.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ProductoTecnologico p = new ProductoTecnologico(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getInt("cantidad")
                );
                lista.add(p);
            }
        } catch (Exception e) {
            System.out.println("Error listar: " + e.getMessage());
        }
        return lista;
    }
    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM producto WHERE id = ?";
        try (Connection conn = Conexión.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error eliminar: " + e.getMessage());
            return false;
        }
    }
    @Override
    public List<ProductoTecnologico> listarProductosDisponibles() {
        List<ProductoTecnologico> lista = new ArrayList<>();
        String sql = "SELECT * FROM producto WHERE cantidad > 0";
        try (Connection conn = Conexión.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ProductoTecnologico p = new ProductoTecnologico(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getInt("cantidad")
                );
                lista.add(p);
            }
        } catch (Exception e) {
            System.out.println("Error listar disponibles: " + e.getMessage());
        }
        return lista;
    }
    @Override
    public boolean actualizar(ProductoTecnologico p) {
        String sql = "UPDATE producto SET nombre = ?, precio = ?, cantidad = ? WHERE id = ?";
        try (Connection conn = Conexión.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setDouble(2, p.getPrecio());
            ps.setInt(3, p.getCantidad());
            ps.setInt(4, p.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error actualizar: " + e.getMessage());
            return false;
        }
    }
}
