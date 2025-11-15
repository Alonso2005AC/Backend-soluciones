package mass.backendmass.models;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "inventario")
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_movimiento;

    private int id_producto;
    private Integer id_almacen; // nullable
     
    @Enumerated(EnumType.STRING)
    private TipoMovimiento tipo_movimiento;

    @Enumerated(EnumType.STRING)
    private ReferenciaTipo referencia_tipo = ReferenciaTipo.otro;

    private Integer referencia_id; // nullable
    private int cantidad;

    @Column(nullable = false)
    private Timestamp fecha_movimiento;

    private String observacion;

    public enum TipoMovimiento {
        entrada,
        salida,
        ajuste,
        traslado
    }

    public enum ReferenciaTipo {
        compra,
        venta,
        ajuste,
        traslado,
        otro
    }

    @PrePersist
    protected void onCreate() {
        this.fecha_movimiento = new Timestamp(System.currentTimeMillis());
    }

    // Getters y Setters
    public int getId_movimiento() { return id_movimiento; }
    public void setId_movimiento(int id_movimiento) { this.id_movimiento = id_movimiento; }

    public int getId_producto() { return id_producto; }
    public void setId_producto(int id_producto) { this.id_producto = id_producto; }

    public Integer getId_almacen() { return id_almacen; }
    public void setId_almacen(Integer id_almacen) { this.id_almacen = id_almacen; }

    public TipoMovimiento getTipo_movimiento() { return tipo_movimiento; }
    public void setTipo_movimiento(TipoMovimiento tipo_movimiento) { this.tipo_movimiento = tipo_movimiento; }

    public ReferenciaTipo getReferencia_tipo() { return referencia_tipo; }
    public void setReferencia_tipo(ReferenciaTipo referencia_tipo) { this.referencia_tipo = referencia_tipo; }

    public Integer getReferencia_id() { return referencia_id; }
    public void setReferencia_id(Integer referencia_id) { this.referencia_id = referencia_id; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public Timestamp getFecha_movimiento() { return fecha_movimiento; }
    public void setFecha_movimiento(Timestamp fecha_movimiento) { this.fecha_movimiento = fecha_movimiento; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}
