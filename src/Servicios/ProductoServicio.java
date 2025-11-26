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
    public boolean agregarProducto(String nombre, double precio, int cantidad) {
        if (nombre.isEmpty() || precio <= 0 || cantidad < 0) {
            System.err.println("Datos inválidos para producto");
            return false;
        }
        ProductoTecnologico p = new ProductoTecnologico();
        p.setNombre(nombre);
        p.setPrecio(precio);
        p.setCantidad(cantidad);
        return productoDAO.agregar(p);
    }
    public List<ProductoTecnologico> listarProductos() {
        return productoDAO.listar();
    }
    public boolean eliminarProducto(int id) {
        return productoDAO.eliminar(id);
    }
    public boolean actualizarProducto(int id, String nombre, double precio, int cantidad) {
        ProductoTecnologico p = new ProductoTecnologico();
        p.setId(id);
        p.setNombre(nombre);
        p.setPrecio(precio);
        p.setCantidad(cantidad);
        return productoDAO.actualizar(p);
    }
    public boolean agregarProducto(ProductoTecnologico p) {
        if (p.getNombre() == null || p.getNombre().isEmpty()) return false;
        if (p.getPrecio() <= 0) return false;
        if (p.getCantidad() < 0) return false;
        return productoDAO.agregar(p);
    }
}