package Servicios;
import DAO.ProductoTecnologicoDAO;
import DAO.ProductoTecDAOImple;
import Modelos.ProductoTecnologico;
import java.util.List;

public class ProductoServicio {
    private ProductoTecnologicoDAO productoDAO;
    public ProductoServicio() {
        this.productoDAO = new ProductoTecDAOImple();
    }
    public boolean agregarProducto(String nombre, double precio, int stock, int descuento, String rutaImagen) {
        if (nombre == null || nombre.trim().isEmpty()) return false;
        if (precio <= 0) return false;
        if (stock < 0) return false;
        if (descuento < 0 || descuento > 100) return false;

        ProductoTecnologico prod = new ProductoTecnologico(0, nombre, precio, stock, descuento, rutaImagen);
        return productoDAO.agregar(prod);
    }
    public List<ProductoTecnologico> listarProductos() {
        return productoDAO.listar();
    }
    public boolean eliminarProducto(int id) {
        return productoDAO.eliminar(id);
    }
    public boolean actualizarProducto(int id, String nombre, double precio, int stock, int descuento, String rutaImagen) {
        ProductoTecnologico prod = new ProductoTecnologico(id, nombre, precio, stock, descuento, rutaImagen);
        return productoDAO.actualizar(prod);
    }
    public boolean agregarProducto(ProductoTecnologico p) {
        if (p.getNombre() == null || p.getNombre().isEmpty()) return false;
        if (p.getPrecio() <= 0) return false;
        if (p.getCantidad() < 0) return false;
        return productoDAO.agregar(p);
    }
    public List<ProductoTecnologico> buscarPorNombre(String texto) {
        return productoDAO.buscarPorNombre(texto);
    }
}