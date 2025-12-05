package DAO;
import Conexión.Conexión;
import Modelos.DetalleCompra;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DetalleCompraDAOImple implements DetalleCompraDAO {
    @Override
    public void registrarDetalle(int idCompra, DetalleCompra det) {
        String sql = "INSERT INTO detalle_compra(idCompra, idProducto, cantidad, precioUnitario) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexión.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCompra);
            stmt.setInt(2, det.getIdProducto());
            stmt.setInt(3, det.getCantidad());
            stmt.setDouble(4, det.getPrecioUnitario());
            stmt.executeUpdate();
        } catch (Exception e) {
            System.err.println("❌ ERROR AL REGISTRAR DETALLE: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<DetalleCompra> listarPorIdCompra(int idCompra) {
        List<DetalleCompra> lista = new ArrayList<>();
        String sql = "SELECT d.idProducto, d.cantidad, d.precioUnitario, p.nombre " +
                "FROM detalle_compra d " +
                "JOIN producto p ON d.idProducto = p.id " +
                "WHERE d.idCompra = ?";

        try (Connection conn = Conexión.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCompra);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                DetalleCompra d = new DetalleCompra();
                d.setIdCompra(idCompra);
                d.setIdProducto(rs.getInt("idProducto"));
                d.setCantidad(rs.getInt("cantidad"));
                d.setPrecioUnitario(rs.getDouble("precioUnitario"));
                d.setNombreProducto(rs.getString("nombre"));
                lista.add(d);
            }
            System.out.println("🔍 Compra ID " + idCompra + ": Se encontraron " + lista.size() + " productos.");
        } catch (Exception e) {
            System.err.println("❌ ERROR AL LEER DETALLES: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }
}
