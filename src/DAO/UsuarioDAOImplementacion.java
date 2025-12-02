package DAO;
import Conexión.Conexión;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
                boolean estaActivo = rs.getBoolean("estado");
                if (!estaActivo) {
                    System.out.println("ACCESO DENEGADO: El usuario " + nombre + " está bloqueado.");
                    return null;
                }
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
  @Override
  public boolean cambiarEstadoUsuario(int idUsuario, boolean activo) {
        String sql = "UPDATE usuarios SET estado = ? WHERE id = ?";
        try (Connection conn = Conexión.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, activo);
            stmt.setInt(2, idUsuario);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios WHERE rol = 'Cliente'";
        try (Connection conn = Conexión.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNombre(rs.getString("nombre"));
                u.setContrasena(rs.getString("contrasena"));
                u.setRol(rs.getString("rol"));
                u.setActivo(rs.getBoolean("estado"));
                lista.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
