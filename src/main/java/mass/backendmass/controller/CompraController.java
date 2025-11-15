package mass.backendmass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Optional;

import mass.backendmass.models.Compra;
import mass.backendmass.service.CompraService;

@RestController
@RequestMapping("/api/compras")
@CrossOrigin(origins = "*")
public class CompraController {

    @Autowired
    private CompraService compraService;

    // Obtener todas las compras
    @GetMapping
    public ArrayList<Compra> getCompras() {
        return compraService.listaCompras();
    }

    // Obtener compra por ID
    @GetMapping("/{id}")
    public Optional<Compra> getCompraPorId(@PathVariable int id) {
        return compraService.obtenerPorId(id);
    }

    // Guardar nueva compra
    @PostMapping
    public Compra guardarCompra(@RequestBody Compra compra) {
        return compraService.guardarCompra(compra);
    }

    // Actualizar compra
    @PutMapping("/{id}")
    public Compra actualizarCompra(@PathVariable int id, @RequestBody Compra compra) {
        return compraService.actualizarCompra(id, compra);
    }

    // Eliminar compra
    @DeleteMapping("/{id}")
    public String eliminarCompra(@PathVariable int id) {
        boolean eliminado = compraService.eliminarCompra(id);
        if (eliminado) {
            return "Compra con ID " + id + " fue eliminada correctamente.";
        } else {
            return "No se pudo eliminar la compra con ID " + id;
        }
    }
}
