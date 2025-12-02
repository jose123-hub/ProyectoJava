package DAO;
import Conexión.Conexión;
import Modelos.ProductoTecnologico;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoTecDAOImple implements ProductoTecnologicoDAO {
        @Override
        public boolean agregar(ProductoTecnologico p) {
            String sql = "INSERT INTO producto (nombre, precio, cantidad, descuento, imagen) VALUES (?, ?, ?, ?, ?)";
            try (Connection conn = Conexión.conectar();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, p.getNombre());
                ps.setDouble(2, p.getPrecio());
                ps.setInt(3, p.getCantidad());
                ps.setInt(4, p.getDescuento());
                String img = (p.getImagen() != null && !p.getImagen().isEmpty()) ? p.getImagen() : "sin_imagen";
                ps.setString(5, img);
                return ps.executeUpdate() > 0;
            } catch (Exception e) {
                System.out.println("❌ Error agregar: " + e.getMessage());
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
                    ProductoTecnologico p = new ProductoTecnologico();
                    p.setId(rs.getInt("id"));
                    p.setNombre(rs.getString("nombre"));
                    p.setPrecio(rs.getDouble("precio"));
                    p.setCantidad(rs.getInt("cantidad"));

                    try { p.setDescuento(rs.getInt("descuento")); } catch (SQLException ex) { p.setDescuento(0); }

                    try {
                        String img = rs.getString("imagen");
                        p.setImagen((img != null) ? img : "sin_imagen");
                    } catch (SQLException ex) {
                        p.setImagen("sin_imagen");
                    }
                    lista.add(p);
                }
            } catch (Exception e) {
                System.out.println("❌ Error listar: " + e.getMessage());
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
                System.out.println("❌ Error eliminar: " + e.getMessage());
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
                    ProductoTecnologico p = new ProductoTecnologico();
                    p.setId(rs.getInt("id"));
                    p.setNombre(rs.getString("nombre"));
                    p.setPrecio(rs.getDouble("precio"));
                    p.setCantidad(rs.getInt("cantidad"));

                    try { p.setDescuento(rs.getInt("descuento")); } catch (SQLException ex) { p.setDescuento(0); }

                    try {
                        String img = rs.getString("imagen");
                        p.setImagen((img != null) ? img : "sin_imagen");
                    } catch (SQLException ex) {
                        p.setImagen("sin_imagen");
                    }
                    lista.add(p);
                }
            } catch (Exception e) {
                System.out.println("❌ Error listar disponibles: " + e.getMessage());
            }
            return lista;
        }
        @Override
        public boolean actualizar(ProductoTecnologico p) {
            String sql = "UPDATE producto SET nombre = ?, precio = ?, cantidad = ?, descuento = ?, imagen = ? WHERE id = ?";
            try (Connection conn = Conexión.conectar();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, p.getNombre());
                ps.setDouble(2, p.getPrecio());
                ps.setInt(3, p.getCantidad());
                ps.setInt(4, p.getDescuento());
                String img = (p.getImagen() != null && !p.getImagen().isEmpty()) ? p.getImagen() : "sin_imagen";
                ps.setString(5, img);
                ps.setInt(6, p.getId());

                return ps.executeUpdate() > 0;
            } catch (Exception e) {
                System.out.println("❌ Error actualizar: " + e.getMessage());
                return false;
            }
        }
        @Override
        public void restarStock(int idProducto, int cantidad) {
            String sql = "UPDATE producto SET cantidad = cantidad - ? WHERE id = ?";
            try (Connection conn = Conexión.conectar();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, cantidad);
                stmt.setInt(2, idProducto);
                stmt.executeUpdate();
            } catch (SQLException e) { e.printStackTrace(); }
        }
        @Override
        public List<ProductoTecnologico> buscarPorNombre(String texto) {
            List<ProductoTecnologico> lista = new ArrayList<>();
            String sql = "SELECT * FROM producto WHERE nombre LIKE ?";
            try (Connection conn = Conexión.conectar();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, "%" + texto + "%");
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    ProductoTecnologico p = new ProductoTecnologico();
                    p.setId(rs.getInt("id"));
                    p.setNombre(rs.getString("nombre"));
                    p.setPrecio(rs.getDouble("precio"));
                    p.setCantidad(rs.getInt("cantidad"));

                    try { p.setDescuento(rs.getInt("descuento")); } catch (SQLException ex) { p.setDescuento(0); }

                    try {
                        String img = rs.getString("imagen");
                        p.setImagen((img != null) ? img : "sin_imagen");
                    } catch (SQLException ex) {
                        p.setImagen("sin_imagen");
                    }
                    lista.add(p);
                }
            } catch (SQLException e) { e.printStackTrace(); }
            return lista;
        }
}
