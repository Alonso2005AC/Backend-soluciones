package mass.backendmass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Optional;

import mass.backendmass.models.Almacen;
import mass.backendmass.service.AlmacenService;

@RestController
@RequestMapping("/api/almacenes")
@CrossOrigin(origins = "*")
public class AlmacenController {

    @Autowired
    private AlmacenService almacenService;

    // Obtener todos los almacenes
    @GetMapping
    public ArrayList<Almacen> getAlmacenes() {
        return almacenService.listaAlmacenes();
    }

    // Obtener almacén por ID
    @GetMapping("/{id}")
    public Optional<Almacen> getAlmacenPorId(@PathVariable int id) {
        return almacenService.obtenerPorId(id);
    }

    // Guardar nuevo almacén
    @PostMapping
    public Almacen guardarAlmacen(@RequestBody Almacen almacen) {
        return almacenService.guardarAlmacen(almacen);
    }

    // Actualizar almacén
    @PutMapping("/{id}")
    public Almacen actualizarAlmacen(@PathVariable int id, @RequestBody Almacen almacen) {
        return almacenService.actualizarAlmacen(id, almacen);
    }

    // Eliminar almacén
    @DeleteMapping("/{id}")
    public String eliminarAlmacen(@PathVariable int id) {
        boolean eliminado = almacenService.eliminarAlmacen(id);
        if (eliminado) {
            return "Almacén con ID " + id + " fue eliminado correctamente.";
        } else {
            return "No se pudo eliminar el almacén con ID " + id;
        }
    }
}
