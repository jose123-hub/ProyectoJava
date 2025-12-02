package DAO;
import Modelos.DetalleCompra;
import java.util.List;

public interface DetalleCompraDAO {
    void registrarDetalle(int idCompra, DetalleCompra detalle);
    List<DetalleCompra> listarPorIdCompra(int idCompra);
}
