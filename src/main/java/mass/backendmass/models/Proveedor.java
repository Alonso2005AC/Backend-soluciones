package mass.backendmass.models;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "proveedores")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_proveedor;

    private String nombre_proveedor;
    private String telefono;
    private String correo;
    private String direccion;

    @Column(nullable = false)
    private Timestamp fecha_registro;

    @PrePersist
    protected void onCreate() {
        this.fecha_registro = new Timestamp(System.currentTimeMillis());
    }

    // Getters y Setters
    public int getId_proveedor() { return id_proveedor; }
    public void setId_proveedor(int id_proveedor) { this.id_proveedor = id_proveedor; }

    public String getNombre_proveedor() { return nombre_proveedor; }
    public void setNombre_proveedor(String nombre_proveedor) { this.nombre_proveedor = nombre_proveedor; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public Timestamp getFecha_registro() { return fecha_registro; }
    public void setFecha_registro(Timestamp fecha_registro) { this.fecha_registro = fecha_registro; }
}
