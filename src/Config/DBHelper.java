package Config;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBHelper{

    //Configuracion de Server ya os la sabeis jeje
    private static final String SERVER = "localhost:1433";
    private static final String DATABASE_NAME = "bd_ventas1";
    private static final String USUARIO = "sa";
    private static final String PASSWORD = "Polar117#";


    private static final String URL = "jdbc:sqlserver://" + SERVER +
            ";databaseName=" + DATABASE_NAME +
            ";encrypt=true;trustServerCertificate=true;";

    private static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }

    // Ejecuta INSERT, UPDATE, DELETE en 1 línea
    public static int execute(String sql, Object... params) {
        try (Connection conn = getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            return ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println(" Error en Execute: " + e.getMessage());
            return -1;
        }
    }

    // Ejecuta INSERT y te regresa el ID (Identity) autogenerado
    public static int executeGetId(String sql, Object... params) {
        try (Connection conn = getConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println(" Error en ExecuteGetId: " + e.getMessage());
        }
        return -1;
    }

    // Ejecuta SELECT en 1 línea y regresa filas ordenadas en un Mapa
    public static List<Map<String, Object>> select(String sql, Object... params) {
        List<Map<String, Object>> resultados = new ArrayList<>();

        try (Connection conn = getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int totalColumnas = metaData.getColumnCount();

                while (rs.next()) {
                    Map<String, Object> fila = new HashMap<>();
                    for (int i = 1; i <= totalColumnas; i++) {
                        fila.put(metaData.getColumnLabel(i), rs.getObject(i));
                    }
                    resultados.add(fila);
                }
            }
        } catch (SQLException e) {
            System.err.println(" Error en Select: " + e.getMessage());
        }

        return resultados;
    }
}
