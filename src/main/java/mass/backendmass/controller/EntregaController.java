package mass.backendmass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Optional;

import mass.backendmass.models.Entrega;
import mass.backendmass.service.EntregaService;

@RestController
@RequestMapping("/api/entregas")
@CrossOrigin(origins = "*")
public class EntregaController {

    @Autowired
    private EntregaService entregaService;

    // Obtener todas las entregas
    @GetMapping
    public ArrayList<Entrega> getEntregas() {
        return entregaService.listaEntregas();
    }

    // Obtener entrega por ID
    @GetMapping("/{id}")
    public Optional<Entrega> getEntregaPorId(@PathVariable int id) {
        return entregaService.obtenerPorId(id);
    }

    // Guardar nueva entrega
    @PostMapping
    public Entrega guardarEntrega(@RequestBody Entrega entrega) {
        return entregaService.guardarEntrega(entrega);
    }

    // Actualizar entrega
    @PutMapping("/{id}")
    public Entrega actualizarEntrega(@PathVariable int id, @RequestBody Entrega entrega) {
        return entregaService.actualizarEntrega(id, entrega);
    }

    // Eliminar entrega
    @DeleteMapping("/{id}")
    public String eliminarEntrega(@PathVariable int id) {
        boolean eliminado = entregaService.eliminarEntrega(id);
        if (eliminado) {
            return "Entrega con ID " + id + " fue eliminada correctamente.";
        } else {
            return "No se pudo eliminar la entrega con ID " + id;
        }
    }
}
