package mass.backendmass.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_usuario;

    private String nombre;
    private String apellido;

    @Column(unique = true)
    private String correo;

    @JsonProperty("contraseña")
    private String contrasena;

    @Enumerated(EnumType.STRING)
    private Rol rol = Rol.cajero; // valor por defecto

    @Column(nullable = false)
    private Timestamp fecha_registro;

    public enum Rol {
        administrador,
        cajero,
        almacenista,
        cliente
    }

    @PrePersist
    protected void onCreate() {
        this.fecha_registro = new Timestamp(System.currentTimeMillis());
    }

    // Getters y Setters
    public int getId_usuario() { return id_usuario; }
    public void setId_usuario(int id_usuario) { this.id_usuario = id_usuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public Timestamp getFecha_registro() { return fecha_registro; }
    public void setFecha_registro(Timestamp fecha_registro) { this.fecha_registro = fecha_registro; }
}
