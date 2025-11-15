package mass.backendmass.models;

import jakarta.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_producto;

    private String codigo_barras;
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;
    @Column(name = "id_categoria")
    private int idCategoria;
    @Column(name = "id_proveedor")
    private int idProveedor;
    private Date fecha_ingreso;
    private Date fecha_vencimiento;
    private String lote;
    
    @Column(nullable = true)
    private String categoria;
    
    @Column(name = "imagen")
    private String imagen;

    @Column(nullable = false)
    private Timestamp fecha_registro;

    // Se ejecuta automáticamente antes de insertar el registro
    @PrePersist
    protected void onCreate() {
        this.fecha_registro = new Timestamp(System.currentTimeMillis());
    }

    // Getters y Setters
    public int getId_producto() { return id_producto; }
    public void setId_producto(int id_producto) { this.id_producto = id_producto; }

    public String getCodigo_barras() { return codigo_barras; }
    public void setCodigo_barras(String codigo_barras) { this.codigo_barras = codigo_barras; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public int getId_categoria() { return idCategoria; }
    public void setId_categoria(int id_categoria) { this.idCategoria = id_categoria; }

    public int getId_proveedor() { return idProveedor; }
    public void setId_proveedor(int id_proveedor) { this.idProveedor = id_proveedor; }

    public Date getFecha_ingreso() { return fecha_ingreso; }
    public void setFecha_ingreso(Date fecha_ingreso) { this.fecha_ingreso = fecha_ingreso; }

    public Date getFecha_vencimiento() { return fecha_vencimiento; }
    public void setFecha_vencimiento(Date fecha_vencimiento) { this.fecha_vencimiento = fecha_vencimiento; }

    public String getLote() { return lote; }
    public void setLote(String lote) { this.lote = lote; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public Timestamp getFecha_registro() { return fecha_registro; }
    public void setFecha_registro(Timestamp fecha_registro) { this.fecha_registro = fecha_registro; }
}
