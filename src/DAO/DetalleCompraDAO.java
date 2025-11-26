package DAO;
import Modelos.DetalleCompra;

public interface DetalleCompraDAO {
    void registrarDetalle(int idCompra, DetalleCompra detalle);
}
