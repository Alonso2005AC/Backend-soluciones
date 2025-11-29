package mass.backendmass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import mass.backendmass.models.Compra;
import mass.backendmass.dto.ResumenModel;
import java.util.ArrayList;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Integer> {
    
    // Dashboard queries
    @Query(value = "SELECT CONVERT(YEAR(fecha_compra), CHAR) as nombre, COUNT(1) as valor FROM compras GROUP BY CONVERT(YEAR(fecha_compra), CHAR)", 
           nativeQuery = true)
    ArrayList<ResumenModel> resumenComprasAnio();
    
    @Query(value = "SELECT CONVERT(MONTH(fecha_compra), CHAR) as nombre, COUNT(1) as valor FROM compras WHERE YEAR(fecha_compra) = :anio GROUP BY CONVERT(MONTH(fecha_compra), CHAR)", 
           nativeQuery = true)
    ArrayList<ResumenModel> resumenComprasMes(@Param("anio") int anio);
    
    @Query(value = "SELECT * FROM compras WHERE YEAR(fecha_compra) = :anio AND MONTH(fecha_compra) = :mes", 
           nativeQuery = true)
    ArrayList<Compra> listaComprasAnioMes(@Param("anio") int anio, @Param("mes") int mes);
    
    @Query(value = "SELECT CONVERT(YEAR(fecha_compra), CHAR) as nombre, SUM(total) as valor FROM compras GROUP BY CONVERT(YEAR(fecha_compra), CHAR)", 
           nativeQuery = true)
    ArrayList<ResumenModel> resumenTotalComprasAnio();
    
    @Query(value = "SELECT CONVERT(MONTH(fecha_compra), CHAR) as nombre, SUM(total) as valor FROM compras WHERE YEAR(fecha_compra) = :anio GROUP BY CONVERT(MONTH(fecha_compra), CHAR)", 
           nativeQuery = true)
    ArrayList<ResumenModel> resumenTotalComprasMes(@Param("anio") int anio);
}
