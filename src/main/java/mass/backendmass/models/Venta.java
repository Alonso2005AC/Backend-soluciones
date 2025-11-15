package mass.backendmass.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "ventas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_venta;

    @Column(nullable = false)
    private Timestamp fecha_venta;

    private Integer id_cliente; // nullable
    private Integer id_usuario; // nullable

    @Column(nullable = false)
    private BigDecimal total = BigDecimal.valueOf(0.00);

    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.completada;

    @Enumerated(EnumType.STRING)
    private MetodoPago metodo_pago;

    private String observacion;

    public enum Estado {
        pendiente,
        completada,
        anulada
    }

    public enum MetodoPago {
        tarjeta,
        agora
    }

    @PrePersist
    protected void onCreate() {
        this.fecha_venta = new Timestamp(System.currentTimeMillis());
    }

    // Getters y Setters
    public int getId_venta() { return id_venta; }
    public void setId_venta(int id_venta) { this.id_venta = id_venta; }

    public Timestamp getFecha_venta() { return fecha_venta; }
    public void setFecha_venta(Timestamp fecha_venta) { this.fecha_venta = fecha_venta; }

    public Integer getId_cliente() { return id_cliente; }
    public void setId_cliente(Integer id_cliente) { this.id_cliente = id_cliente; }

    public Integer getId_usuario() { return id_usuario; }
    public void setId_usuario(Integer id_usuario) { this.id_usuario = id_usuario; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public MetodoPago getMetodo_pago() { return metodo_pago; }
    public void setMetodo_pago(MetodoPago metodo_pago) { this.metodo_pago = metodo_pago; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}
