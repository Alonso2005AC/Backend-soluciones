package mass.backendmass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import mass.backendmass.dto.DetalleVentaDTO;
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

    // Endpoint de prueba POST para validar JSON
    @PostMapping("/test-json")
    public ResponseEntity<?> testJsonEndpoint(@RequestBody VentaRequest ventaRequest) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "ok");
        response.put("mensaje", "JSON recibido correctamente");
        response.put("id_cliente", ventaRequest.getId_cliente());
        response.put("metodo_pago_recibido", ventaRequest.getMetodo_pago());
        response.put("metodo_pago_es_null", ventaRequest.getMetodo_pago() == null);
        response.put("metodo_pago_length", ventaRequest.getMetodo_pago() != null ? ventaRequest.getMetodo_pago().length() : 0);
        response.put("total", ventaRequest.getTotal());
        response.put("tipo_comprobante", ventaRequest.getTipo_comprobante());
        response.put("cantidad_detalles", ventaRequest.getDetalles() != null ? ventaRequest.getDetalles().size() : 0);
        
        System.out.println("\n=== TEST JSON ===");
        System.out.println("metodo_pago: '" + ventaRequest.getMetodo_pago() + "'");
        System.out.println("metodo_pago es 'tarjeta': " + "tarjeta".equals(ventaRequest.getMetodo_pago()));
        System.out.println("metodo_pago es 'agora': " + "agora".equals(ventaRequest.getMetodo_pago()));
        System.out.println("================\n");
        
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
            // Log detallado para debugging
            System.out.println("\n=== REQUEST RECIBIDO ===");
            System.out.println("ID Cliente: " + ventaRequest.getId_cliente());
            System.out.println("Método de pago RAW: '" + ventaRequest.getMetodo_pago() + "'");
            System.out.println("Método de pago LENGTH: " + (ventaRequest.getMetodo_pago() != null ? ventaRequest.getMetodo_pago().length() : "null"));
            System.out.println("Método de pago BYTES: " + (ventaRequest.getMetodo_pago() != null ? java.util.Arrays.toString(ventaRequest.getMetodo_pago().getBytes()) : "null"));
            System.out.println("Total: " + ventaRequest.getTotal());
            System.out.println("Tipo comprobante: " + ventaRequest.getTipo_comprobante());
            System.out.println("Datos fiscales: " + ventaRequest.getDatos_fiscales());
            System.out.println("Detalles: " + (ventaRequest.getDetalles() != null ? ventaRequest.getDetalles().size() : "null"));
            if (ventaRequest.getDetalles() != null) {
                for (int i = 0; i < ventaRequest.getDetalles().size(); i++) {
                    DetalleVentaDTO det = ventaRequest.getDetalles().get(i);
                    System.out.println("  Detalle " + i + ": Producto=" + det.getId_producto() + ", Cantidad=" + det.getCantidad() + ", Precio=" + det.getPrecio_unitario());
                }
            }
            System.out.println("========================\n");
            
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

    // Obtener ventas de un cliente específico
    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<?> getVentasPorCliente(@PathVariable int idCliente) {
        try {
            ArrayList<Venta> ventas = ventaService.obtenerVentasPorCliente(idCliente);
            return ResponseEntity.ok(ventas);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("mensaje", "Error al obtener ventas: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    // Obtener factura por ID de venta
    @GetMapping("/factura/{idVenta}")
    public ResponseEntity<?> getFacturaPorVenta(@PathVariable int idVenta) {
        try {
            Optional<?> factura = ventaService.obtenerFacturaPorVenta(idVenta);
            if (factura.isPresent()) {
                return ResponseEntity.ok(factura.get());
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("mensaje", "No se encontró factura para la venta con ID: " + idVenta);
                return ResponseEntity.status(404).body(errorResponse);
            }
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("mensaje", "Error al obtener factura: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
