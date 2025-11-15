package mass.backendmass.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mass.backendmass.models.Entrega;
import mass.backendmass.repository.EntregaRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class EntregaService {

    @Autowired
    private EntregaRepository entregaRepository;

    // Obtener todas las entregas
    public ArrayList<Entrega> listaEntregas() {
        return (ArrayList<Entrega>) entregaRepository.findAll();
    }

    // Guardar entrega
    public Entrega guardarEntrega(Entrega entrega) {
        return entregaRepository.save(entrega);
    }

    // Obtener entrega por ID
    public Optional<Entrega> obtenerPorId(int id) {
        return entregaRepository.findById(id);
    }

    // Actualizar entrega
    public Entrega actualizarEntrega(int id, Entrega entregaActualizada) {
        return entregaRepository.findById(id).map(e -> {
            e.setId_venta(entregaActualizada.getId_venta());
            e.setFecha_entrega(entregaActualizada.getFecha_entrega());
            e.setMedio_transporte(entregaActualizada.getMedio_transporte());
            e.setEstado(entregaActualizada.getEstado());
            e.setDireccion_envio(entregaActualizada.getDireccion_envio());
            e.setObservacion(entregaActualizada.getObservacion());
            return entregaRepository.save(e);
        }).orElse(null);
    }

    // Eliminar entrega por ID
    public boolean eliminarEntrega(int id) {
        if (entregaRepository.existsById(id)) {
            entregaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
