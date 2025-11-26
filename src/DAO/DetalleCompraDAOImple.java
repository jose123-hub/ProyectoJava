package DAO;
import Conexión.Conexión;
import Modelos.DetalleCompra;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DetalleCompraDAOImple implements DetalleCompraDAO {
    public void registrarDetalle(int idCompra, DetalleCompra det) {
        try (Connection conn = Conexión.conectar()) {
            String sql = "INSERT INTO detalle_compra(idCompra, idProducto, cantidad, precioUnitario) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idCompra);
            stmt.setInt(2, det.getIdProducto());
            stmt.setInt(3, det.getCantidad());
            stmt.setDouble(4, det.getPrecioUnitario());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
