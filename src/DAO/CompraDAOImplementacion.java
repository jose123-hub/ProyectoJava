package DAO;
import Conexión.Conexión;
import Modelos.Compra;
import Modelos.DetalleCompra;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompraDAOImplementacion implements CompraDAO {
    @Override
    public int registrarCompra(Compra compra) {
        int idGenerado = -1;
        try (Connection conn = Conexión.conectar()) {
            String sql = "INSERT INTO compras(idUsuario, fecha, total) VALUES (?, NOW(), ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, compra.getIdUsuario());
            stmt.setDouble(2, compra.getTotal());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next())
                idGenerado = keys.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idGenerado;
    }

    @Override
    public List<Compra> listarComprasPorUsuario(int idUsuario) {
        List<Compra> lista = new ArrayList<>();
        DetalleCompraDAOImple detalleDao = new DetalleCompraDAOImple();

        try (Connection conn = Conexión.conectar()) {
            String sql = "SELECT * FROM compras WHERE idUsuario = ? ORDER BY fecha DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Compra compra = new Compra();
                compra.setId(rs.getInt("idCompra"));
                compra.setIdUsuario(rs.getInt("idUsuario"));
                Timestamp ts = rs.getTimestamp("fecha");
                if (ts != null) {
                    compra.setFecha(ts.toLocalDateTime());
                }
                compra.setTotal(rs.getDouble("total"));
                List<DetalleCompra> detalles = detalleDao.listarPorIdCompra(compra.getId());
                compra.setDetalles(detalles);
                lista.add(compra);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}
