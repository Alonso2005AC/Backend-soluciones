package mass.backendmass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import mass.backendmass.models.Factura;
import java.util.Optional;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Integer> {
    @Query("SELECT f FROM Factura f WHERE f.id_venta = :idVenta")
    Optional<Factura> findByIdVenta(@Param("idVenta") int idVenta);
}
