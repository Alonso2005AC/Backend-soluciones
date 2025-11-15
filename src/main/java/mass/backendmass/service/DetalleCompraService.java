package mass.backendmass.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mass.backendmass.models.DetalleCompra;
import mass.backendmass.repository.DetalleCompraRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class DetalleCompraService {

    @Autowired
    private DetalleCompraRepository detalleCompraRepository;

    // Obtener todos los detalles de compras
    public ArrayList<DetalleCompra> listaDetalleCompras() {
        return (ArrayList<DetalleCompra>) detalleCompraRepository.findAll();
    }

    // Guardar detalle de compra
    public DetalleCompra guardarDetalle(DetalleCompra detalle) {
        return detalleCompraRepository.save(detalle);
    }

    // Obtener detalle por ID
    public Optional<DetalleCompra> obtenerPorId(int id) {
        return detalleCompraRepository.findById(id);
    }

    // Actualizar detalle
    public DetalleCompra actualizarDetalle(int id, DetalleCompra detalleActualizado) {
        return detalleCompraRepository.findById(id).map(d -> {
            d.setId_compra(detalleActualizado.getId_compra());
            d.setId_producto(detalleActualizado.getId_producto());
            d.setCantidad(detalleActualizado.getCantidad());
            d.setPrecio_unitario(detalleActualizado.getPrecio_unitario());
            d.setSubtotal(detalleActualizado.getSubtotal());
            d.setLote(detalleActualizado.getLote());
            d.setFecha_vencimiento(detalleActualizado.getFecha_vencimiento());
            return detalleCompraRepository.save(d);
        }).orElse(null);
    }

    // Eliminar detalle por ID
    public boolean eliminarDetalle(int id) {
        if (detalleCompraRepository.existsById(id)) {
            detalleCompraRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
