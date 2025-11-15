package mass.backendmass.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mass.backendmass.models.Almacen;
import mass.backendmass.repository.AlmacenRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class AlmacenService {

    @Autowired
    private AlmacenRepository almacenRepository;

    // Obtener todos los almacenes
    public ArrayList<Almacen> listaAlmacenes() {
        return (ArrayList<Almacen>) almacenRepository.findAll();
    }

    // Guardar almacén
    public Almacen guardarAlmacen(Almacen almacen) {
        return almacenRepository.save(almacen);
    }

    // Obtener almacén por ID
    public Optional<Almacen> obtenerPorId(int id) {
        return almacenRepository.findById(id);
    }

    // Cambiar solo referencia de responsable_id por id_usuario en métodos de actualización
    public Almacen actualizarAlmacen(int id, Almacen almacenActualizado) {
    return almacenRepository.findById(id).map(a -> {
        a.setNombre_almacen(almacenActualizado.getNombre_almacen());
        a.setUbicacion(almacenActualizado.getUbicacion());
        a.setId_usuario(almacenActualizado.getId_usuario());
        a.setDescripcion(almacenActualizado.getDescripcion());
        return almacenRepository.save(a);
    }).orElse(null);
}


    // Eliminar almacén por ID
    public boolean eliminarAlmacen(int id) {
        if (almacenRepository.existsById(id)) {
            almacenRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
