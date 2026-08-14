/*package BD1;

import Models.Tarjeta;
import Models.Usuario;
import Utils.Validacion;
import Utils.Validaciones;
import dao.UsuarioDAO;
import dao.TarjetaDao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class bd1 {
    static Scanner scan = new Scanner(System.in);
    static UsuarioDAO dao = new UsuarioDAO();
    static TarjetaDao tarjed = new TarjetaDao();


    //Flujo main
    public static void main(String[] args) {
//Menu de acciones user o tarjetas
        int opcion_menu = -1;
        System.out.println("=====Bienvendido======");

        while (opcion_menu != 0) {
            System.out.println("1. Gestion de Usuario");
            System.out.println("2. Gestion de Tarjetas");
            System.out.println("0. Salir");
            opcion_menu = Validacion.Scanner_enteros(scan,"");

            switch (opcion_menu) {
                case 1:
                    int opcion = 0;
                    do {
                        menu();
                        try {
                            opcion = Integer.parseInt(scan.nextLine());

                            switch (opcion) {
                                case 1:
                                    mostrarUsuario();
                                    break;
                                case 2:
                                    registrarUsuario();
                                    break;
                                case 3:
                                    buscarUsuario();
                                    break;
                                case 4:
                                    editarUsuario();
                                    break;
                                case 5:
                                    eliminarUsuario();
                                    break;
                                case 6:
                                    System.out.println("Saliendo");

                                    break;

                                default:
                                    System.out.println("\nOpción inválida.");
                            }
                        } catch (Exception e) {
                            System.out.println("\nDebe ingresar un número.");
                        }
                        if (opcion != 6) {
                            System.out.println("\nPresione ENTER para continuar...");
                            scan.nextLine();
                        }
                    } while (opcion != 6);
                    break;
                case 2 :
                    int opcion_tarje = -1;
                    do {
                        menu_tarjeta();
                        opcion_tarje = Validacion.Scanner_enteros(scan, "");
                        switch (opcion_tarje) {
                            case 1:
                                mostrar_tarjetas();
                                break;
                            case 2:
                                registrarTarjeta();
                                break;
                            case 3:
                                mostrarTarjetaIndividual();
                                break;
                            case 4:
                                gestionarEstadoTarjetaPorUsuario();
                                break;
                            case 5:
                                System.out.println("Ingrese el id del usuario a utilizar");
                                mostrarUsuario();
                                int idUsuario = Validacion.Scanner_enteros(scan, "Ingrese el ID del usuario: ");
                                Usuario usuario = dao.buscar(idUsuario);

                                if (usuario == null) {
                                    System.out.println("El usuario ingresado no existe.");
                                    break;
                                }

                                if (usuario.isActivo()) {

                                    int intentosPass = 0;
                                    boolean loginCorrecto = false;

                                    while (intentosPass < 3) {
                                        System.out.println("Ingrese la contraseña del usuario para continuar:");
                                        String password = Validacion.scan_string_vacio(scan);

                                        if (password.equals(usuario.getPassword())) {
                                            loginCorrecto = true;
                                            break;
                                        } else {
                                            intentosPass++;
                                            System.out.println("Contraseña incorrecta. Intentos restantes: " + (3 - intentosPass));
                                        }
                                    }

                                    if (!loginCorrecto) {
                                        System.out.println("BLOQUEO] Demasiados intentos fallidos. Su cuenta ha sido desactivada.");
                                        System.out.println("Puede volver a activarla después.");
                                        usuario.setActivo(false);
                                        dao.cambiarEstado(usuario.getId_user(), false);
                                        break;
                                    }


                                    System.out.println("Bienvenido " + usuario.getNombre() + " " + usuario.getApellido());

                                    ArrayList<Tarjeta> tarjetas = tarjed.buscarPorUsuario(usuario.getId_user());

                                    if (tarjetas.isEmpty()) {
                                        System.out.println("El usuario no tiene tarjetas asociadas.");
                                        break;
                                    }

                                    System.out.println("\nSeleccione la tarjeta que desea utilizar:");
                                    for (Tarjeta tarjeta : tarjetas) {
                                        String tipoTexto = tarjeta.getTipo().equalsIgnoreCase("D") ? "Débito" : "Crédito";
                                        String estadoTexto = tarjeta.isActivo() ? "ACTIVA" : "BLOQUEADA";

                                        if (tarjeta.getTipo().equalsIgnoreCase("D")) {
                                            System.out.printf("ID: %d | Número: %s | Tipo: %s | Saldo: $%.2f | Estado: %s%n",
                                                    tarjeta.getId_tarjeta(),
                                                    tarjeta.getNumero(),
                                                    tipoTexto,
                                                    tarjeta.getSaldo(),
                                                    estadoTexto);
                                        } else {
                                            System.out.printf("ID: %d | Número: %s | Tipo: %s | Saldo (Deuda): $%.2f | Crédito Disp.: $%.2f | Estado: %s%n",
                                                    tarjeta.getId_tarjeta(),
                                                    tarjeta.getNumero(),
                                                    tipoTexto,
                                                    tarjeta.getSaldo(),
                                                    tarjeta.getCredito(),
                                                    estadoTexto);
                                        }
                                    }

                                    int tarjetaid = Validacion.Scanner_enteros(scan, "Ingrese el ID de la tarjeta: ");


                                    Tarjeta tarjetaElegida = null;
                                    for (Tarjeta t : tarjetas) {
                                        if (t.getId_tarjeta() == tarjetaid) {
                                            tarjetaElegida = t;
                                            break;
                                        }
                                    }

                                    if (tarjetaElegida == null) {
                                        System.out.println("La tarjeta seleccionada no existe o no pertenece a este usuario.");
                                        break;
                                    }


                                    if (!tarjetaElegida.isActivo()) {
                                        System.out.println("La tarjeta seleccionada está BLOQUEADA/INACTIVA. No se puede utilizar.");
                                        break;
                                    }


                                    int intentosPIN = 0;
                                    boolean pinCorrecto = false;

                                    while (intentosPIN < 3) {
                                        System.out.print("\nIngrese el PIN de la tarjeta (cvv): ");
                                        String pinIngresado = Validacion.scan_string_vacio(scan);


                                        if (pinIngresado.equals(tarjetaElegida.getCvv())) {
                                            pinCorrecto = true;
                                            break;
                                        } else {
                                            intentosPIN++;
                                            System.out.println("PIN incorrecto. Intentos restantes: " + (3 - intentosPIN));
                                        }
                                        if (intentosPIN > 3) {
                                            System.out.println("[ALERTA] Ha superado el límite de 3 intentos de PIN.");
                                            System.out.println("La tarjeta ha sido BLOQUEADA por razones de seguridad.");

                                            tarjetaElegida.setActivo(false);
                                            tarjed.cambiarEstadoTarjeta(tarjetaElegida.getId_tarjeta(), false);
                                        }
                                    }

                                    if (pinCorrecto) {

                                        boolean menuDebito = true;


                                        System.out.println("\n¡PIN Verificado con éxito!");
                                        System.out.println("Tarjeta #" + tarjetaElegida.getNumero() + " lista para realizar operaciones.");
                                        if (tarjetaElegida.getTipo().equalsIgnoreCase("D")) {
                                            do {


                                                System.out.println("--- MENÚ TARJETA DE DÉBITO ---");
                                                System.out.println("1. Retirar efectivo");
                                                System.out.println("2. Depositar saldo");
                                                System.out.println("3. Consultar saldo");
                                                System.out.println("4: Realizar transferencia");
                                                System.out.println("5 Realizar una compra");
                                                System.out.println("0 Salir");
                                                int opDebito = Validacion.Scanner_enteros(scan, "Seleccione una opción: ");

                                                switch (opDebito) {
                                                    case 1:
                                                        double retiro;
                                                        do {
                                                            System.out.print("Monto a retirar: ");
                                                            retiro = Validacion.Scanner_doubles(scan, "");

                                                            if (retiro <= 0) {
                                                                System.out.println("[ERROR] El monto a retirar debe ser mayor a $0.00");
                                                            }
                                                        } while (retiro <= 0);
                                                        if (retiro <= tarjetaElegida.getSaldo()) {
                                                            tarjetaElegida.setSaldo(tarjetaElegida.getSaldo() - retiro);
                                                            tarjed.actualizarTarje(tarjetaElegida);
                                                            System.out.println("Retiro exitoso. Nuevo saldo: $" + tarjetaElegida.getSaldo());
                                                        } else {
                                                            System.out.println("[ERROR] Saldo insuficiente.");
                                                        }
                                                        break;
                                                    case 2:
                                                        double deposito ;
                                                        do {
                                                            System.out.print("Monto a depositar: ");
                                                            deposito = Validacion.Scanner_doubles(scan, "");
                                                            if (deposito<=0){
                                                                System.out.println("Errror no se pueden hacer depositos con numeros negativos");
                                                            }
                                                        }while (deposito<=0);
                                                        tarjetaElegida.setSaldo(tarjetaElegida.getSaldo() + deposito);
                                                        tarjed.actualizarTarje(tarjetaElegida);
                                                        System.out.println("Depósito exitoso. Nuevo saldo: $" + tarjetaElegida.getSaldo());
                                                        break;
                                                    case 3:
                                                        System.out.println("Saldo disponible: $" + tarjetaElegida.getSaldo());
                                                        break;
                                                    case 4:
                                                        System.out.println("--- TRANSFERENCIA ENTRE TARJETAS ---");
                                                        System.out.println("Ingrese la clabe interbancaria de la tarjeta destino:");
                                                        String clabeInterdesti = Validacion.scan_string_vacio(scan);

                                                        if (clabeInterdesti.equals(tarjetaElegida.getClabe())) {
                                                            System.out.println("[ERROR] No puedes transferir a la misma tarjeta.");
                                                            break;
                                                        }

                                                        //crear la tarjeta
                                                        Tarjeta tarjeDestino = tarjed.buscarClabe(clabeInterdesti);

                                                        if (tarjeDestino == null) {
                                                            System.out.println("La tarjeta con esta clabe interbancaria no existe");
                                                            break;
                                                        }

                                                        if (!tarjeDestino.isActivo()) {
                                                            System.out.println("La tarjeta de destino se encuentra desactivada asi que no se puede realizar la transferencia");
                                                            break;
                                                        }
                                                        //Aca ya es cuando todo lo demas es correcto
                                                        double MontoTrans;
                                                        do {
                                                            System.out.println("Ingrese el monto a transferir:");
                                                            MontoTrans = Validacion.Scanner_doubles(scan, "");

                                                            if (MontoTrans <= 0) {
                                                                System.out.println("El monto de transferencia debe ser mayor a 0");
                                                            }
                                                        } while (MontoTrans <= 0);

                                                        if (MontoTrans > tarjetaElegida.getSaldo()) {
                                                            System.out.println("No tienes los fondos suficientes para realizar esta operación ");
                                                            break;
                                                        }
                                                        if (tarjeDestino.getTipo().equalsIgnoreCase("C")){
                                                            tarjeDestino.setCredito(tarjeDestino.getCredito() + MontoTrans);
                                                        }
                                                        tarjeDestino.setSaldo(tarjeDestino.getSaldo() - MontoTrans);
                                                        tarjetaElegida.setSaldo(tarjetaElegida.getSaldo() - MontoTrans);
                                                        tarjed.actualizarTarje(tarjetaElegida);
                                                        tarjed.actualizarTarje(tarjeDestino);


                                                        break;
                                                    case 5:
                                                        double compra;
                                                        do {
                                                            System.out.println("Ingrese el monto de la compra: ");
                                                            compra = Validacion.Scanner_doubles(scan,"");

                                                            if (compra <= 0) {
                                                                System.out.println("[ERROR] El monto de la compra debe ser mayor a $0.00");
                                                            }
                                                        }while (compra <= 0);

                                                        if (tarjetaElegida.getSaldo() >= compra) {
                                                            tarjetaElegida.setSaldo(tarjetaElegida.getSaldo() - compra);
                                                            tarjed.actualizarTarje(tarjetaElegida);
                                                            System.out.println("Compra realizada con éxito. Nuevo saldo: $" + tarjetaElegida.getSaldo());
                                                        } else {
                                                            System.out.println("[ERROR] Saldo insuficiente para realizar la compra.");
                                                        }
                                                        break;
                                                    case 0:
                                                        System.out.println("Saliendo...");
                                                        menuDebito = false;
                                                    default:
                                                        System.out.println("Opción no válida.");
                                                        break;
                                                }
                                            } while (menuDebito);
                                        }
                                        // CRÉDITO
                                        else if (tarjetaElegida.getTipo().equalsIgnoreCase("C")) {
                                            boolean menuCredi = true;
                                            do {


                                                System.out.println("\n--- MENÚ TARJETA DE CRÉDITO ---");
                                                System.out.println("1. Realizar una compra");
                                                System.out.println("2. Pagar tarjeta (Abonar a la deuda)");
                                                System.out.println("3. Consultar crédito y deuda");
                                                System.out.println("0. Salir");
                                                int opCredito = Validacion.Scanner_enteros(scan, "Seleccione una opción: ");

                                                switch (opCredito) {
                                                    case 1:
                                                        double compra ;
                                                        do {
                                                            System.out.print("Valor de la compra: ");
                                                            compra = Validacion.Scanner_doubles(scan, "");

                                                            if (compra<= 0){
                                                                System.out.println("No se puede comprar con un valor menor o igual a 0");
                                                            }
                                                        } while (compra <= 0);
                                                        if (compra <= tarjetaElegida.getCredito()) {
                                                            tarjetaElegida.setCredito(tarjetaElegida.getCredito() - compra);
                                                            tarjetaElegida.setSaldo(tarjetaElegida.getSaldo() + compra);
                                                            tarjed.actualizarTarje(tarjetaElegida);
                                                            System.out.println("Compra autorizada. Crédito disponible: $" + tarjetaElegida.getCredito());
                                                        } else {
                                                            System.out.println("[ERROR] Excede el límite de crédito disponible.");
                                                        }
                                                        break;
                                                    case 2:
                                                        if (tarjetaElegida.getSaldo() <= 0) {
                                                            System.out.println("No tienes deuda pendiente en esta tarjeta.");
                                                            break;
                                                        }
                                                        double pagotarje;
                                                        do {
                                                            System.out.println("Su credito actual es de "+ tarjetaElegida.getSaldo());
                                                            System.out.println("El saldo a pagar es de "+tarjetaElegida.getSaldo() );
                                                            System.out.print("Monto a pagar a la tarjeta: ");
                                                            pagotarje = Validacion.Scanner_doubles(scan,"");

                                                            if (pagotarje<=0){
                                                                System.out.println("Error el monto no puede ser negativo intentalo de nuevo");
                                                            }
                                                        } while (pagotarje<=0);


                                                        tarjetaElegida.setSaldo(tarjetaElegida.getSaldo() - pagotarje);
                                                        tarjetaElegida.setCredito(tarjetaElegida.getCredito() + pagotarje);
                                                        tarjed.actualizarTarje(tarjetaElegida);


                                                        break;
                                                    case 3:
                                                        System.out.println("\n--- INFORMACIÓN DE LA TARJETA ---");
                                                        System.out.println("Número de tarjeta: " + tarjetaElegida.getNumero());
                                                        System.out.println("Cuenta clabe: "+tarjetaElegida.getClabe());
                                                        System.out.println("Fecha de expiración: "+tarjetaElegida.getFecha_exp());
                                                        System.out.println("CVV: "+tarjetaElegida.getCvv());
                                                        System.out.println("Crédito disponible: $" + tarjetaElegida.getCredito());
                                                        System.out.println("Deuda actual: $" + tarjetaElegida.getSaldo());
                                                        break;
                                                    case 0:
                                                        System.out.println("Saliendo...");
                                                        menuCredi=false;
                                                        break;
                                                    default:
                                                        System.out.println("Opción no válida.");
                                                        break;
                                                }
                                            } while (menuCredi);
                                        }






                                    } else {
                                        System.out.println("\n[ALERTA] Ha superado el límite de 3 intentos de PIN.");
                                        System.out.println("La tarjeta ha sido BLOQUEADA por razones de seguridad.");

                                        tarjetaElegida.setActivo(false);
                                        tarjed.actualizarTarje(tarjetaElegida);
                                    }

                                } else {
                                    System.out.println("Usuario inactivo, no se puede utilizar.");
                                }
                                break;

                            case 0:
                                System.out.println("Volviendo al menú principal...");
                                break;
                            default:
                                System.out.println("Opcion Invalida");
                        }
                        if (opcion_tarje!= 0) {System.out.println("\nPresione ENTER para continuar...");
                            scan.nextLine();                 }
                    } while (opcion_tarje!=0 );

                    break;

                case 0:
                    System.out.println("Saliendo del sistema");


                default:
                    System.out.println("Opcion invalida intente de nuevo");
                    break;
            }
        }
    }

    package Interfaces;
import Models.Tarjeta;
import java.util.ArrayList;
import java.util.List;

    public interface ITarjetaDAO {
        boolean guardar(Tarjeta tarjeta);
        ArrayList<Tarjeta> listar();
        Tarjeta buscar(int id);
        boolean actualizarTarje(Tarjeta tarjeta);
        boolean eliminar(int id);
    }
    package CONNECTION;

import java.sql.Connection;
import java.sql.DriverManager;

    public class Conexion {
        public static Connection conectar() {
            Connection con = null;

            try {

                String servidor = "localhost";
                String puerto = "1433";
                String baseDatos = "TARJETAS_BD";

                String usuario = "sa";
                String password = "Polar117#";

                String url = "jdbc:sqlserver://"
                        + servidor + ":"
                        + puerto
                        + ";databaseName="
                        + baseDatos
                        + ";encrypt=true;"
                        + "trustServerCertificate=true;";

                con = DriverManager.getConnection(
                        url,
                        usuario,
                        password
                );

            }
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error al conectar con la base de datos.");
            }

            return con;
        }



    }*/

