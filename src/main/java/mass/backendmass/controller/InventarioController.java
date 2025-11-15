package mass.backendmass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Optional;

import mass.backendmass.models.Inventario;
import mass.backendmass.service.InventarioService;

@RestController
@RequestMapping("/api/inventario")
@CrossOrigin(origins = "*")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    // Obtener todos los movimientos
    @GetMapping
    public ArrayList<Inventario> getMovimientos() {
        return inventarioService.listaMovimientos();
    }

    // Obtener movimiento por ID
    @GetMapping("/{id}")
    public Optional<Inventario> getMovimientoPorId(@PathVariable int id) {
        return inventarioService.obtenerPorId(id);
    }

    // Guardar nuevo movimiento
    @PostMapping
    public Inventario guardarMovimiento(@RequestBody Inventario movimiento) {
        return inventarioService.guardarMovimiento(movimiento);
    }

    // Actualizar movimiento
    @PutMapping("/{id}")
    public Inventario actualizarMovimiento(@PathVariable int id, @RequestBody Inventario movimiento) {
        return inventarioService.actualizarMovimiento(id, movimiento);
    }

    // Eliminar movimiento
    @DeleteMapping("/{id}")
    public String eliminarMovimiento(@PathVariable int id) {
        boolean eliminado = inventarioService.eliminarMovimiento(id);
        if (eliminado) {
            return "Movimiento de inventario con ID " + id + " fue eliminado correctamente.";
        } else {
            return "No se pudo eliminar el movimiento con ID " + id;
        }
    }
}
    