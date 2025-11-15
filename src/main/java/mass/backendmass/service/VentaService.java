package mass.backendmass.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mass.backendmass.dto.DetalleVentaDTO;
import mass.backendmass.dto.VentaRequest;
import mass.backendmass.models.DetalleVenta;
import mass.backendmass.models.Factura;
import mass.backendmass.models.Producto;
import mass.backendmass.models.Venta;
import mass.backendmass.repository.DetalleVentaRepository;
import mass.backendmass.repository.FacturaRepository;
import mass.backendmass.repository.ProductoRepository;
import mass.backendmass.repository.VentaRepository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;
    
    @Autowired
    private DetalleVentaRepository detalleVentaRepository;
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private FacturaRepository facturaRepository;

    // Obtener todas las ventas
    public ArrayList<Venta> listaVentas() {
        return (ArrayList<Venta>) ventaRepository.findAll();
    }

    // Guardar una venta
    public Venta guardarVenta(Venta venta) {
        return ventaRepository.save(venta);
    }

    // Obtener venta por ID
    public Optional<Venta> obtenerPorId(int id) {
        return ventaRepository.findById(id);
    }

    // Actualizar venta
    public Venta actualizarVenta(int id, Venta ventaActualizada) {
        return ventaRepository.findById(id).map(v -> {
            v.setId_cliente(ventaActualizada.getId_cliente());
            v.setId_usuario(ventaActualizada.getId_usuario());
            v.setTotal(ventaActualizada.getTotal());
            v.setEstado(ventaActualizada.getEstado());
            v.setObservacion(ventaActualizada.getObservacion());
            return ventaRepository.save(v);
        }).orElse(null);
    }

    // Eliminar venta por ID
    public boolean eliminarVenta(int id) {
        if (ventaRepository.existsById(id)) {
            ventaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Registrar venta completa con detalles
    @Transactional
    public Map<String, Object> registrarVentaCompleta(VentaRequest request) {
        Map<String, Object> response = new HashMap<>();
        
        System.out.println("\n=== VALIDACIÓN EN SERVICE ===");
        System.out.println("metodo_pago recibido: '" + request.getMetodo_pago() + "'");
        System.out.println("metodo_pago es null: " + (request.getMetodo_pago() == null));
        System.out.println("metodo_pago isEmpty: " + (request.getMetodo_pago() != null && request.getMetodo_pago().isEmpty()));
        
        // Validar método de pago
        String metodoPago = request.getMetodo_pago();
        if (metodoPago == null) {
            System.out.println("ERROR: metodo_pago es NULL");
            throw new IllegalArgumentException("Método de pago es obligatorio (null)");
        }
        
        if (metodoPago.trim().isEmpty()) {
            System.out.println("ERROR: metodo_pago está vacío");
            throw new IllegalArgumentException("Método de pago es obligatorio (vacío)");
        }
        
        metodoPago = metodoPago.trim().toLowerCase();
        System.out.println("metodo_pago normalizado: '" + metodoPago + "'");
        
        if (!metodoPago.equals("tarjeta") && !metodoPago.equals("agora")) {
            System.out.println("ERROR: metodo_pago inválido: '" + metodoPago + "'");
            System.out.println("  - Comparación con 'tarjeta': " + metodoPago.equals("tarjeta"));
            System.out.println("  - Comparación con 'agora': " + metodoPago.equals("agora"));
            throw new IllegalArgumentException("Método de pago inválido. Solo se acepta 'tarjeta' o 'agora'. Recibido: '" + metodoPago + "'");
        }
        
        System.out.println("✓ Validación de metodo_pago exitosa");
        System.out.println("=============================\n");
        
        // 1. Crear y guardar la venta
        Venta venta = new Venta();
        venta.setId_cliente(request.getId_cliente());
        venta.setTotal(BigDecimal.valueOf(request.getTotal()));
        venta.setEstado(Venta.Estado.completada);
        venta.setMetodo_pago(Venta.MetodoPago.valueOf(metodoPago));
        venta.setFecha_venta(new Timestamp(System.currentTimeMillis()));
        
        Venta ventaGuardada = ventaRepository.save(venta);
        System.out.println("Venta guardada con ID: " + ventaGuardada.getId_venta());

        // 2. Guardar los detalles de venta y actualizar stock
        for (DetalleVentaDTO detalleDTO : request.getDetalles()) {
            // Validar que el producto existe
            Optional<Producto> productoOpt = productoRepository.findById(detalleDTO.getId_producto());
            if (!productoOpt.isPresent()) {
                throw new RuntimeException("Producto con ID " + detalleDTO.getId_producto() + " no encontrado");
            }
            
            Producto producto = productoOpt.get();
            
            // Validar stock suficiente
            if (producto.getStock() < detalleDTO.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre() + 
                    ". Stock disponible: " + producto.getStock() + ", solicitado: " + detalleDTO.getCantidad());
            }
            
            // Crear detalle
            DetalleVenta detalle = new DetalleVenta();
            detalle.setId_venta(ventaGuardada.getId_venta());
            detalle.setId_producto(detalleDTO.getId_producto());
            detalle.setCantidad(detalleDTO.getCantidad());
            detalle.setPrecio_unitario(BigDecimal.valueOf(detalleDTO.getPrecio_unitario()));
            detalle.setSubtotal(BigDecimal.valueOf(detalleDTO.getSubtotal()));
            detalle.setLote(detalleDTO.getLote());
            detalle.setFecha_vencimiento(detalleDTO.getFecha_vencimiento());
            
            detalleVentaRepository.save(detalle);
            System.out.println("Detalle guardado para producto ID: " + detalleDTO.getId_producto());

            // Actualizar stock del producto
            int nuevoStock = producto.getStock() - detalleDTO.getCantidad();
            producto.setStock(nuevoStock);
            productoRepository.save(producto);
            System.out.println("Stock actualizado para producto: " + producto.getNombre() + ", nuevo stock: " + nuevoStock);
        }

        // 3. Crear factura si se requiere
        if (request.getTipo_comprobante() != null && !request.getTipo_comprobante().isEmpty()) {
            Factura factura = new Factura();
            factura.setId_venta(ventaGuardada.getId_venta());
            factura.setNumero_factura(generarNumeroFactura());
            factura.setFecha_emision(new Timestamp(System.currentTimeMillis()));
            factura.setTipo_comprobante(Factura.TipoComprobante.valueOf(request.getTipo_comprobante()));
            factura.setTotal(BigDecimal.valueOf(request.getTotal()));
            factura.setEstado(Factura.Estado.emitida);
            factura.setDatos_fiscales(request.getDatos_fiscales());
            
            Factura facturaGuardada = facturaRepository.save(factura);
            response.put("factura", facturaGuardada);
            System.out.println("Factura generada con número: " + facturaGuardada.getNumero_factura());
        }

        response.put("success", true);
        response.put("venta", ventaGuardada);
        response.put("mensaje", "Venta registrada exitosamente");
        
        return response;
    }

    // Generar número de factura único
    private String generarNumeroFactura() {
        long timestamp = System.currentTimeMillis();
        return "F-" + timestamp;
    }

    // Obtener ventas de un cliente específico
    public ArrayList<Venta> obtenerVentasPorCliente(int idCliente) {
        return (ArrayList<Venta>) ventaRepository.findByIdCliente(idCliente);
    }

    // Obtener factura por ID de venta
    public Optional<Factura> obtenerFacturaPorVenta(int idVenta) {
        return facturaRepository.findByIdVenta(idVenta);
    }
}
