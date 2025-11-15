package mass.backendmass.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mass.backendmass.models.Inventario;
import mass.backendmass.repository.InventarioRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    // Obtener todos los movimientos
    public ArrayList<Inventario> listaMovimientos() {
        return (ArrayList<Inventario>) inventarioRepository.findAll();
    }

    // Guardar movimiento
    public Inventario guardarMovimiento(Inventario movimiento) {
        return inventarioRepository.save(movimiento);
    }

    // Obtener movimiento por ID
    public Optional<Inventario> obtenerPorId(int id) {
        return inventarioRepository.findById(id);
    }

    // Actualizar movimiento
    public Inventario actualizarMovimiento(int id, Inventario movimientoActualizado) {
        return inventarioRepository.findById(id).map(m -> {
            m.setId_producto(movimientoActualizado.getId_producto());
            m.setId_almacen(movimientoActualizado.getId_almacen());
            m.setTipo_movimiento(movimientoActualizado.getTipo_movimiento());
            m.setReferencia_tipo(movimientoActualizado.getReferencia_tipo());
            m.setReferencia_id(movimientoActualizado.getReferencia_id());
            m.setCantidad(movimientoActualizado.getCantidad());
            m.setFecha_movimiento(movimientoActualizado.getFecha_movimiento());
            m.setObservacion(movimientoActualizado.getObservacion());
            return inventarioRepository.save(m);
        }).orElse(null);
    }

    // Eliminar movimiento por ID
    public boolean eliminarMovimiento(int id) {
        if (inventarioRepository.existsById(id)) {
            inventarioRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
