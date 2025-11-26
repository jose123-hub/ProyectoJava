package DAO;
import Conexión.Conexión;
import Modelos.Compra;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompraDAOImplementacion implements CompraDAO {
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
        try (Connection conn = Conexión.conectar()) {
            String sql = "SELECT * FROM compras WHERE idUsuario = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Compra compra = new Compra();
                compra.setId(rs.getInt("id"));
                compra.setIdUsuario(rs.getInt("idUsuario"));
                compra.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                compra.setTotal(rs.getDouble("total"));
                lista.add(compra);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}
