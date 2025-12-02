package DAO;
import Modelos.Usuario;
import java.util.List;

public interface UsuarioDAO {
    Usuario login(String nombre, String contrasena);
    boolean registrar(Usuario usuario);
    boolean cambiarEstadoUsuario(int idUsuario, boolean activo);
    List<Usuario> listarTodos();
}
