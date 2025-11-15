package mass.backendmass.models;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "detalle_venta")
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_detalle;

    private int id_venta;
    private int id_producto;
    private int cantidad;

    @Column(nullable = false)
    private BigDecimal precio_unitario;

    @Column(nullable = false)
    private BigDecimal subtotal;

    private String lote;

    // Getters y Setters
    public int getId_detalle() { return id_detalle; }
    public void setId_detalle(int id_detalle) { this.id_detalle = id_detalle; }

    public int getId_venta() { return id_venta; }
    public void setId_venta(int id_venta) { this.id_venta = id_venta; }

    public int getId_producto() { return id_producto; }
    public void setId_producto(int id_producto) { this.id_producto = id_producto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecio_unitario() { return precio_unitario; }
    public void setPrecio_unitario(BigDecimal precio_unitario) { this.precio_unitario = precio_unitario; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public String getLote() { return lote; }
    public void setLote(String lote) { this.lote = lote; }
}
