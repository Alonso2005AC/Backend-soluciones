package mass.backendmass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import mass.backendmass.models.Compra;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Integer> {
}
