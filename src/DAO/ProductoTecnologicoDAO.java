package DAO;
import Modelos.ProductoTecnologico;
import java.util.List;

public interface ProductoTecnologicoDAO {
    boolean agregar(ProductoTecnologico p);
    List<ProductoTecnologico> listar();
    boolean eliminar(int id);
    List<ProductoTecnologico> listarProductosDisponibles();

    boolean actualizar(ProductoTecnologico p);
}
