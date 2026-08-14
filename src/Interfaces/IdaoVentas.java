package Interfaces;

import Models.Producto;
import Models.Venta;

import java.util.ArrayList;
public interface IdaoVentas {
    boolean guardarVenta(Venta venta);
    boolean elimianrVenta(int id);
    boolean actualizarVenta(Venta venta);
    ArrayList<Producto> listar();
    ArrayList<Venta> listarVentas();
    Venta VentaMayor();
    Venta VentaMenor();

}
