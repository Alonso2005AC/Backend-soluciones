package mass.backendmass.repository;

import mass.backendmass.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findByIdCategoria(int idCategoria);
    List<Producto> findByPrecioBetween(double min, double max);
    List<Producto> findByIdCategoriaAndPrecioBetween(int idCategoria, double min, double max);
}
