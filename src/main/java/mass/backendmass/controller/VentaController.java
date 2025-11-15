package mass.backendmass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import mass.backendmass.dto.VentaRequest;
import mass.backendmass.models.Venta;
import mass.backendmass.service.VentaService;

@RestController
@RequestMapping("/api/ventas")
@CrossOrigin(origins = "*")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    // Endpoint de prueba
    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> testEndpoint() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "ok");
        response.put("mensaje", "Endpoint de ventas funcionando correctamente");
        return ResponseEntity.ok(response);
    }

    // Obtener todas las ventas
    @GetMapping
    public ArrayList<Venta> getVentas() {
        return ventaService.listaVentas();
    }

    // Obtener venta por ID
    @GetMapping("/{id}")
    public Optional<Venta> getVentaPorId(@PathVariable int id) {
        return ventaService.obtenerPorId(id);
    }

    // Guardar nueva venta
    @PostMapping
    public Venta guardarVenta(@RequestBody Venta venta) {
        return ventaService.guardarVenta(venta);
    }

    // Registrar venta completa con detalles
    @PostMapping("/registrar-venta")
    public ResponseEntity<?> registrarVentaCompleta(@RequestBody VentaRequest ventaRequest) {
        try {
            // Log para debugging
            System.out.println("=== REQUEST RECIBIDO ===");
            System.out.println("ID Cliente: " + ventaRequest.getId_cliente());
            System.out.println("Método de pago: '" + ventaRequest.getMetodo_pago() + "'");
            System.out.println("Total: " + ventaRequest.getTotal());
            System.out.println("Tipo comprobante: " + ventaRequest.getTipo_comprobante());
            System.out.println("Datos fiscales: " + ventaRequest.getDatos_fiscales());
            System.out.println("Detalles: " + (ventaRequest.getDetalles() != null ? ventaRequest.getDetalles().size() : "null"));
            System.out.println("========================");
            
            Map<String, Object> response = ventaService.registrarVentaCompleta(ventaRequest);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("mensaje", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("mensaje", "Error al procesar la venta: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("mensaje", "Error inesperado: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    // Actualizar venta
    @PutMapping("/{id}")
    public Venta actualizarVenta(@PathVariable int id, @RequestBody Venta venta) {
        return ventaService.actualizarVenta(id, venta);
    }

    // Eliminar venta
    @DeleteMapping("/{id}")
    public String eliminarVenta(@PathVariable int id) {
        boolean eliminado = ventaService.eliminarVenta(id);
        if (eliminado) {
            return "Venta con ID " + id + " fue eliminada correctamente.";
        } else {
            return "No se pudo eliminar la venta con ID " + id;
        }
    }
}
