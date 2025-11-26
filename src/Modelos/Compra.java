package Modelos;

import java.time.LocalDateTime;
import java.util.List;

public class Compra {
    private int id;
    private int idUsuario;
    private double total;
    private LocalDateTime fecha;
    private List<DetalleCompra> detalles;

    public Compra() {};

    public Compra(int id, int idUsuario, double total, LocalDateTime fecha) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.total = total;
        this.fecha = fecha;
    }

    public Compra(int idUsuario, double total) {
        this.idUsuario = idUsuario;
        this.total = total;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    public double getTotal() {
        return total;
    }
    public void setTotal(double total) {
        this.total = total;
    }
    public LocalDateTime getFecha() {
        return fecha;
    }
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    public List<DetalleCompra> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleCompra> detalles) { this.detalles = detalles; }
}
