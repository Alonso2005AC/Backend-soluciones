package mass.backendmass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Optional;

import mass.backendmass.models.Cliente;
import mass.backendmass.service.ClienteService;

@RestController
@CrossOrigin(origins = "*")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // Obtener todos los clientes (ambas rutas)
    @GetMapping({"/api/clientes", "/api/auth/clientes"})
    public ArrayList<Cliente> getClientes() {
        return clienteService.listaClientes();
    }

    // Obtener cliente por ID (ambas rutas)
    @GetMapping({"/api/clientes/{id}", "/api/auth/clientes/{id}"})
    public Optional<Cliente> getClientePorId(@PathVariable int id) {
        return clienteService.obtenerPorId(id);
    }

    // Guardar nuevo cliente (ambas rutas)
    @PostMapping({"/api/clientes", "/api/auth/clientes"})
    public Cliente guardarCliente(@RequestBody Cliente cliente) {
        return clienteService.guardarCliente(cliente);
    }

    // Actualizar cliente (ambas rutas)
    @PutMapping({"/api/clientes/{id}", "/api/auth/clientes/{id}"})
    public Cliente actualizarCliente(@PathVariable int id, @RequestBody Cliente cliente) {
        return clienteService.actualizarCliente(id, cliente);
    }

    // Eliminar cliente (ambas rutas)
    @DeleteMapping({"/api/clientes/{id}", "/api/auth/clientes/{id}"})
    public String eliminarCliente(@PathVariable int id) {
        boolean eliminado = clienteService.eliminarCliente(id);
        if (eliminado) {
            return "Cliente con ID " + id + " fue eliminado correctamente.";
        } else {
            return "No se pudo eliminar el cliente con ID " + id;
        }
    }
}
    