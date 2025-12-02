package Servicios;
import DAO.*;
import Modelos.Compra;
import Modelos.DetalleCompra;
import Modelos.ProductoTecnologico;

import java.util.List;

public class CompraServicio {
    private CompraDAO compraDAO;
    private DetalleCompraDAO detalleDAO;
    private ProductoTecnologicoDAO productoDAO;
    public CompraServicio() {
        compraDAO = new CompraDAOImplementacion();
        detalleDAO = new DetalleCompraDAOImple();
        productoDAO = new ProductoTecDAOImple();
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
            productoDAO.restarStock(item.getIdProducto(), item.getCantidad());
        }
        return true;
    }
    public List<Compra> listarComprasPorUsuario(int idUsuario) {
        return compraDAO.listarComprasPorUsuario(idUsuario);
    }
    public List<ProductoTecnologico> listarProductos() {
        return productoDAO.listar();
    }
    public List<ProductoTecnologico> buscarPorNombre(String texto) {
        return productoDAO.buscarPorNombre(texto);
    }
}
