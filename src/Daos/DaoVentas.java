package Daos;

import Conexion.Conecction;
import Interfaces.IdaoVentas;
import Models.Producto;
import Models.Venta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DaoVentas implements IdaoVentas {

    @Override
    public boolean guardarVenta(Venta venta) {
        String sqlVenta = "INSERT INTO ventas (quantity, unit_price, subtotal, iva, total, producto) VALUES (?,?,?,?,?,?)";
        String sqlStock = "UPDATE productos SET stock = stock - ? WHERE id_producto = ?";
        try (Connection con = Conecction.getConexion()) {
            con.setAutoCommit(false);
            try (PreparedStatement psVenta = con.prepareStatement(sqlVenta)) {
                psVenta.setInt(1, venta.getQuantity());
                psVenta.setDouble(2, venta.getUnit_price());
                psVenta.setDouble(3, venta.getSubtotal());
                psVenta.setDouble(4, venta.getIva());
                psVenta.setDouble(5, venta.getTotal());
                psVenta.setInt(6, venta.getProducto());
                psVenta.executeUpdate();
            }

            try (PreparedStatement psStock = con.prepareStatement(sqlStock)) {
                psStock.setInt(1, venta.getQuantity());
                psStock.setInt(2, venta.getProducto());
                psStock.executeUpdate();
            }

            con.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean elimianrVenta(int id) {
        return false;
    }

    @Override
    public boolean actualizarVenta(Venta venta) {
        return false;
    }


    @Override
    public ArrayList<Venta> listarVentas() {
        ArrayList<Venta> lista = new ArrayList<>();
        String sql = "SELECT * FROM ventas";

        try (Connection con = Conecction.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Venta v = mapResultSetToVenta(rs);
                lista.add(v);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }


    public Venta VentaMayor() {
        String sql = "SELECT TOP 1 * FROM ventas ORDER BY total DESC ";

        try (Connection con = Conecction.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return mapResultSetToVenta(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public Venta VentaMenor() {
        String sql = "SELECT TOP 1 * FROM ventas ORDER BY total ASC ";

        try (Connection con = Conecction.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return mapResultSetToVenta(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    private Venta mapResultSetToVenta(ResultSet rs) throws Exception {
        Venta v = new Venta();
        v.setId_ventas(rs.getInt("Id_ventas"));
        v.setQuantity(rs.getInt("quantity"));
        v.setUnit_price(rs.getDouble("unit_price"));
        v.setSubtotal(rs.getDouble("subtotal"));
        v.setIva(rs.getDouble("iva"));
        v.setTotal(rs.getDouble("total"));
        v.setProducto(rs.getInt("producto"));
        return v;
    }
    @Override
    public ArrayList<Producto> listar() {
        return null;
    }


}
