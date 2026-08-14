import Daos.DaoProducto;
import Daos.DaoVentas;
import Models.Producto;
import Models.Venta;
import Utils.Validacion;

import java.util.ArrayList;
import java.util.Scanner;



    void main(String[] args) {
        DaoProducto dao = new DaoProducto();
        DaoVentas vent = new DaoVentas();
        int acc;
        boolean salV= true;
        boolean sal = true;
        boolean salo = true;
        Scanner scan = new Scanner(System.in);

    System.out.println("Bienvendio a su inventario");
    while (sal) {
        System.out.println("Inserte que quiere realizar");
    System.out.println("1. Gestionar Productos");
    System.out.println("2. Ventas");
    System.out.println("3. Salir");
    acc = Validacion.Scanner_enteros(scan,"");
    switch (acc){
        case 1:
            while (salo) {
                int accp;
                System.out.println("Inserte que quiere realizar");
                System.out.println("1. Mostrar productos");
                System.out.println("2. Crear productos");
                System.out.println("3. Editar productos");
                System.out.println("4. Eliminar productos");
                System.out.println("5. Salir");
                accp = Validacion.Scanner_enteros(scan,"");
            switch (accp){
                case 1:
                        ArrayList<Producto> productos = dao.listar();

                        if (productos.isEmpty()){
                            System.out.println("No hay productos que mostrar");
                            break;
                        }
                    System.out.println("-----PRODUCTOS------");
                        for (Producto produ : productos){
                            System.out.println("Id_producto: "+produ.getId_producto()+"|"+"Nombre: "+produ.getNombre()+"|"+"Precio: "+produ.getPrecio()+"|Stock: "+produ.getStock());
                        }

                    break;
                case 2:
                    System.out.println("Ingrese el nombre del producto");
                    String productoniu = Validacion.scan_string_vacio(scan);
                    double precioPorducto;
                    do {
                        System.out.println("Ingrese el precio del producto");
                        precioPorducto = Validacion.Scanner_doubles(scan,"");
                        if (precioPorducto<=0){
                            System.out.println("Ingrese un precio valido");
                        }
                    }while (precioPorducto<=0);
                    int stock;
                    do {
                        System.out.println("Ingrese el stock");
                        stock =Validacion.Scanner_enteros(scan,"");
                        if (stock<=0){
                            System.out.println("No se puede ingresar un stock menor o igual a 0");
                        }
                    } while (stock<=0);

                    Producto productoNuevo = new Producto(productoniu,precioPorducto,stock);
                    dao.guardarProducto(productoNuevo);
                    break;
                case 3:
                    ArrayList<Producto> productosedi = dao.listar();
                    if (productosedi.isEmpty()) {
                        System.out.println("No hay productos que mostrar.");
                        break;
                    }

                    System.out.println("----- PRODUCTOS DISPONIBLES ------");
                    for (Producto p : productosedi) {
                        System.out.println("ID: " + p.getId_producto() + " | Nombre: " + p.getNombre() + " | Precio: $" + p.getPrecio() + " | Stock: " + p.getStock());
                    }

                    System.out.println("\nIngrese el ID del producto que desea actualizar:");
                    int idProd = Validacion.Scanner_enteros(scan, "");
                    Producto productoelegido = null;

                    for (Producto p : productosedi) {
                        if (p.getId_producto() == idProd) {
                            productoelegido = p;
                            break;
                        }
                    }

                    if (productoelegido == null) {
                        System.out.println("El ID ingresado no corresponde a ningún producto.");
                        break;
                    }
                    boolean salact = true;
                        while(salact){
                            int act;
                            System.out.println("Que quiere actualizar");
                            System.out.println("1. Nombre");
                            System.out.println("2. Precio");
                            System.out.println("3. Stock");
                            System.out.println("4.Salir");
                            act = Validacion.Scanner_enteros(scan,"");
                            switch (act){
                                case 1:
                                    System.out.println("Inserte el nuevo nombre:");
                                    String nv = Validacion.scan_string_vacio(scan);
                                    productoelegido.setNombre(nv);
                                    dao.actualizar(productoelegido);
                                    break;
                                case 2:
                                    double precio;
                                    do {
                                        System.out.println("Inserte el nuevo precio:");
                                        precio = Validacion.Scanner_doubles(scan, "");
                                        if (precio<=0 ){
                                            System.out.println("El precio no puede ser 0 menor");
                                        }

                                    }while (precio<=0);
                                    productoelegido.setPrecio(precio);
                                    dao.actualizar(productoelegido);
                                    break;
                                case 3:
                                    int st;
                                    do {
                                        System.out.println("Inserte el nuevo stock:");
                                        st = Validacion.Scanner_enteros(scan, "");
                                        if (st <= 0) {
                                            System.out.println("El stock no puede ser menor o igual a 0");
                                        }
                                    } while (st<=0);
                                        productoelegido.setStock(st);
                                        dao.actualizar(productoelegido);
                                    break;
                                case 4:
                                    salact = false;
                                    break;
                                default:
                                    System.out.println("Opcion invalida");

                            }
                        }

                    break;
                case 4:
                    System.out.println("\n--- ELIMINAR PRODUCTO ---");
                    ArrayList<Producto> listaEliminar = dao.listar();
                    if (listaEliminar.isEmpty()) {
                        System.out.println("No hay productosedi disponibles para eliminar.");
                        break;
                    } else{
                        for (Producto p : listaEliminar) {
                            System.out.println("ID: " + p.getId_producto() + " | Nombre: " + p.getNombre() + " | Stock: " + p.getStock());
                        }
                        System.out.println("\nIngrese el ID del producto que desea eliminar:");
                        int idEliminar = Validacion.Scanner_enteros(scan, "");
                        Producto prodExiste = dao.buscar(idEliminar);

                        if (prodExiste ==null){
                            System.out.println("El id ingresado no existe");
                            break;
                        }
                        if (prodExiste != null) {
                            System.out.println("¿Está seguro de eliminar '" + prodExiste.getNombre() + "'? (1: Sí / 2: No)");
                            int confirma = Validacion.Scanner_enteros(scan, "");
                            if (confirma == 1) {
                                if (dao.eliminarProducto(idEliminar)) {
                                    System.out.println("¡Producto eliminado con éxito!");
                                    break;
                                    } else {
                                        System.out.println("No se pudo eliminar el producto");
                                    }
                                } if (confirma == 2) {
                                    System.out.println("Operación cancelada.");
                                    break;
                                }else {
                                    System.out.println("Opcion invalida");
                                    break;
                                }
                            }
                        
                    }
                    break;
                case 5:
                    System.out.println("Saliendo del menu de porductos...");
                    salo = false;
                    break;
                default:
                    System.out.println("Opcion invalida");
                    break;
            }
            }
            break;
        case 2:
            while (salV) {
                System.out.println("1. Realizar una Venta");
                System.out.println("2. Mostar todas las ventas");
                System.out.println("3. Mostrar venta con MAYOR total");
                System.out.println("4. Mostrar venta con MENOR total");
                System.out.println("5. Volver al menú principal");
                System.out.print("Seleccione una opción: ");
                int opcionVenta = Validacion.Scanner_enteros(scan,"");
                switch (opcionVenta) {
                    case 1:
                        ArrayList<Producto> productos = dao.listar();
                        if (productos.isEmpty()) {
                            System.out.println("No hay productos disponibles para vender.");
                            break;
                        }
                        System.out.println("\n--- PRODUCTOS DISPONIBLES ---");
                        for (Producto p : productos) {
                            System.out.println("ID: " + p.getId_producto() + " | Nombre: " + p.getNombre() + " | Precio: $" + p.getPrecio() + " | Stock: " + p.getStock());
                        }

                        System.out.println("\nIngrese el ID del producto que desea comprar:");
                        int idProd = Validacion.Scanner_enteros(scan, "");
                        Producto prodSeleccionado = null;
                        for (Producto p : productos) {
                            if (p.getId_producto() == idProd) {
                                prodSeleccionado = p;
                                break;
                            }
                        }
                        if (prodSeleccionado == null) {
                            System.out.println("El ID ingresado no corresponde a ningún producto.");
                            break;
                        }
                        int cantidad;
                            System.out.println("Ingrese la cantidad a comprar (Stock disponible: " + prodSeleccionado.getStock() + "):");
                            cantidad = Validacion.Scanner_enteros(scan, "");

                            if (cantidad <= 0) {
                                System.out.println("La cantidad debe ser mayor a 0.");
                                break;
                            } else if (cantidad > prodSeleccionado.getStock()) {
                                System.out.println("Stock insuficiente. Intente con una cantidad menor.");
                                break;
                            }

                        double precioUnitario = prodSeleccionado.getPrecio();
                        double subtotal = precioUnitario * cantidad;
                        double iva = subtotal * 0.16;
                        double total = subtotal + iva;
                        Venta nuevaVenta = new Venta();
                        nuevaVenta.setQuantity(cantidad);
                        nuevaVenta.setUnit_price(precioUnitario);
                        nuevaVenta.setSubtotal(subtotal);
                        nuevaVenta.setIva(iva);
                        nuevaVenta.setTotal(total);
                        nuevaVenta.setProducto(prodSeleccionado.getId_producto());
                        if (vent.guardarVenta(nuevaVenta)) {
                            System.out.println("\n¡Venta registrada con éxito!");
                            System.out.println("Resumen: Subtotal: $" + subtotal + " | IVA: $" + iva + " | Total: $" + total);
                        } else {
                            System.out.println("Error al procesar la venta.");
                        }
                        break;
                    case 2:
                        ArrayList<Venta> listaVentas = vent.listarVentas();
                        if (listaVentas.isEmpty()) {
                            System.out.println("No se han registrado ventas aún.");
                        } else {
                            System.out.println("\n--- HISTORIAL DE VENTAS ---");
                            for (Venta v : listaVentas) {
                                System.out.println("Producto ID: " + v.getProducto() +
                                        " | Cantidad: " + v.getQuantity() +
                                        " | Precio U.: $" + v.getUnit_price() +
                                        " | Subtotal: $" + v.getSubtotal() +
                                        " | IVA: $" + v.getIva() +
                                        " | Total: $" + v.getTotal());
                            }
                        }
                        break;
                    case 3:
                        Venta ventaMayor = vent.VentaMayor();
                        if (ventaMayor != null) {
                            System.out.println("\n--- VENTA CON MAYOR TOTAL ---");
                            System.out.println("Producto ID: " + ventaMayor.getProducto() +
                                    " | Cantidad: " + ventaMayor.getQuantity() +
                                    " | Total: $" + ventaMayor.getTotal());
                        } else {
                            System.out.println("No hay ventas registradas.");
                        }
                        break;

                    case 4:
                        Venta ventaMenor = vent.VentaMenor();
                        if (ventaMenor != null) {
                            System.out.println("\n--- VENTA CON MENOR TOTAL ---");
                            System.out.println("Producto ID: " + ventaMenor.getProducto() +
                                    " | Cantidad: " + ventaMenor.getQuantity() +
                                    " | Total: $" + ventaMenor.getTotal());
                        } else {
                            System.out.println("No hay ventas registradas.");
                        }
                        break;

                    case 5:
                        salV = false;
                        break;

                    default:
                        System.out.println("Opción inválida.");
                        break;
                }
            }
            break;
        case 3:
            sal = false;
            break;
        default:
            System.out.println("Opcion invalida");
            break;
    }
}
}