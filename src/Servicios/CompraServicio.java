package Servicios;
import DAO.CompraDAO;
import DAO.CompraDAOImplementacion;
import DAO.DetalleCompraDAO;
import DAO.DetalleCompraDAOImple;
import Modelos.Compra;
import Modelos.DetalleCompra;

import java.util.List;

public class CompraServicio {
    private CompraDAO compraDAO;
    private DetalleCompraDAO detalleDAO;
    public CompraServicio() {
        compraDAO = new CompraDAOImplementacion();
        detalleDAO = new DetalleCompraDAOImple();
    }
    public boolean procesarCompra(int idUsuario, List<DetalleCompra> items) {
        double total = items.stream()
                .mapToDouble(i -> i.getCantidad() * i.getPrecioUnitario())
                .sum();
        Compra compra = new Compra(idUsuario, total);
        int idGenerado = compraDAO.registrarCompra(compra);
        if (idGenerado == -1) return false;
        for (DetalleCompra item : items) {
            detalleDAO.registrarDetalle(idGenerado, item);
        }
        return true;
    }
    public List<Compra> listarComprasPorUsuario(int idUsuario) {
        return compraDAO.listarComprasPorUsuario(idUsuario);
    }
}
