package Conexión;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexión {
    private static final String URL = "jdbc:mysql://localhost:3306/proyecto";
    private static final String USER = "root";
    private static final String PASS = "";
    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos:");
            return null;
        }
    }
}
