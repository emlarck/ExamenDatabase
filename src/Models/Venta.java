package Models;

public class Venta {
    private int id_ventas;
    private int quantity;
    private double unit_price;
    private double subtotal;
    private double iva;
    private double total;
    protected int producto;

    public Venta(int quantity, double unit_price, double subtotal, double iva, double total, int producto) {
        this.quantity = quantity;
        this.unit_price = unit_price;
        this.subtotal = subtotal;
        this.iva = iva;
        this.total = total;
        this.producto = producto;
    }

    public Venta() {
    }

    public int getId_ventas() {
        return id_ventas;
    }

    public void setId_ventas(int id_ventas) {
        this.id_ventas = id_ventas;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getProducto() {
        return producto;
    }

    public void setProducto(int producto) {
        this.producto = producto;
    }
}

