package Modelos;

public class ProductoTecnologico {
    private int id;
    private String nombre;
    private double precio;
    private int cantidad;
    private int descuento;
    private String imagen;
    public ProductoTecnologico() {};
    public ProductoTecnologico(int id, String nombre, double precio, int cantidad, int descuento, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.descuento = descuento;
        this.imagen = imagen;
    }
    public ProductoTecnologico(int id, String nombre, double precio, int cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
    }
    public ProductoTecnologico(int id, String nombre, double precio, int cantidad, int descuento) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.descuento = descuento;
    }
    public ProductoTecnologico(String nombre, double precio, int cantidad) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
    }
    public ProductoTecnologico(String nombre, double precio, int cantidad, int descuento) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.descuento = descuento;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public double getPrecio() {
        return precio;
    }
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    public int getDescuento() {
        return descuento;
    }
    public void setDescuento(int descuento) {
        this.descuento = descuento;
    }
    public double getPrecioFinal() {
        if (descuento <= 0) return precio;
        return precio - (precio * descuento / 100.0);
    }
    public String getImagen() {
        return imagen;
    }
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}

