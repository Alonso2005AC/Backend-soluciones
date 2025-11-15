package mass.backendmass.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mass.backendmass.models.Proveedor;
import mass.backendmass.repository.ProveedorRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    // Obtener todos los proveedores
    public ArrayList<Proveedor> listaProveedores() {
        return (ArrayList<Proveedor>) proveedorRepository.findAll();
    }

    // Guardar un proveedor
    public Proveedor guardarProveedor(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    // Obtener proveedor por ID
    public Optional<Proveedor> obtenerPorId(int id) {
        return proveedorRepository.findById(id);
    }

    // Actualizar proveedor
    public Proveedor actualizarProveedor(int id, Proveedor proveedorActualizado) {
        return proveedorRepository.findById(id).map(p -> {
            p.setNombre_proveedor(proveedorActualizado.getNombre_proveedor());
            p.setTelefono(proveedorActualizado.getTelefono());
            p.setCorreo(proveedorActualizado.getCorreo());
            p.setDireccion(proveedorActualizado.getDireccion());
            return proveedorRepository.save(p);
        }).orElse(null);
    }

    // Eliminar proveedor por ID
    public boolean eliminarProveedor(int id) {
        if (proveedorRepository.existsById(id)) {
            proveedorRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
