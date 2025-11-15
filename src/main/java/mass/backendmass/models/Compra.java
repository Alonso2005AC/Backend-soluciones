package mass.backendmass.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "compras")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_compra;

    private int id_proveedor;
    private Integer id_usuario; // usuario que registra la compra, nullable

    @Column(nullable = false)
    private Timestamp fecha_compra;

    @Column(nullable = false)
    private BigDecimal total = BigDecimal.valueOf(0.00);

    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.pendiente;

    private String observacion;

    public enum Estado {
        pendiente,
        recepcionada,
        cancelada
    }

    @PrePersist
    protected void onCreate() {
        this.fecha_compra = new Timestamp(System.currentTimeMillis());
    }

    // Getters y Setters
    public int getId_compra() { return id_compra; }
    public void setId_compra(int id_compra) { this.id_compra = id_compra; }

    public int getId_proveedor() { return id_proveedor; }
    public void setId_proveedor(int id_proveedor) { this.id_proveedor = id_proveedor; }

    public Integer getId_usuario() { return id_usuario; }
    public void setId_usuario(Integer id_usuario) { this.id_usuario = id_usuario; }

    public Timestamp getFecha_compra() { return fecha_compra; }
    public void setFecha_compra(Timestamp fecha_compra) { this.fecha_compra = fecha_compra; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}
