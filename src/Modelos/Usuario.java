package Modelos;

public class Usuario {
    private int id;
    private String nombre;
    private String rol;
    private String contrasena;
    private boolean activo;
    public Usuario() {};
    public Usuario(String nombre, String rol, String contrasena) {
        this.nombre = nombre;
        this.rol = rol;
        this.contrasena = contrasena;
    }
    public Usuario(int id, String nombre, String rol, String contrasena) {
        this.id = id;
        this.nombre = nombre;
        this.rol = rol;
        this.contrasena = contrasena;
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
    public String getRol() {
        return rol;
    }
    public void setRol(String rol) {
        this.rol = rol;
    }
    public String getContrasena() {
        return contrasena;
    }
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    public boolean isActivo() {
        return activo;
    }
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
