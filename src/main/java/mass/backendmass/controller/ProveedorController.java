package mass.backendmass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Optional;

import mass.backendmass.models.Proveedor;
import mass.backendmass.service.ProveedorService;

@RestController
@RequestMapping("/api/proveedores")
@CrossOrigin(origins = "*")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    // Obtener todos los proveedores
    @GetMapping
    public ArrayList<Proveedor> getProveedores() {
        return proveedorService.listaProveedores();
    }

    // Obtener proveedor por ID
    @GetMapping("/{id}")
    public Optional<Proveedor> getProveedorPorId(@PathVariable int id) {
        return proveedorService.obtenerPorId(id);
    }

    // Guardar nuevo proveedor
    @PostMapping
    public Proveedor guardarProveedor(@RequestBody Proveedor proveedor) {
        return proveedorService.guardarProveedor(proveedor);
    }

    // Actualizar proveedor
    @PutMapping("/{id}")
    public Proveedor actualizarProveedor(@PathVariable int id, @RequestBody Proveedor proveedor) {
        return proveedorService.actualizarProveedor(id, proveedor);
    }

    // Eliminar proveedor
    @DeleteMapping("/{id}")
    public String eliminarProveedor(@PathVariable int id) {
        boolean eliminado = proveedorService.eliminarProveedor(id);
        if (eliminado) {
            return "Proveedor con ID " + id + " fue eliminado correctamente.";
        } else {
            return "No se pudo eliminar el proveedor con ID " + id;
        }
    }
}
