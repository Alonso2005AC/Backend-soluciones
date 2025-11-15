package mass.backendmass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Optional;

import mass.backendmass.models.Categoria;
import mass.backendmass.service.CategoriaService;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    // Obtener todas las categorias
    @GetMapping
    public ArrayList<Categoria> getCategorias() {
        return categoriaService.listaCategorias();
    }

    // Obtener categoria por ID
    @GetMapping("/{id}")
    public Optional<Categoria> getCategoriaPorId(@PathVariable int id) {
        return categoriaService.obtenerPorId(id);
    }

    // Guardar nueva categoria
    @PostMapping
    public Categoria guardarCategoria(@RequestBody Categoria categoria) {
        return categoriaService.guardarCategoria(categoria);
    }

    // Actualizar categoria
    @PutMapping("/{id}")
    public Categoria actualizarCategoria(@PathVariable int id, @RequestBody Categoria categoria) {
        return categoriaService.actualizarCategoria(id, categoria);
    }

    // Eliminar categoria
    @DeleteMapping("/{id}")
    public String eliminarCategoria(@PathVariable int id) {
        boolean eliminado = categoriaService.eliminarCategoria(id);
        if (eliminado) {
            return "Categoría con ID " + id + " fue eliminada correctamente.";
        } else {
            return "No se pudo eliminar la categoría con ID " + id;
        }
    }
}
