package mass.backendmass.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "detalle_compra")
public class DetalleCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_detalle_compra;

    private int id_compra;
    private int id_producto;
    private int cantidad;

    @Column(nullable = false)
    private BigDecimal precio_unitario;

    @Column(nullable = false)
    private BigDecimal subtotal;

    private String lote;
    private Date fecha_vencimiento;

    // Getters y Setters
    public int getId_detalle_compra() { return id_detalle_compra; }
    public void setId_detalle_compra(int id_detalle_compra) { this.id_detalle_compra = id_detalle_compra; }

    public int getId_compra() { return id_compra; }
    public void setId_compra(int id_compra) { this.id_compra = id_compra; }

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

    public Date getFecha_vencimiento() { return fecha_vencimiento; }
    public void setFecha_vencimiento(Date fecha_vencimiento) { this.fecha_vencimiento = fecha_vencimiento; }
}
