package mass.backendmass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import mass.backendmass.models.Venta;
import java.util.List;
import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {
    @Query("SELECT v FROM Venta v WHERE v.id_cliente = :idCliente ORDER BY v.fecha_venta DESC")
    Optional<Venta> findUltimaVentaPorCliente(@Param("idCliente") int idCliente);
    
    @Query("SELECT v FROM Venta v WHERE v.id_cliente = :idCliente ORDER BY v.fecha_venta DESC")
    List<Venta> findByIdCliente(@Param("idCliente") int idCliente);
}
