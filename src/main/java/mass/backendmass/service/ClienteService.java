package mass.backendmass.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import mass.backendmass.models.Cliente;
import mass.backendmass.repository.ClienteRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Cliente loginCliente(String correo, String contraseña) throws Exception {
        Cliente cliente = clienteRepository.findByCorreo(correo);

        if (cliente == null) {
            throw new Exception("Correo no registrado");
        }

        if (!passwordEncoder.matches(contraseña, cliente.getContraseña())) {
            throw new Exception("Contraseña incorrecta");
        }

        return cliente;
    }


    // Obtener todos los clientes
    public ArrayList<Cliente> listaClientes() {
        return (ArrayList<Cliente>) clienteRepository.findAll();
    }

    // Guardar cliente
    public Cliente guardarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    // Obtener cliente por ID
    public Optional<Cliente> obtenerPorId(int id) {
        return clienteRepository.findById(id);
    }

    // Actualizar cliente
    public Cliente actualizarCliente(int id, Cliente clienteActualizado) {
        return clienteRepository.findById(id).map(c -> {
            c.setNombre(clienteActualizado.getNombre());
            c.setApellido(clienteActualizado.getApellido());
            c.setCorreo(clienteActualizado.getCorreo());
            c.setTelefono(clienteActualizado.getTelefono());
            c.setDireccion(clienteActualizado.getDireccion());
            return clienteRepository.save(c);
        }).orElse(null);
    }
    public Cliente registrarCliente(Cliente cliente) {
    // Verificar si el correo ya existe
    if (clienteRepository.findByCorreo(cliente.getCorreo()) != null) {
        throw new RuntimeException("El correo ya está registrado");
    }
    
    // Encriptar la contraseña
    cliente.setContraseña(passwordEncoder.encode(cliente.getContraseña()));
    
    // Establecer rol por defecto
    cliente.setRol("cliente");
    
    return clienteRepository.save(cliente);
    }

    // Eliminar cliente por ID
    public boolean eliminarCliente(int id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
