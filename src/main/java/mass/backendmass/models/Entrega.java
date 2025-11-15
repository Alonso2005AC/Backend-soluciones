package mass.backendmass.models;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "entregas")
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_entrega;

    private int id_venta;
    private Timestamp fecha_entrega;
    private String medio_transporte;

    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.pendiente;

    private String direccion_envio;
    private String observacion;

    public enum Estado {
        pendiente,
        en_transito,
        entregado,
        cancelado
    }

    // Getters y Setters
    public int getId_entrega() { return id_entrega; }
    public void setId_entrega(int id_entrega) { this.id_entrega = id_entrega; }

    public int getId_venta() { return id_venta; }
    public void setId_venta(int id_venta) { this.id_venta = id_venta; }

    public Timestamp getFecha_entrega() { return fecha_entrega; }
    public void setFecha_entrega(Timestamp fecha_entrega) { this.fecha_entrega = fecha_entrega; }

    public String getMedio_transporte() { return medio_transporte; }
    public void setMedio_transporte(String medio_transporte) { this.medio_transporte = medio_transporte; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public String getDireccion_envio() { return direccion_envio; }
    public void setDireccion_envio(String direccion_envio) { this.direccion_envio = direccion_envio; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}
