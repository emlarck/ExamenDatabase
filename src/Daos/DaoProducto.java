package Daos;

import Conexion.Conecction;
import Config.DBHelper;
import Interfaces.IdaoProductos;
import Models.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DaoProducto implements IdaoProductos {


    @Override
    public boolean guardarProducto(Producto producto) {
        String sql = "INSERT INTO productos(nombre,precio,stock) VALUES (?,?,?)";

        try (Connection con = Conecction.getConexion ();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecio());
            ps.setInt(3, producto.getStock());
            return ps.executeUpdate() >0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ArrayList<Producto> listar() {
        ArrayList<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos ORDER BY id_producto";
        try (Connection con =Conecction.getConexion ();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producto producto = new Producto();

                producto.setId_producto(rs.getInt("Id_producto"));
                producto.setNombre(rs.getString("Nombre"));
                producto.setPrecio(rs.getDouble("Precio"));
                producto.setStock(rs.getInt("Stock"));

                lista.add(producto);
            }
        } catch (Exception e) {
            System.out.println("Error al listar productos: " + e.getLocalizedMessage());
        }
        return lista;
    }

    @Override
    public Producto buscar(int id_producto) {
        Producto producto = null;
        String sql = "SELECT * FROM productos WHERE id_producto =?";

        try (Connection con = Conecction.getConexion();
        PreparedStatement ps = con.prepareStatement(sql)){

            ps.setInt(1,id_producto);
            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()){
                 producto = new Producto();
                 producto.setId_producto(rs.getInt("id_producto"));
                 producto.setNombre(rs.getString("nombre"));
                 producto.setPrecio(rs.getDouble("precio"));
                 producto.setStock(rs.getInt("stock"));


                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return producto;
    }

    @Override
    public boolean actualizar(Producto producto) {
        String sql = "UPDATE productos SET nombre=?, precio=?, stock=? WHERE id_producto=?";

        try (Connection con = Conecction.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecio());
            ps.setInt(3, producto.getStock());
            ps.setInt(4, producto.getId_producto());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error al actualizar producto: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean eliminarProducto(int id) {
            String sql = "DELETE FROM productos WHERE Id_producto = ?";
            try (Connection con = Conecction.getConexion();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, id);
                return ps.executeUpdate() > 0;

            } catch (Exception e) {
                System.out.println("Error al eliminar producto: " + e.getMessage());
            }
            return false;
    }
}
