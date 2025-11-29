package mass.backendmass.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mass.backendmass.models.Factura;
import mass.backendmass.models.Venta;
import mass.backendmass.models.DetalleVenta;
import mass.backendmass.models.Cliente;
import mass.backendmass.models.Producto;
import mass.backendmass.repository.FacturaRepository;
import mass.backendmass.repository.VentaRepository;
import mass.backendmass.repository.DetalleVentaRepository;
import mass.backendmass.repository.ClienteRepository;
import mass.backendmass.repository.ProductoRepository;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProductoRepository productoRepository;

    // Obtener todas las facturas
    public ArrayList<Factura> listaFacturas() {
        return (ArrayList<Factura>) facturaRepository.findAll();
    }

    // Guardar factura
    public Factura guardarFactura(Factura factura) {
        return facturaRepository.save(factura);
    }

    // Obtener factura por ID
    public Optional<Factura> obtenerPorId(int id) {
        return facturaRepository.findById(id);
    }

    // Actualizar factura
    public Factura actualizarFactura(int id, Factura facturaActualizada) {
        return facturaRepository.findById(id).map(f -> {
            f.setId_venta(facturaActualizada.getId_venta());
            f.setNumero_factura(facturaActualizada.getNumero_factura());
            f.setFecha_emision(facturaActualizada.getFecha_emision());
            f.setTipo_comprobante(facturaActualizada.getTipo_comprobante());
            f.setTotal(facturaActualizada.getTotal());
            f.setEstado(facturaActualizada.getEstado());
            f.setDatos_fiscales(facturaActualizada.getDatos_fiscales());
            return facturaRepository.save(f);
        }).orElse(null);
    }

    // Eliminar factura por ID
    public boolean eliminarFactura(int id) {
        if (facturaRepository.existsById(id)) {
            facturaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Obtener última factura de un cliente
    public Optional<Factura> obtenerUltimaFacturaCliente(int idCliente) {
        Optional<Venta> ultimaVenta = ventaRepository.findUltimaVentaPorCliente(idCliente);
        if (ultimaVenta.isPresent()) {
            return facturaRepository.findByIdVenta(ultimaVenta.get().getId_venta());
        }
        return Optional.empty();
    }

    // Generar PDF de la factura
    public byte[] generarPdfFactura(int id) throws Exception {
        System.out.println("=== INICIANDO GENERACIÓN DE PDF ===");
        System.out.println("ID Factura: " + id);
        
        Optional<Factura> facturaOpt = facturaRepository.findById(id);
        if (!facturaOpt.isPresent()) {
            System.err.println("ERROR: Factura no encontrada con ID: " + id);
            throw new Exception("Factura no encontrada");
        }
        System.out.println("✓ Factura encontrada");

        Factura factura = facturaOpt.get();
        Optional<Venta> ventaOpt = ventaRepository.findById(factura.getId_venta());
        if (!ventaOpt.isPresent()) {
            System.err.println("ERROR: Venta no encontrada con ID: " + factura.getId_venta());
            throw new Exception("Venta no encontrada");
        }
        System.out.println("✓ Venta encontrada");

        Venta venta = ventaOpt.get();
        List<DetalleVenta> detalles = detalleVentaRepository.findByIdVenta(venta.getId_venta());
        System.out.println("✓ Detalles encontrados: " + detalles.size());

        System.out.println("Creando ByteArrayOutputStream...");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        System.out.println("Creando PdfWriter...");
        PdfWriter writer = new PdfWriter(baos);
        
        System.out.println("Creando PdfDocument...");
        PdfDocument pdfDoc = new PdfDocument(writer);
        
        System.out.println("Creando Document...");
        Document document = new Document(pdfDoc);
        
        // Colores personalizados
        DeviceRgb colorPrimario = new DeviceRgb(41, 128, 185);  // Azul
        DeviceRgb colorSecundario = new DeviceRgb(52, 73, 94);  // Azul oscuro
        DeviceRgb colorFondo = new DeviceRgb(236, 240, 241);    // Gris claro

        System.out.println("Agregando contenido...");
        
        // ========== ENCABEZADO ==========
        // Título principal
        Paragraph titulo = new Paragraph("TIENDA MASS")
                .setFontSize(24)
                .setBold()
                .setFontColor(colorPrimario)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(5);
        document.add(titulo);
        
        // Subtítulo
        String tipoDoc = factura.getTipo_comprobante().toString().toUpperCase();
        Paragraph subtitulo = new Paragraph(tipoDoc)
                .setFontSize(18)
                .setBold()
                .setFontColor(colorSecundario)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20);
        document.add(subtitulo);

        // ========== INFORMACIÓN DE LA FACTURA ==========
        Table infoTable = new Table(2);
        infoTable.setWidth(UnitValue.createPercentValue(100));
        infoTable.setMarginBottom(20);
        
        // Columna izquierda
        Cell leftCell = new Cell()
                .setBorder(null)
                .add(new Paragraph("INFORMACIÓN DEL COMPROBANTE")
                        .setBold()
                        .setFontSize(12)
                        .setFontColor(colorSecundario)
                        .setMarginBottom(10))
                .add(new Paragraph("N° Comprobante: " + factura.getNumero_factura())
                        .setFontSize(10))
                .add(new Paragraph("Fecha: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(factura.getFecha_emision()))
                        .setFontSize(10))
                .add(new Paragraph("Estado: " + factura.getEstado().toString().toUpperCase())
                        .setFontSize(10)
                        .setFontColor(factura.getEstado().toString().equals("emitida") ? 
                            new DeviceRgb(39, 174, 96) : new DeviceRgb(231, 76, 60)));
        
        // Columna derecha - Información del cliente
        Cell rightCell = new Cell().setBorder(null);
        if (venta.getId_cliente() != null) {
            Optional<Cliente> clienteOpt = clienteRepository.findById(venta.getId_cliente());
            if (clienteOpt.isPresent()) {
                Cliente cliente = clienteOpt.get();
                rightCell.add(new Paragraph("DATOS DEL CLIENTE")
                        .setBold()
                        .setFontSize(12)
                        .setFontColor(colorSecundario)
                        .setMarginBottom(10))
                    .add(new Paragraph("Cliente: " + cliente.getNombre() + " " + cliente.getApellido())
                        .setFontSize(10));
                if (cliente.getTelefono() != null) {
                    rightCell.add(new Paragraph("Teléfono: " + cliente.getTelefono())
                        .setFontSize(10));
                }
                if (cliente.getCorreo() != null) {
                    rightCell.add(new Paragraph("Correo: " + cliente.getCorreo())
                        .setFontSize(10));
                }
            }
        }
        
        if (factura.getDatos_fiscales() != null && !factura.getDatos_fiscales().isEmpty()) {
            rightCell.add(new Paragraph("Datos Fiscales: " + factura.getDatos_fiscales())
                .setFontSize(10)
                .setMarginTop(5));
        }
        
        infoTable.addCell(leftCell);
        infoTable.addCell(rightCell);
        document.add(infoTable);

        // ========== TABLA DE PRODUCTOS ==========
        document.add(new Paragraph("DETALLE DE PRODUCTOS")
                .setBold()
                .setFontSize(12)
                .setFontColor(colorSecundario)
                .setMarginBottom(10));
        
        Table table = new Table(UnitValue.createPercentArray(new float[]{4, 1, 2, 2}));
        table.setWidth(UnitValue.createPercentValue(100));

        // Headers con estilo
        table.addHeaderCell(new Cell()
                .add(new Paragraph("Producto").setBold().setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(colorPrimario)
                .setTextAlignment(TextAlignment.LEFT)
                .setPadding(8));
        table.addHeaderCell(new Cell()
                .add(new Paragraph("Cant.").setBold().setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(colorPrimario)
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(8));
        table.addHeaderCell(new Cell()
                .add(new Paragraph("Precio Unit.").setBold().setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(colorPrimario)
                .setTextAlignment(TextAlignment.RIGHT)
                .setPadding(8));
        table.addHeaderCell(new Cell()
                .add(new Paragraph("Subtotal").setBold().setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(colorPrimario)
                .setTextAlignment(TextAlignment.RIGHT)
                .setPadding(8));

        // Detalles con filas alternadas
        boolean alternate = false;
        for (DetalleVenta detalle : detalles) {
            String nombreProducto = "Producto ID: " + detalle.getId_producto();
            Optional<Producto> productoOpt = productoRepository.findById(detalle.getId_producto());
            if (productoOpt.isPresent()) {
                nombreProducto = productoOpt.get().getNombre();
            }
            
            Cell cellProducto = new Cell()
                    .add(new Paragraph(nombreProducto).setFontSize(10))
                    .setPadding(6)
                    .setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 0.5f));
            Cell cellCantidad = new Cell()
                    .add(new Paragraph(String.valueOf(detalle.getCantidad())).setFontSize(10))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setPadding(6)
                    .setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 0.5f));
            Cell cellPrecio = new Cell()
                    .add(new Paragraph("S/ " + String.format("%.2f", detalle.getPrecio_unitario())).setFontSize(10))
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setPadding(6)
                    .setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 0.5f));
            Cell cellSubtotal = new Cell()
                    .add(new Paragraph("S/ " + String.format("%.2f", detalle.getSubtotal())).setFontSize(10))
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setPadding(6)
                    .setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 0.5f));
            
            if (alternate) {
                cellProducto.setBackgroundColor(colorFondo);
                cellCantidad.setBackgroundColor(colorFondo);
                cellPrecio.setBackgroundColor(colorFondo);
                cellSubtotal.setBackgroundColor(colorFondo);
            }
            
            table.addCell(cellProducto);
            table.addCell(cellCantidad);
            table.addCell(cellPrecio);
            table.addCell(cellSubtotal);
            
            alternate = !alternate;
        }

        document.add(table);
        document.add(new Paragraph("\n"));

        // ========== TOTAL ==========
        Table totalTable = new Table(2);
        totalTable.setWidth(UnitValue.createPercentValue(100));
        
        totalTable.addCell(new Cell()
                .add(new Paragraph(""))
                .setBorder(null));
        
        totalTable.addCell(new Cell()
                .add(new Paragraph("TOTAL: S/ " + String.format("%.2f", factura.getTotal()))
                        .setBold()
                        .setFontSize(18)
                        .setFontColor(colorPrimario)
                        .setTextAlignment(TextAlignment.RIGHT))
                .setBorder(null)
                .setBackgroundColor(colorFondo)
                .setPadding(10));
        
        document.add(totalTable);
        
        // ========== PIE DE PÁGINA ==========
        document.add(new Paragraph("\n\n"));
        document.add(new Paragraph("Gracias por su preferencia")
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.GRAY)
                .setItalic());
        document.add(new Paragraph("Tienda Mass - Sistema de Gestión")
                .setFontSize(8)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.GRAY));

        System.out.println("Cerrando documento...");
        document.close();
        
        byte[] pdfBytes = baos.toByteArray();
        System.out.println("✓ PDF generado correctamente. Tamaño: " + pdfBytes.length + " bytes");
        System.out.println("=== FIN GENERACIÓN DE PDF ===\n");
        
        return pdfBytes;
    }
}
