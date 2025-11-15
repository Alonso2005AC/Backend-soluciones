package mass.backendmass.controller;

import mass.backendmass.models.Cliente;
import mass.backendmass.models.Usuario;
import mass.backendmass.repository.ClienteRepository;
import mass.backendmass.repository.UsuarioRepository;
import mass.backendmass.service.ClienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/login-cliente")
    public ResponseEntity<?> loginCliente(@RequestBody Map<String, String> body) {
        String correo = body.get("correo");
        String contrasena = body.get("contrasena");
        
        System.out.println("=== LOGIN DEBUG ===");
        System.out.println("Correo recibido: " + correo);
        System.out.println("Contraseña recibida: " + contrasena);
        
        // Primero buscar en tabla cliente
        Cliente cliente = clienteRepository.findByCorreo(correo);
        if (cliente != null) {
            System.out.println("Cliente encontrado: " + cliente.getNombre());
            System.out.println("Hash en BD: " + cliente.getContraseña());
            // Verificar contraseña (asumiendo que está encriptada)
            boolean matches = passwordEncoder.matches(contrasena, cliente.getContraseña());
            System.out.println("Contraseña coincide: " + matches);
            if (matches) {
                Map<String, Object> response = new HashMap<>();
                response.put("id", cliente.getId_cliente());
                response.put("nombre", cliente.getNombre());
                response.put("apellido", cliente.getApellido());
                response.put("correo", cliente.getCorreo());
                response.put("rol", "cliente");
                response.put("tipo", "cliente");
                return ResponseEntity.ok(response);
            }
        } else {
            System.out.println("Cliente NO encontrado en tabla clientes");
        }
        
        // Si no se encuentra en clientes, buscar en tabla usuario
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            System.out.println("Usuario encontrado: " + usuario.getNombre());
            System.out.println("Hash en BD: " + usuario.getContrasena());
            boolean matches = passwordEncoder.matches(contrasena, usuario.getContrasena());
            System.out.println("Contraseña coincide: " + matches);
            if (matches) {
                Map<String, Object> response = new HashMap<>();
                response.put("id", usuario.getId_usuario());
                response.put("nombre", usuario.getNombre());
                response.put("apellido", usuario.getApellido());
                response.put("correo", usuario.getCorreo());
                response.put("rol", usuario.getRol().toString());
                response.put("tipo", "usuario");
                return ResponseEntity.ok(response);
            }
        } else {
            System.out.println("Usuario NO encontrado en tabla usuarios");
        }
        
        System.out.println("=== FIN LOGIN DEBUG ===");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody Cliente cliente) {
        try {
            Cliente nuevoCliente = clienteService.registrarCliente(cliente);
            
            Map<String, Object> response = new HashMap<>();
            response.put("id_cliente", nuevoCliente.getId_cliente());
            response.put("nombre", nuevoCliente.getNombre());
            response.put("correo", nuevoCliente.getCorreo());
            response.put("mensaje", "Registro exitoso");
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/registrar-usuario")
    public ResponseEntity<?> registrarUsuario(@RequestBody Map<String, String> body) {
        try {
            Usuario usuario = new Usuario();
            usuario.setNombre(body.get("nombre"));
            usuario.setApellido(body.get("apellido"));
            usuario.setCorreo(body.get("correo"));
            
            // Encriptar la contraseña
            String contrasena = body.get("contrasena");
            if (contrasena == null || contrasena.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La contraseña es requerida");
            }
            usuario.setContrasena(passwordEncoder.encode(contrasena));
            
            // Establecer el rol
            String rolStr = body.get("rol");
            if (rolStr != null) {
                usuario.setRol(Usuario.Rol.valueOf(rolStr));
            }
            
            Usuario nuevoUsuario = usuarioRepository.save(usuario);
            
            Map<String, Object> response = new HashMap<>();
            response.put("id", nuevoUsuario.getId_usuario());
            response.put("nombre", nuevoUsuario.getNombre());
            response.put("correo", nuevoUsuario.getCorreo());
            response.put("rol", nuevoUsuario.getRol().toString());
            response.put("mensaje", "Usuario registrado exitosamente");
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
}