/*

package Interfaces;
import Models.Tarjeta;

import java.util.ArrayList;
import java.util.List;

public interface ITarjetaDAO {
    boolean guardar(Tarjeta tarjeta);
    ArrayList<Tarjeta> listar();
    Tarjeta buscar(int id);
    boolean actualizarTarje(Tarjeta tarjeta);
    boolean eliminar(int id);
}


package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import CONNECTION.Conexion;
import Models.Tarjeta;
import Interfaces.ITarjetaDAO;
import Config.DBHelper;

public class TarjetaDao implements ITarjetaDAO {

    @Override
    public boolean guardar(Tarjeta tarjeta) {
        String sql = "INSERT INTO tarjetas_emiel (clabe, numero, fecha_exp, cvv, saldo, tipo, credito, activo, id_user) VALUES(?,?,?,?,?,?,?,?,?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tarjeta.getClabe());
            ps.setString(2, tarjeta.getNumero());
            ps.setString(3, tarjeta.getFecha_exp());
            ps.setString(4, tarjeta.getCvv());
            ps.setDouble(5, tarjeta.getSaldo());
            ps.setString(6, tarjeta.getTipo());
            ps.setDouble(7, tarjeta.getCredito());
            ps.setBoolean(8, tarjeta.isActivo());
            ps.setInt(9, tarjeta.getId_user());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error al guardar tarjeta: " + e.getLocalizedMessage());
            return false;
        }
    }

    @Override
    public ArrayList<Tarjeta> listar() {
        ArrayList<Tarjeta> lista = new ArrayList<>();
        String sql = "SELECT * FROM tarjetas_emiel ORDER BY id_tarjeta";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Tarjeta tarjeta = new Tarjeta();

                tarjeta.setId_tarjeta(rs.getInt("id_tarjeta"));
                tarjeta.setClabe(rs.getString("clabe"));
                tarjeta.setNumero(rs.getString("numero"));
                tarjeta.setFecha_exp(rs.getString("fecha_exp"));
                tarjeta.setCvv(rs.getString("cvv"));
                tarjeta.setSaldo(rs.getDouble("saldo"));
                tarjeta.setTipo(rs.getString("tipo"));
                tarjeta.setCredito(rs.getDouble("credito"));
                tarjeta.setActivo(rs.getBoolean("activo"));
                tarjeta.setId_user(rs.getInt("id_user"));

                lista.add(tarjeta);
            }
        } catch (Exception e) {
            System.out.println("Error al listar tarjetas: " + e.getLocalizedMessage());
        }
        return lista;
    }

    @Override
    public Tarjeta buscar(int id) {
        Tarjeta tarjeta = null;
        String sql = "SELECT * FROM tarjetas_emiel WHERE id_tarjeta = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    tarjeta = new Tarjeta();

                    tarjeta.setId_tarjeta(rs.getInt("id_tarjeta"));
                    tarjeta.setClabe(rs.getString("clabe"));
                    tarjeta.setNumero(rs.getString("numero"));
                    tarjeta.setFecha_exp(rs.getString("fecha_exp"));
                    tarjeta.setCvv(rs.getString("cvv"));
                    tarjeta.setSaldo(rs.getDouble("saldo"));
                    tarjeta.setTipo(rs.getString("tipo"));
                    tarjeta.setCredito(rs.getDouble("credito"));
                    tarjeta.setActivo(rs.getBoolean("activo"));
                    tarjeta.setId_user(rs.getInt("id_user"));
                }
            }
        } catch (Exception e) {
            System.out.println("Error al buscar tarjeta: " + e.getLocalizedMessage());
        }
        return tarjeta;
    }

    @Override
    public boolean actualizarTarje(Tarjeta tarjeta) {
        String sql = "UPDATE tarjetas_emiel SET clabe=?, numero=?, fecha_exp=?, cvv=?, saldo=?, tipo=?, credito=?, activo=?, id_user=? WHERE id_tarjeta=?";
        int filas =DBHelper.execute(sql,
                tarjeta.getClabe(),
                tarjeta.getNumero(),
                tarjeta.getFecha_exp(),
                tarjeta.getCvv(),
                tarjeta.getSaldo(),
                tarjeta.getTipo(),
                tarjeta.getCredito(),
                tarjeta.isActivo(),
                tarjeta.getId_user(),
                tarjeta.getId_tarjeta()
        );
        return  filas>0;
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM tarjetas_emiel WHERE id_tarjeta = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error al eliminar tarjeta: " + e.getLocalizedMessage());
            return false;
        }
    }


    public boolean cambiarEstadoTarjeta(int idTarjeta, boolean estado) {
        String sql = "UPDATE tarjetas_emiel SET activo = ? WHERE id_tarjeta = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, estado);
            ps.setInt(2, idTarjeta);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error al cambiar estado de tarjeta: " + e.getLocalizedMessage());
            return false;
        }
    }

    public ArrayList<Tarjeta> buscarPorUsuario(int idUsuario) {
        ArrayList<Tarjeta> lista = new ArrayList<>();
        String sql = "SELECT * FROM tarjetas_emiel WHERE id_user = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Tarjeta t = new Tarjeta();
                    t.setId_tarjeta(rs.getInt("id_tarjeta"));
                    t.setNumero(rs.getString("numero"));
                    t.setClabe(rs.getString("clabe"));
                    t.setFecha_exp(rs.getString("fecha_exp"));
                    t.setCvv(rs.getString("cvv"));
                    t.setTipo(rs.getString("tipo"));
                    t.setSaldo(rs.getDouble("saldo"));
                    t.setCredito(rs.getDouble("credito"));
                    t.setActivo(rs.getBoolean("activo"));
                    t.setId_user(rs.getInt("id_user"));

                    lista.add(t);
                }
            }
        } catch (Exception e) {
            System.out.println("Error al buscar tarjetas del usuario: " + e.getMessage());
        }



        return lista;
    }
    public Tarjeta buscarClabe(String clabe){
        String sql = "SELECT * FROM tarjetas_emiel WHERE clabe = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, clabe);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Tarjeta t = new Tarjeta();
                    t.setId_tarjeta(rs.getInt("id_tarjeta"));
                    t.setNumero(rs.getString("numero"));
                    t.setClabe(rs.getString("clabe"));
                    t.setFecha_exp(rs.getString("fecha_exp"));
                    t.setCvv(rs.getString("cvv"));
                    t.setTipo(rs.getString("tipo"));
                    t.setSaldo(rs.getDouble("saldo"));
                    t.setCredito(rs.getDouble("credito"));
                    t.setActivo(rs.getBoolean("activo"));
                    t.setId_user(rs.getInt("id_user"));
                    return t;
                }
            }
        } catch (Exception e) {
            System.out.println("Error al buscar tarjeta por clabe: " + e.getMessage());
        }
        return null;
    }
}
{
        Scanner leer = new Scanner(System.in);
        DAOjugador dao = new DAOjugador();

        ArrayList<Jugador> listaJugadores = new ArrayList<>();
        boolean sal = true;
        int acc;
        System.out.println("Bienvenido a su tic tac toe");
        while (sal) {
            System.out.println("Inserte que quiere realizar");
            System.out.println("1. Registrar jugador");
            System.out.println("2. Jugar");
            System.out.println("3. Mostrar jugadores");
            System.out.println("4. Salir");
            acc = Validacion.Scanner_enteros(leer, "");

            switch (acc) {
                case 1:
                    System.out.println("Insertar nickname del jugador:");
                    String nicknam = Validacion.scan_string_vacio(leer);

                    Jugador nuevoJuga = new Jugador(nicknam, 0, 0, 0);

                    if (dao.InsertarJugador(nuevoJuga)) {
                        System.out.println("Jugador registrado con éxito");
                    } else {
                        System.out.println("Error al registrar el jugador .");
                    }

                    break;

                case 2:
                    System.out.println("Bienvenido al Juego del gatito");

                    Random random = new Random();
                    System.out.println("=======================");
                    List<Jugador> Opciones = dao.ListarTodos();
                    if (Opciones.isEmpty()) {
                        System.out.println("Lista de jugadores vacia");
                    } else {
                        for (Jugador jug : Opciones) {
                            System.out.println("ID: " + jug.getId() +
                                    "|Nickname: " + jug.getNickname() +
                                    "|Wins: " + jug.getWins() +
                                    "|Lose: " + jug.getLose() +
                                    "|Tie: " + jug.getTie());
                        }
                    }
                    Jugador Yin = null;
                    int idP1 = 0;
                    do {
                        System.out.println("Jugador 1 Seleccione el jugador (Ingrese el ID)");
                        idP1 = Validacion.Scanner_enteros(leer, "");
                        Yin = dao.BuscarPorId(idP1);
                        if (Yin == null) {
                            System.out.println("No se encuentra jugador con ese ID");
                            System.out.println("Intente de nuevo...");
                        }

                    } while (Yin == null);
                    int idP2 = 0;
                    Jugador Yang = null;

                    do {
                        System.out.println("Jugador 2 Seleccione el jugador (Ingrese el ID)");
                        idP2 = Validacion.Scanner_enteros(leer, "");

                        Yang = dao.BuscarPorId(idP2);

                        if (Yang == null) {
                            System.out.println("No se encuentra jugador con ese ID");
                            System.out.println("Intente de nuevo...");
                        }

                        if (idP2 == Yin.getId()) {
                            System.out.println("No se puede escoger al mismo jugador");
                            System.out.println("Intente de nuevo...");
                        }

                    } while (Yang == null || idP2 == idP1);

                    System.out.println("======================");

                    System.out.println("Se lanzara una moneda para decidir el jugador que empieza ");
                    System.out.println("Suerte");

                    System.out.println("*Se lanza la moneda epicamente*");

                    int moneda = random.nextInt(2) + 1;
                    if (moneda == 1) {
                        System.out.println("El jugador a iniciar sera el jugador 1");
                    } else
                        System.out.println("El jugador a iniciar sera el jugador 2");


                    String[] gatito = {"[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]", "[]"};

                    int turnoActual = moneda;

                    boolean juegoActivo = true;
                    int movimientos = 0;


                    while (juegoActivo) {

                        System.out.println("\n===============================");
                        System.out.println("   | " + gatito[0] + " | " + gatito[1] + " | " + gatito[2] + " |");
                        System.out.println("   | " + gatito[3] + " | " + gatito[4] + " | " + gatito[5] + " |");
                        System.out.println("   | " + gatito[6] + " | " + gatito[7] + " | " + gatito[8] + " |");
                        System.out.println("===============================");

                        Jugador jugadorActual;
                        String ficha;

                        if (turnoActual == 1) {
                            jugadorActual = Yin;
                            ficha = "X";
                        } else {
                            jugadorActual = Yang;
                            ficha = "O";
                        }

                        int posicion = 0;
                        boolean casillaValida = false;

                        while (!casillaValida) {
                            System.out.println("Turno de " + jugadorActual.getNickname() + " (" + ficha + ")");
                            System.out.print("Elija una casilla disponible (1-9): ");
                            posicion = Validacion.Scanner_enteros(leer, "") - 1;
                            if (posicion < 0 || posicion > 8) {
                                System.out.println(" Opción inválida. Debe ser un número del 1 al 9.");
                            } else if (gatito[posicion].equals("X") || gatito[posicion].equals("O")) {
                                System.out.println(" Esa casilla ya está ocupada. Elija otra.");
                            } else {
                                casillaValida = true;
                            }
                        }

                        gatito[posicion] = ficha;
                        movimientos++;

                        String fichaSiguiente = ficha.equals("X") ? "O" : "X";

                        if (evaluarGanador(gatito, ficha)) {
                            System.out.println("===============================");
                            System.out.println("   | " + gatito[0] + " | " + gatito[1] + " | " + gatito[2] + " |");
                            System.out.println("   | " + gatito[3] + " | " + gatito[4] + " | " + gatito[5] + " |");
                            System.out.println("   | " + gatito[6] + " | " + gatito[7] + " | " + gatito[8] + " |");
                            System.out.println("===============================");

                            System.out.println(" ¡Felicidades por ganar, " + jugadorActual.getNickname() + "! ");

                            if (jugadorActual == Yin) {
                                Yin.setWins(Yin.getWins() + 1);
                                Yang.setLose(Yang.getLose() + 1);
                            } else {
                                Yang.setWins(Yang.getWins() + 1);
                                Yin.setLose(Yin.getLose() + 1);
                            }

                            dao.ActualizarJugadores(Yin);
                            dao.ActualizarJugadores(Yang);

                            juegoActivo = false;

                        } else if (empateauto(gatito, fichaSiguiente) || movimientos == 9) {
                            System.out.println("===============================");
                            System.out.println("   | " + gatito[0] + " | " + gatito[1] + " | " + gatito[2] + " |");
                            System.out.println("   | " + gatito[3] + " | " + gatito[4] + " | " + gatito[5] + " |");
                            System.out.println("   | " + gatito[6] + " | " + gatito[7] + " | " + gatito[8] + " |");
                            System.out.println("===============================");

                            System.out.println(" Empate. Es imposible que alguno gane en las líneas restantes.");

                            Yin.setTie(Yin.getTie() + 1);
                            Yang.setTie(Yang.getTie() + 1);

                            dao.ActualizarJugadores(Yin);
                            dao.ActualizarJugadores(Yang);

                            juegoActivo = false;

                        } else {
                            if (turnoActual == 1) {
                                turnoActual = 2;
                            } else {
                                turnoActual = 1;
                            }
                        }

                    }
                    break;

                case 3:
                    System.out.println("Mostrando jugadores");
                    List<Jugador> players = dao.ListarTodos();
                    if (players.isEmpty()){
                        System.out.println("Lista de jugadores vacia");
                    }
                    else {
                        for (Jugador jug : players){
                            System.out.println("ID: " + jug.getId()+
                                    "|Nickname: " +jug.getNickname()+
                                    "|Wins: "+jug.getWins()+
                                    "|Lose: "+jug.getLose()+
                                    "|Tie: "+jug.getTie());
                        }
                    }
                    break;

                case 4:
                    sal = false;
                    break;
                default:
                    System.out.println("Opcion invalida");
                    break;
            }

        }

    }
*/

