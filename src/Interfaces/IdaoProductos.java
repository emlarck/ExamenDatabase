package Interfaces;

import Models.Producto;

import java.util.ArrayList;

public interface IdaoProductos {
    boolean guardarProducto(Producto producto);
    ArrayList<Producto> listar();
    Producto buscar(int id_producto);
    boolean actualizar(Producto producto);
    boolean eliminarProducto(int id);
}

