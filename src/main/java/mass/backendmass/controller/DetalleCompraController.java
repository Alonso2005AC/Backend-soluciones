package mass.backendmass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Optional;

import mass.backendmass.models.DetalleCompra;
import mass.backendmass.service.DetalleCompraService;

@RestController
@RequestMapping("/api/detalle-compras")
@CrossOrigin(origins = "*")
public class DetalleCompraController {

    @Autowired
    private DetalleCompraService detalleCompraService;

    // Obtener todos los detalles de compras
    @GetMapping
    public ArrayList<DetalleCompra> getDetalleCompras() {
        return detalleCompraService.listaDetalleCompras();
    }

    // Obtener detalle por ID
    @GetMapping("/{id}")
    public Optional<DetalleCompra> getDetallePorId(@PathVariable int id) {
        return detalleCompraService.obtenerPorId(id);
    }

    // Guardar nuevo detalle
    @PostMapping
    public DetalleCompra guardarDetalle(@RequestBody DetalleCompra detalle) {
        return detalleCompraService.guardarDetalle(detalle);
    }

    // Actualizar detalle
    @PutMapping("/{id}")
    public DetalleCompra actualizarDetalle(@PathVariable int id, @RequestBody DetalleCompra detalle) {
        return detalleCompraService.actualizarDetalle(id, detalle);
    }

    // Eliminar detalle
    @DeleteMapping("/{id}")
    public String eliminarDetalle(@PathVariable int id) {
        boolean eliminado = detalleCompraService.eliminarDetalle(id);
        if (eliminado) {
            return "Detalle de compra con ID " + id + " fue eliminado correctamente.";
        } else {
            return "No se pudo eliminar el detalle de compra con ID " + id;
        }
    }
}
