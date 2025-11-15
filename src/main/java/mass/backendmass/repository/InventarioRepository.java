package mass.backendmass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import mass.backendmass.models.Inventario;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Integer> {
}
