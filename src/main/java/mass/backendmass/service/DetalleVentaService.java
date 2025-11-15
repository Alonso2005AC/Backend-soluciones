package mass.backendmass.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mass.backendmass.models.DetalleVenta;
import mass.backendmass.repository.DetalleVentaRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class DetalleVentaService {

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    // Obtener todos los detalles
    public ArrayList<DetalleVenta> listaDetalleVenta() {
        return (ArrayList<DetalleVenta>) detalleVentaRepository.findAll();
    }

    // Guardar detalle
    public DetalleVenta guardarDetalle(DetalleVenta detalle) {
        return detalleVentaRepository.save(detalle);
    }

    // Obtener detalle por ID
    public Optional<DetalleVenta> obtenerPorId(int id) {
        return detalleVentaRepository.findById(id);
    }

    // Actualizar detalle
    public DetalleVenta actualizarDetalle(int id, DetalleVenta detalleActualizado) {
        return detalleVentaRepository.findById(id).map(d -> {
            d.setId_venta(detalleActualizado.getId_venta());
            d.setId_producto(detalleActualizado.getId_producto());
            d.setCantidad(detalleActualizado.getCantidad());
            d.setPrecio_unitario(detalleActualizado.getPrecio_unitario());
            d.setSubtotal(detalleActualizado.getSubtotal());
            d.setLote(detalleActualizado.getLote());
            return detalleVentaRepository.save(d);
        }).orElse(null);
    }

    // Eliminar detalle
    public boolean eliminarDetalle(int id) {
        if (detalleVentaRepository.existsById(id)) {
            detalleVentaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
