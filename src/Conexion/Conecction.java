package Conexion;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Conecction {



    private static final String SERVIDOR = "localhost";
    private static final String PUERTO = "1433";
    private static final String BASE_DATOS = "bd_ventas1"; //Aqui va neim de la bd
    private static final String USUARIO = "sa";
    private static final String PASSWORD = "Polar117#";


    private static final String URL = String.format(
            "jdbc:sqlserver://%s:%s;databaseName=%s;encrypt=true;trustServerCertificate=true;",
            SERVIDOR, PUERTO, BASE_DATOS
    );


    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }


    //aqui solo orobamod si ta bien la conexion a data base jeje
    public static void main(String[] args) {
        try (Connection conn = getConexion()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("¡Conexión exitosa a SQL Server!");
            }
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos:");
            e.printStackTrace();
        }
    }
}



