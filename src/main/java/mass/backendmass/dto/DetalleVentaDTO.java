package mass.backendmass.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DetalleVentaDTO {
    @JsonProperty("id_producto")
    private int id_producto;
    
    @JsonProperty("cantidad")
    private int cantidad;
    
    @JsonProperty("precio_unitario")
    private double precio_unitario;
    
    @JsonProperty("subtotal")
    private double subtotal;

    // Constructor vacío
    public DetalleVentaDTO() {}

    // Constructor con parámetros
    public DetalleVentaDTO(int id_producto, int cantidad, double precio_unitario, double subtotal) {
        this.id_producto = id_producto;
        this.cantidad = cantidad;
        this.precio_unitario = precio_unitario;
        this.subtotal = subtotal;
    }

    // Getters y Setters
    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(double precio_unitario) {
        this.precio_unitario = precio_unitario;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}
