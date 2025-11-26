package DAO;
import Modelos.Compra;
import java.util.List;

public interface CompraDAO {
    int registrarCompra(Compra compra);
    List<Compra> listarComprasPorUsuario(int idUsuario);
}
