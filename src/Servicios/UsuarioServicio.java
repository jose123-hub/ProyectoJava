package Servicios;
import DAO.UsuarioDAO;
import DAO.UsuarioDAOImplementacion;
import Modelos.Usuario;

public class UsuarioServicio {
    private UsuarioDAO usuarioDAO;
    public UsuarioServicio() {
        this.usuarioDAO = new UsuarioDAOImplementacion();
    }
    public Usuario login(String nombre, String contrasena) {
        if (nombre == null || nombre.isEmpty()) {
            System.err.println("El nombre no puede estar vacío");
            return null;
        }
        if (contrasena == null || contrasena.isEmpty()) {
            System.err.println("La contraseña no puede estar vacía");
            return null;
        }
        return usuarioDAO.login(nombre, contrasena);
    }
    public boolean registrar(String nombre, String contrasena) {
        if (nombre.isEmpty() || contrasena.isEmpty()) {
            System.err.println("Datos incompletos");
            return false;
        }
        if(nombre.equalsIgnoreCase("admin")) {
            System.err.println("No se puede crear un usuario admin desde aquí");
            return false;
        }
        Usuario existingUser = usuarioDAO.login(nombre, contrasena);
        if (existingUser != null) {
            System.err.println("El usuario ya existe");
            return false;
        }
        Usuario u = new Usuario();
        u.setNombre(nombre);
        u.setRol("Cliente");
        u.setContrasena(contrasena);
        return usuarioDAO.registrar(u);
    }
}
