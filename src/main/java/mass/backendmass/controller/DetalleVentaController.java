package mass.backendmass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Optional;

import mass.backendmass.models.DetalleVenta;
import mass.backendmass.service.DetalleVentaService;

@RestController
@RequestMapping("/api/detalle-venta")
@CrossOrigin(origins = "*")
public class DetalleVentaController {

    @Autowired
    private DetalleVentaService detalleVentaService;

    // Obtener todos los detalles
    @GetMapping
    public ArrayList<DetalleVenta> getDetalleVentas() {
        return detalleVentaService.listaDetalleVenta();
    }

    // Obtener detalle por ID
    @GetMapping("/{id}")
    public Optional<DetalleVenta> getDetallePorId(@PathVariable int id) {
        return detalleVentaService.obtenerPorId(id);
    }

    // Guardar nuevo detalle
    @PostMapping
    public DetalleVenta guardarDetalle(@RequestBody DetalleVenta detalle) {
        return detalleVentaService.guardarDetalle(detalle);
    }

    // Actualizar detalle
    @PutMapping("/{id}")
    public DetalleVenta actualizarDetalle(@PathVariable int id, @RequestBody DetalleVenta detalle) {
        return detalleVentaService.actualizarDetalle(id, detalle);
    }

    // Eliminar detalle
    @DeleteMapping("/{id}")
    public String eliminarDetalle(@PathVariable int id) {
        boolean eliminado = detalleVentaService.eliminarDetalle(id);
        if (eliminado) {
            return "Detalle de venta con ID " + id + " fue eliminado correctamente.";
        } else {
            return "No se pudo eliminar el detalle de venta con ID " + id;
        }
    }
}
    