package mass.backendmass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import mass.backendmass.models.Factura;
import mass.backendmass.service.FacturaService;

@RestController
@RequestMapping("/api/facturas")
@CrossOrigin(origins = "*")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    // Obtener todas las facturas
    @GetMapping
    public ArrayList<Factura> getFacturas() {
        return facturaService.listaFacturas();
    }

    // Obtener factura por ID
    @GetMapping("/{id}")
    public Optional<Factura> getFacturaPorId(@PathVariable int id) {
        return facturaService.obtenerPorId(id);
    }

    // Guardar nueva factura
    @PostMapping
    public Factura guardarFactura(@RequestBody Factura factura) {
        return facturaService.guardarFactura(factura);
    }

    // Actualizar factura
    @PutMapping("/{id}")
    public Factura actualizarFactura(@PathVariable int id, @RequestBody Factura factura) {
        return facturaService.actualizarFactura(id, factura);
    }

    // Eliminar factura
    @DeleteMapping("/{id}")
    public String eliminarFactura(@PathVariable int id) {
        boolean eliminado = facturaService.eliminarFactura(id);
        if (eliminado) {
            return "Factura con ID " + id + " fue eliminada correctamente.";
        } else {
            return "No se pudo eliminar la factura con ID " + id;
        }
    }

    // Obtener última factura de un cliente
    @GetMapping("/cliente/{idCliente}/ultima")
    public ResponseEntity<?> obtenerUltimaFacturaCliente(@PathVariable int idCliente) {
        Optional<Factura> factura = facturaService.obtenerUltimaFacturaCliente(idCliente);
        if (factura.isPresent()) {
            return ResponseEntity.ok(factura.get());
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "No se encontró factura para el cliente");
            return ResponseEntity.status(404).body(response);
        }
    }

    // Generar PDF de factura
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> generarPdfFactura(@PathVariable int id) {
        try {
            byte[] pdfBytes = facturaService.generarPdfFactura(id);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "factura-" + id + ".pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error generando PDF: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
