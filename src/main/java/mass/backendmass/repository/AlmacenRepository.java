package mass.backendmass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import mass.backendmass.models.Almacen;

@Repository
public interface AlmacenRepository extends JpaRepository<Almacen, Integer> {
}
