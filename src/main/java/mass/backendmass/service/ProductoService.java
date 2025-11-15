package mass.backendmass.service;

import mass.backendmass.models.Producto;
import mass.backendmass.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listarProductos(Integer categoria, Double precioMin, Double precioMax) {
        if (categoria != null && precioMin != null && precioMax != null)
            return productoRepository.findByIdCategoriaAndPrecioBetween(categoria, precioMin, precioMax);
        if (categoria != null)
            return productoRepository.findByIdCategoria(categoria);
        if (precioMin != null && precioMax != null)
            return productoRepository.findByPrecioBetween(precioMin, precioMax);
        return productoRepository.findAll();
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

    public boolean eliminarProducto(int id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
