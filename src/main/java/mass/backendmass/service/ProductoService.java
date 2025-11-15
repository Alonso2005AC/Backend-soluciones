package mass.backendmass.service;

import mass.backendmass.models.Producto;
import mass.backendmass.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    // Listar solo productos activos
    public List<Producto> listarProductos(Integer categoria, Double precioMin, Double precioMax) {
        if (categoria != null && precioMin != null && precioMax != null)
            return productoRepository.findByIdCategoriaAndPrecioBetweenAndActivoTrue(categoria, precioMin, precioMax);
        if (categoria != null)
            return productoRepository.findByIdCategoriaAndActivoTrue(categoria);
        if (precioMin != null && precioMax != null)
            return productoRepository.findByPrecioBetweenAndActivoTrue(precioMin, precioMax);
        return productoRepository.findByActivoTrue();
    }

    public Optional<Producto> obtenerPorId(int id) {
        return productoRepository.findById(id);
    }

    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public Producto actualizarProducto(int id, Producto producto) {
        producto.setId_producto(id);
        return productoRepository.save(producto);
    }

    // Eliminación lógica (soft delete)
    @Transactional
    public boolean eliminarProducto(int id) {
        if (productoRepository.existsById(id)) {
            productoRepository.softDelete(id);
            return true;
        }
        return false;
    }
}
