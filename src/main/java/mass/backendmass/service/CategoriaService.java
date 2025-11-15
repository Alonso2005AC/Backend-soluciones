package mass.backendmass.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mass.backendmass.models.Categoria;
import mass.backendmass.repository.CategoriaRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Obtener todas las categorias
    public ArrayList<Categoria> listaCategorias() {
        return (ArrayList<Categoria>) categoriaRepository.findAll();
    }

    // Guardar nueva categoria
    public Categoria guardarCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    // Obtener categoria por ID
    public Optional<Categoria> obtenerPorId(int id) {
        return categoriaRepository.findById(id);
    }

    // Actualizar categoria
    public Categoria actualizarCategoria(int id, Categoria categoriaActualizada) {
        return categoriaRepository.findById(id).map(c -> {
            c.setNombre_categoria(categoriaActualizada.getNombre_categoria());
            c.setDescripcion(categoriaActualizada.getDescripcion());
            return categoriaRepository.save(c);
        }).orElse(null);
    }

    // Eliminar categoria por ID
    public boolean eliminarCategoria(int id) {
        if (categoriaRepository.existsById(id)) {
            categoriaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
