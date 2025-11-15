package mass.backendmass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
    public ResponseEntity<?> guardarCliente(@RequestBody Cliente cliente) {
        try {
            // Verificar si el correo ya existe
            Cliente existente = clienteService.buscarPorCorreo(cliente.getCorreo());
            if (existente != null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("mensaje", "El correo electrónico ya está registrado");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            Cliente nuevoCliente = clienteService.guardarCliente(cliente);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("cliente", nuevoCliente);
            response.put("mensaje", "Cliente registrado correctamente");
            return ResponseEntity.ok(response);
        } catch (DataIntegrityViolationException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("mensaje", "El correo electrónico ya está registrado");
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("mensaje", "Error al registrar cliente: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    // Actualizar cliente (ambas rutas)
    @PutMapping({"/api/clientes/{id}", "/api/auth/clientes/{id}"})
    public ResponseEntity<?> actualizarCliente(@PathVariable int id, @RequestBody Cliente cliente) {
        try {
            // Verificar que el cliente existe
            Optional<Cliente> clienteExistente = clienteService.obtenerPorId(id);
            if (!clienteExistente.isPresent()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("mensaje", "Cliente no encontrado");
                return ResponseEntity.status(404).body(errorResponse);
            }
            
            // Verificar si el correo está siendo usado por otro cliente
            Cliente otroCliente = clienteService.buscarPorCorreo(cliente.getCorreo());
            if (otroCliente != null && otroCliente.getId_cliente() != id) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("mensaje", "El correo electrónico ya está registrado por otro cliente");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            Cliente actualizado = clienteService.actualizarCliente(id, cliente);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("cliente", actualizado);
            response.put("mensaje", "Cliente actualizado correctamente");
            return ResponseEntity.ok(response);
        } catch (DataIntegrityViolationException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("mensaje", "El correo electrónico ya está registrado");
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("mensaje", "Error al actualizar cliente: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
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
    