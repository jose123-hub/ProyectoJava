package DAO;
import Modelos.Usuario;
public interface UsuarioDAO {
    public abstract Usuario login(String nombre, String contrasena);
    public abstract boolean registrar(Usuario usuario);
}
