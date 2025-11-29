package mass.backendmass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import mass.backendmass.models.Venta;
import mass.backendmass.dto.ResumenModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {
    @Query("SELECT v FROM Venta v WHERE v.id_cliente = :idCliente ORDER BY v.fecha_venta DESC")
    Optional<Venta> findUltimaVentaPorCliente(@Param("idCliente") int idCliente);
    
    @Query("SELECT v FROM Venta v WHERE v.id_cliente = :idCliente ORDER BY v.fecha_venta DESC")
    List<Venta> findByIdCliente(@Param("idCliente") int idCliente);
    
    // Dashboard queries
    @Query(value = "SELECT CONVERT(YEAR(fecha_venta), CHAR) as nombre, COUNT(1) as valor FROM ventas GROUP BY CONVERT(YEAR(fecha_venta), CHAR)", 
           nativeQuery = true)
    ArrayList<ResumenModel> resumenVentasAnio();
    
    @Query(value = "SELECT CONVERT(MONTH(fecha_venta), CHAR) as nombre, COUNT(1) as valor FROM ventas WHERE YEAR(fecha_venta) = :anio GROUP BY CONVERT(MONTH(fecha_venta), CHAR)", 
           nativeQuery = true)
    ArrayList<ResumenModel> resumenVentasMes(@Param("anio") int anio);
    
    @Query(value = "SELECT * FROM ventas WHERE YEAR(fecha_venta) = :anio AND MONTH(fecha_venta) = :mes", 
           nativeQuery = true)
    ArrayList<Venta> listaVentasAnioMes(@Param("anio") int anio, @Param("mes") int mes);
    
    @Query(value = "SELECT CONVERT(YEAR(fecha_venta), CHAR) as nombre, SUM(total) as valor FROM ventas GROUP BY CONVERT(YEAR(fecha_venta), CHAR)", 
           nativeQuery = true)
    ArrayList<ResumenModel> resumenTotalVentasAnio();
    
    @Query(value = "SELECT CONVERT(MONTH(fecha_venta), CHAR) as nombre, SUM(total) as valor FROM ventas WHERE YEAR(fecha_venta) = :anio GROUP BY CONVERT(MONTH(fecha_venta), CHAR)", 
           nativeQuery = true)
    ArrayList<ResumenModel> resumenTotalVentasMes(@Param("anio") int anio);
    
    // Categoría más vendida
    @Query(value = "SELECT c.nombre_categoria as nombre, SUM(dv.cantidad) as valor " +
                   "FROM detalle_venta dv " +
                   "INNER JOIN productos p ON dv.id_producto = p.id_producto " +
                   "INNER JOIN categorias c ON p.id_categoria = c.id_categoria " +
                   "GROUP BY c.id_categoria, c.nombre_categoria " +
                   "ORDER BY valor DESC", 
           nativeQuery = true)
    ArrayList<ResumenModel> categoriaMasVendida();
    
    // Forma de pago más usada
    @Query(value = "SELECT metodo_pago as nombre, COUNT(*) as valor " +
                   "FROM ventas " +
                   "WHERE metodo_pago IS NOT NULL " +
                   "GROUP BY metodo_pago " +
                   "ORDER BY valor DESC", 
           nativeQuery = true)
    ArrayList<ResumenModel> formaPagoMasUsada();
}
