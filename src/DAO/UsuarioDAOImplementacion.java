package DAO;
import Conexión.Conexión;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import Modelos.Usuario;

public class UsuarioDAOImplementacion implements UsuarioDAO {
    @Override
    public Usuario login(String nombre, String contrasena) {
        Usuario usuario = null;
        try (Connection conn = Conexión.conectar()) {
            String sql = "SELECT * FROM usuarios WHERE nombre = ? AND contrasena = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nombre);
            stmt.setString(2, contrasena);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                usuario = new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("rol"),
                        rs.getString("contrasena")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usuario;
    }
@Override
public boolean registrar(Usuario usuario) {
    try (Connection conn = Conexión.conectar()) {
        String sql = "INSERT INTO usuarios(nombre, rol, contrasena) VALUES (?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, usuario.getNombre());
        stmt.setString(2, "Cliente");
        stmt.setString(3, usuario.getContrasena());
        return stmt.executeUpdate() > 0;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
  }
}
