package mass.backendmass.models;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "almacenes")
public class Almacen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_almacen;

    private String nombre_almacen;
    private String ubicacion;
    private Integer id_usuario; // usuario responsable
    private String descripcion;

    @Column(nullable = false)
    private Timestamp fecha_apertura;

    @PrePersist
    protected void onCreate() {
        this.fecha_apertura = new Timestamp(System.currentTimeMillis());
    }

    // Getters y Setters
    public int getId_almacen() { return id_almacen; }
    public void setId_almacen(int id_almacen) { this.id_almacen = id_almacen; }

    public String getNombre_almacen() { return nombre_almacen; }
    public void setNombre_almacen(String nombre_almacen) { this.nombre_almacen = nombre_almacen; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public Integer getId_usuario() { return id_usuario; }
    public void setId_usuario(Integer id_usuario) { this.id_usuario = id_usuario; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Timestamp getFecha_apertura() { return fecha_apertura; }
    public void setFecha_apertura(Timestamp fecha_apertura) { this.fecha_apertura = fecha_apertura; }
}
