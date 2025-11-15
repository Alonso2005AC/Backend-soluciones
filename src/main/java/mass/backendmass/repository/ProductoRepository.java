package mass.backendmass.repository;

import mass.backendmass.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    // Buscar solo productos activos
    List<Producto> findByActivoTrue();
    
    List<Producto> findByIdCategoriaAndActivoTrue(int idCategoria);
    List<Producto> findByPrecioBetweenAndActivoTrue(double min, double max);
    List<Producto> findByIdCategoriaAndPrecioBetweenAndActivoTrue(int idCategoria, double min, double max);
    
    // Eliminación lógica
    @Modifying
    @Query("UPDATE Producto p SET p.activo = false WHERE p.id_producto = :id")
    void softDelete(@Param("id") int id);
}
