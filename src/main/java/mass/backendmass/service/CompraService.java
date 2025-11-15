package mass.backendmass.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mass.backendmass.models.Compra;
import mass.backendmass.repository.CompraRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CompraService {

    @Autowired
    private CompraRepository compraRepository;

    // Obtener todas las compras
    public ArrayList<Compra> listaCompras() {
        return (ArrayList<Compra>) compraRepository.findAll();
    }

    // Guardar compra
    public Compra guardarCompra(Compra compra) {
        return compraRepository.save(compra);
    }

    // Obtener compra por ID
    public Optional<Compra> obtenerPorId(int id) {
        return compraRepository.findById(id);
    }

    // Actualizar compra
    public Compra actualizarCompra(int id, Compra compraActualizada) {
        return compraRepository.findById(id).map(c -> {
            c.setId_proveedor(compraActualizada.getId_proveedor());
            c.setId_usuario(compraActualizada.getId_usuario());
            c.setFecha_compra(compraActualizada.getFecha_compra());
            c.setTotal(compraActualizada.getTotal());
            c.setEstado(compraActualizada.getEstado());
            c.setObservacion(compraActualizada.getObservacion());
            return compraRepository.save(c);
        }).orElse(null);
    }

    // Eliminar compra por ID
    public boolean eliminarCompra(int id) {
        if (compraRepository.existsById(id)) {
            compraRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
