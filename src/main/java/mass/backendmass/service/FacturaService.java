package mass.backendmass.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mass.backendmass.models.Factura;
import mass.backendmass.models.Venta;
import mass.backendmass.models.DetalleVenta;
import mass.backendmass.models.Cliente;
import mass.backendmass.repository.FacturaRepository;
import mass.backendmass.repository.VentaRepository;
import mass.backendmass.repository.DetalleVentaRepository;
import mass.backendmass.repository.ClienteRepository;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

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
        Optional<Factura> facturaOpt = facturaRepository.findById(id);
        if (!facturaOpt.isPresent()) {
            throw new Exception("Factura no encontrada");
        }

        Factura factura = facturaOpt.get();
        Optional<Venta> ventaOpt = ventaRepository.findById(factura.getId_venta());
        if (!ventaOpt.isPresent()) {
            throw new Exception("Venta no encontrada");
        }

        Venta venta = ventaOpt.get();
        List<DetalleVenta> detalles = detalleVentaRepository.findByIdVenta(venta.getId_venta());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // Título
        Paragraph titulo = new Paragraph("FACTURA / BOLETA")
                .setFontSize(20)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER);
        document.add(titulo);

        // Información de la factura
        document.add(new Paragraph("Número: " + factura.getNumero_factura()));
        document.add(new Paragraph("Tipo: " + factura.getTipo_comprobante()));
        document.add(new Paragraph("Fecha: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(factura.getFecha_emision())));
        document.add(new Paragraph("Estado: " + factura.getEstado()));
        
        if (factura.getDatos_fiscales() != null) {
            document.add(new Paragraph("Datos Fiscales: " + factura.getDatos_fiscales()));
        }
        
        document.add(new Paragraph("\n"));

        // Información del cliente
        if (venta.getId_cliente() != null) {
            Optional<Cliente> clienteOpt = clienteRepository.findById(venta.getId_cliente());
            if (clienteOpt.isPresent()) {
                Cliente cliente = clienteOpt.get();
                document.add(new Paragraph("Cliente: " + cliente.getNombre() + " " + cliente.getApellido()));
                if (cliente.getTelefono() != null) {
                    document.add(new Paragraph("Teléfono: " + cliente.getTelefono()));
                }
            }
        }

        document.add(new Paragraph("\n"));

        // Tabla de productos
        Table table = new Table(UnitValue.createPercentArray(new float[]{3, 1, 2, 2}));
        table.setWidth(UnitValue.createPercentValue(100));

        // Headers
        table.addHeaderCell("Producto");
        table.addHeaderCell("Cantidad");
        table.addHeaderCell("Precio Unit.");
        table.addHeaderCell("Subtotal");

        // Detalles
        for (DetalleVenta detalle : detalles) {
            table.addCell("Producto ID: " + detalle.getId_producto());
            table.addCell(String.valueOf(detalle.getCantidad()));
            table.addCell("S/ " + detalle.getPrecio_unitario());
            table.addCell("S/ " + detalle.getSubtotal());
        }

        document.add(table);
        document.add(new Paragraph("\n"));

        // Total
        Paragraph total = new Paragraph("TOTAL: S/ " + factura.getTotal())
                .setFontSize(16)
                .setBold()
                .setTextAlignment(TextAlignment.RIGHT);
        document.add(total);

        document.close();
        return baos.toByteArray();
    }
}
