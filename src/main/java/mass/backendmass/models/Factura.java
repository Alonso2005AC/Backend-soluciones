package mass.backendmass.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "facturas")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_factura;

    private int id_venta;

    @Column(unique = true, nullable = false)
    private String numero_factura;

    @Column(nullable = false)
    private Timestamp fecha_emision;

    @Enumerated(EnumType.STRING)
    private TipoComprobante tipo_comprobante = TipoComprobante.boleta;

    @Column(nullable = false)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.emitida;

    @Column(columnDefinition = "TEXT")
    private String datos_fiscales;

    public enum TipoComprobante {
        boleta,
        factura
    }

    public enum Estado {
        emitida,
        anulada
    }

    @PrePersist
    protected void onCreate() {
        this.fecha_emision = new Timestamp(System.currentTimeMillis());
    }

    // Getters y Setters
    public int getId_factura() { return id_factura; }
    public void setId_factura(int id_factura) { this.id_factura = id_factura; }

    public int getId_venta() { return id_venta; }
    public void setId_venta(int id_venta) { this.id_venta = id_venta; }

    public String getNumero_factura() { return numero_factura; }
    public void setNumero_factura(String numero_factura) { this.numero_factura = numero_factura; }

    public Timestamp getFecha_emision() { return fecha_emision; }
    public void setFecha_emision(Timestamp fecha_emision) { this.fecha_emision = fecha_emision; }

    public TipoComprobante getTipo_comprobante() { return tipo_comprobante; }
    public void setTipo_comprobante(TipoComprobante tipo_comprobante) { this.tipo_comprobante = tipo_comprobante; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public String getDatos_fiscales() { return datos_fiscales; }
    public void setDatos_fiscales(String datos_fiscales) { this.datos_fiscales = datos_fiscales; }
}
