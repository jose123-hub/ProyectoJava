package Servicios;
import Modelos.DetalleCompra;
import java.util.ArrayList;
import java.util.List;

public class CarritoServicio {
    private static List<DetalleCompra> carrito = new ArrayList<>();
    public void agregarProducto(DetalleCompra nuevoDetalle) {
        for (DetalleCompra item : carrito) {
            if (item.getIdProducto() == nuevoDetalle.getIdProducto()) {
                item.setCantidad(item.getCantidad() + nuevoDetalle.getCantidad());
                return;
            }
        }
        carrito.add(nuevoDetalle);
    }
    public void eliminarProducto(int idProducto) {
        carrito.removeIf(item -> item.getIdProducto() == idProducto);
    }
    public List<DetalleCompra> getCarrito() {
        return carrito;
    }
    public void limpiarCarrito() {
        carrito.clear();
    }
    public double calcularTotal() {
        double total = 0;
        for (DetalleCompra item : carrito) {
            total += item.getCantidad() * item.getPrecioUnitario();
        }
        return total;
    }
}
