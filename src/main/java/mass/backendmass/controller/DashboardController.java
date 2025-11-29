package mass.backendmass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mass.backendmass.repository.VentaRepository;
import mass.backendmass.repository.CompraRepository;
import mass.backendmass.repository.DetalleVentaRepository;
import mass.backendmass.repository.ProductoRepository;
import mass.backendmass.dto.ResumenModel;
import mass.backendmass.models.Venta;
import mass.backendmass.models.Compra;
import mass.backendmass.models.Producto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private CompraRepository compraRepository;
    
    @Autowired
    private DetalleVentaRepository detalleVentaRepository;
    
    @Autowired
    private ProductoRepository productoRepository;

    // ========== ENDPOINTS DE VENTAS ==========
    
    @GetMapping("/ventas/resumen-anio")
    public ResponseEntity<ArrayList<ResumenModel>> getResumenVentasAnio() {
        ArrayList<ResumenModel> resumen = ventaRepository.resumenVentasAnio();
        return ResponseEntity.ok(resumen);
    }

    @GetMapping("/ventas/resumen-mes/{anio}")
    public ResponseEntity<ArrayList<ResumenModel>> getResumenVentasMes(@PathVariable int anio) {
        ArrayList<ResumenModel> resumen = ventaRepository.resumenVentasMes(anio);
        return ResponseEntity.ok(resumen);
    }

    @GetMapping("/ventas/lista/{anio}/{mes}")
    public ResponseEntity<ArrayList<Venta>> getListaVentasAnioMes(
            @PathVariable int anio, 
            @PathVariable int mes) {
        ArrayList<Venta> ventas = ventaRepository.listaVentasAnioMes(anio, mes);
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/ventas/total-anio")
    public ResponseEntity<ArrayList<ResumenModel>> getResumenTotalVentasAnio() {
        ArrayList<ResumenModel> resumen = ventaRepository.resumenTotalVentasAnio();
        return ResponseEntity.ok(resumen);
    }

    @GetMapping("/ventas/total-mes/{anio}")
    public ResponseEntity<ArrayList<ResumenModel>> getResumenTotalVentasMes(@PathVariable int anio) {
        ArrayList<ResumenModel> resumen = ventaRepository.resumenTotalVentasMes(anio);
        return ResponseEntity.ok(resumen);
    }

    // ========== ENDPOINTS DE COMPRAS ==========
    
    @GetMapping("/compras/resumen-anio")
    public ResponseEntity<ArrayList<ResumenModel>> getResumenComprasAnio() {
        ArrayList<ResumenModel> resumen = compraRepository.resumenComprasAnio();
        return ResponseEntity.ok(resumen);
    }

    @GetMapping("/compras/resumen-mes/{anio}")
    public ResponseEntity<ArrayList<ResumenModel>> getResumenComprasMes(@PathVariable int anio) {
        ArrayList<ResumenModel> resumen = compraRepository.resumenComprasMes(anio);
        return ResponseEntity.ok(resumen);
    }

    @GetMapping("/compras/lista/{anio}/{mes}")
    public ResponseEntity<ArrayList<Compra>> getListaComprasAnioMes(
            @PathVariable int anio, 
            @PathVariable int mes) {
        ArrayList<Compra> compras = compraRepository.listaComprasAnioMes(anio, mes);
        return ResponseEntity.ok(compras);
    }

    @GetMapping("/compras/total-anio")
    public ResponseEntity<ArrayList<ResumenModel>> getResumenTotalComprasAnio() {
        ArrayList<ResumenModel> resumen = compraRepository.resumenTotalComprasAnio();
        return ResponseEntity.ok(resumen);
    }

    @GetMapping("/compras/total-mes/{anio}")
    public ResponseEntity<ArrayList<ResumenModel>> getResumenTotalComprasMes(@PathVariable int anio) {
        ArrayList<ResumenModel> resumen = compraRepository.resumenTotalComprasMes(anio);
        return ResponseEntity.ok(resumen);
    }

    // ========== ENDPOINT DE RESUMEN GENERAL ==========
    
    @GetMapping("/resumen-general")
    public ResponseEntity<Map<String, Object>> getResumenGeneral() {
        Map<String, Object> resumen = new HashMap<>();
        
        // Totales generales
        long totalVentas = ventaRepository.count();
        long totalCompras = compraRepository.count();
        
        resumen.put("totalVentas", totalVentas);
        resumen.put("totalCompras", totalCompras);
        resumen.put("ventasPorAnio", ventaRepository.resumenVentasAnio());
        resumen.put("comprasPorAnio", compraRepository.resumenComprasAnio());
        
        return ResponseEntity.ok(resumen);
    }
    
    // ========== ENDPOINTS DE DASHBOARDS ADICIONALES ==========
    
    @GetMapping("/categoria-mas-vendida")
    public ResponseEntity<ArrayList<ResumenModel>> getCategoriaMasVendida() {
        ArrayList<ResumenModel> resumen = ventaRepository.categoriaMasVendida();
        return ResponseEntity.ok(resumen);
    }
    
    @GetMapping("/producto-mas-vendido-categoria/{idCategoria}")
    public ResponseEntity<ArrayList<ResumenModel>> getProductoMasVendidoPorCategoria(@PathVariable int idCategoria) {
        ArrayList<ResumenModel> resumen = detalleVentaRepository.productoMasVendidoPorCategoria(idCategoria);
        return ResponseEntity.ok(resumen);
    }
    
    @GetMapping("/forma-pago-mas-usada")
    public ResponseEntity<ArrayList<ResumenModel>> getFormaPagoMasUsada() {
        ArrayList<ResumenModel> resumen = ventaRepository.formaPagoMasUsada();
        return ResponseEntity.ok(resumen);
    }
    
    @GetMapping("/inventario-serial-ideal")
    public ResponseEntity<Map<String, Object>> getInventarioSerialIdeal() {
        Map<String, Object> dashboard = new HashMap<>();
        List<Producto> productos = productoRepository.findByActivoTrue();

        dashboard.put("total_productos", productos.size());
        dashboard.put("valor_total_inventario", productos.stream().mapToDouble(p -> p.getPrecio() * p.getStock()).sum());
        dashboard.put("stock_bajo", productos.stream().filter(p -> p.getStock() > 0 && p.getStock() < 10).collect(Collectors.toList()));
        dashboard.put("agotados", productos.stream().filter(p -> p.getStock() == 0).collect(Collectors.toList()));
        dashboard.put("stock_por_categoria", productos.stream().collect(Collectors.groupingBy(Producto::getCategoria, Collectors.summingInt(Producto::getStock))));
        dashboard.put("alertas_vencimiento", productos.stream().filter(p -> p.getFecha_vencimiento() != null && p.getFecha_vencimiento().toLocalDate().isBefore(java.time.LocalDate.now().plusDays(30))).collect(Collectors.toList()));
        // Puedes agregar más indicadores aquí
        return ResponseEntity.ok(dashboard);
    }
}
