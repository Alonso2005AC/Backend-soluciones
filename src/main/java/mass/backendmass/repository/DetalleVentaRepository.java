package mass.backendmass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import mass.backendmass.models.DetalleVenta;
import java.util.List;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Integer> {
    @Query("SELECT d FROM DetalleVenta d WHERE d.id_venta = :idVenta")
    List<DetalleVenta> findByIdVenta(@Param("idVenta") int idVenta);
    
    @Query("SELECT COALESCE(SUM(d.cantidad), 0) FROM DetalleVenta d WHERE d.id_producto = :idProducto")
    Long countVentasByProductoId(@Param("idProducto") int idProducto);
}
