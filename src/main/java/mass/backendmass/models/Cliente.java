package mass.backendmass.models;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_cliente;

    private String nombre;
    private String apellido;
    private String correo;
    
    @Column(name = "contrasena", nullable = false)
    private String contraseña;
    
    private String telefono;
    private String direccion;

    @Column(nullable = false)
    private Timestamp fecha_registro;

    @Column(name = "rol")
    private String rol;

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    @PrePersist
    protected void onCreate() {
        this.fecha_registro = new Timestamp(System.currentTimeMillis());
    }

    // Getters y Setters
    public int getId_cliente() { return id_cliente; }
    public void setId_cliente(int id_cliente) { this.id_cliente = id_cliente; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContraseña() { return contraseña; }
    public void setContraseña(String contraseña) { this.contraseña = contraseña; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public Timestamp getFecha_registro() { return fecha_registro; }
    public void setFecha_registro(Timestamp fecha_registro) { this.fecha_registro = fecha_registro; }
}
