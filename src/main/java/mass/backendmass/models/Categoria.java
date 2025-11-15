package mass.backendmass.models;

import jakarta.persistence.*;

@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_categoria;

    private String nombre_categoria;
    private String descripcion;

    // Getters y Setters
    public int getId_categoria() { return id_categoria; }
    public void setId_categoria(int id_categoria) { this.id_categoria = id_categoria; }

    public String getNombre_categoria() { return nombre_categoria; }
    public void setNombre_categoria(String nombre_categoria) { this.nombre_categoria = nombre_categoria; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

}
