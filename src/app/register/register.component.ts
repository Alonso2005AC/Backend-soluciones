import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from '../services/api.service';
import { MockAuthService } from '../services/mock-auth.service';
import { timeout } from 'rxjs/operators';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  nombre: string = '';
  apellido: string = '';
  correo: string = '';
  contrasena: string = '';
  telefono: string = '';
  direccion: string = '';
  error: string = '';
  success: string = '';
  loading: boolean = false;

  constructor(private apiService: ApiService, private router: Router, private mockAuth: MockAuthService) {}

  isPasswordValid(): boolean {
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+\-=[\]{};':"\\|,.<>/?]).{8,}$/;
    return passwordRegex.test(this.contrasena);
  }

  register() {
    // Reset UI state
    this.error = '';
    this.success = '';
    this.loading = true;

    // Trim input fields
    this.nombre = this.nombre?.trim();
    this.apellido = this.apellido?.trim();
    this.correo = this.correo?.trim();
    this.contrasena = this.contrasena?.trim();

    // Validate required fields
    if (!this.nombre) {
      this.error = 'El campo Nombre es obligatorio.';
      this.loading = false;
      return;
    }
    if (!this.apellido) {
      this.error = 'El campo Apellido es obligatorio.';
      this.loading = false;
      return;
    }
    if (!this.correo) {
      this.error = 'El campo Correo Electrónico es obligatorio.';
      this.loading = false;
      return;
    }
    // Simple email format check
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(this.correo)) {
      this.error = 'Ingrese un correo válido.';
      this.loading = false;
      return;
    }
    if (!this.contrasena) {
      this.error = 'El campo Contraseña es obligatorio.';
      this.loading = false;
      return;
    }

    // Local duplicate-check fallback (for offline/mock scenarios)
    try {
      const usuariosRaw = localStorage.getItem('usuarios');
      const usuarios = usuariosRaw ? JSON.parse(usuariosRaw) : [];
      const usuarioRaw = localStorage.getItem('usuario');
      const usuarioActual = usuarioRaw ? JSON.parse(usuarioRaw) : null;
      const mockUserRaw = localStorage.getItem('mock_user');
      const mockUser = mockUserRaw ? JSON.parse(mockUserRaw) : null;

      const existeInUsuarios = usuarios.some((u: any) => (u.correo || u.email) === this.correo);
      const existeInUsuario = usuarioActual && ((usuarioActual.correo || usuarioActual.email) === this.correo);
      const existeInMock = mockUser && ((mockUser.email || mockUser.correo) === this.correo);
      const existe = existeInUsuarios || existeInUsuario || existeInMock;
      if (existe) {
        this.error = 'El correo ya está registrado. Intenta con otro.';
        this.loading = false;
        return;
      }
    } catch (e) {
      console.warn('Error leyendo usuarios locales:', e);
      // continue to attempt server registration
    }

    // Password strength validation
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+\-=[\]{};':"\\|,.<>/?]).{8,}$/;
    if (!passwordRegex.test(this.contrasena)) {
      this.error = 'La contraseña debe tener mínimo 8 caracteres, al menos una mayúscula, una minúscula, un número y un carácter especial.';
      this.loading = false;
      return;
    }

    // Build client payload
    const cliente = {
      nombre: this.nombre,
      apellido: this.apellido,
      correo: this.correo,
      contrasena: this.contrasena,
      telefono: this.telefono,
      direccion: this.direccion
    };

    console.log('Registrando cliente:', { ...cliente, contrasena: '***' });

    // Call backend, with timeout and fallback logic
    this.apiService.register(cliente).pipe(timeout(8000)).subscribe({
      next: (res: any) => {
        // IMPORTANT: backend may respond with { success: false, mensaje: '...' } and HTTP 200
        // — check for that and display the message instead of treating as success.
        if (res && res.success === false) {
          this.error = res.mensaje || res.message || 'El correo ya está siendo utilizado.';
          this.loading = false;
          return;
        }

        // Otherwise treat as success (either res.success === true, or non-structured successful response)
        console.log('✅ Registro exitoso:', res);

        // Save locally as a fallback copy (optional)
        try {
          const usuariosRaw = localStorage.getItem('usuarios');
          const usuarios = usuariosRaw ? JSON.parse(usuariosRaw) : [];
          usuarios.push({
            nombre: cliente.nombre,
            apellido: cliente.apellido,
            correo: cliente.correo,
            contrasena: cliente.contrasena
          });
          localStorage.setItem('usuarios', JSON.stringify(usuarios));
        } catch (e) {
          console.warn('No se pudo guardar usuario localmente', e);
        }

        // Show server success message if present, otherwise fallback text
        this.success = (res && res.mensaje) ? res.mensaje : 'Registro exitoso. Redirigiendo al login...';
        this.loading = false;

        // Navigate to login after a delay
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2000);
      },
      error: (err: any) => {
        console.error('❌ Error en registro:', err);
        console.error('Status:', err?.status);
        console.error('Error body:', err?.error);
        this.loading = false;

        // Timeout or network fallback to local/mock registration
        const isTimeout = err && (err.name === 'TimeoutError' || err?.message?.toLowerCase?.().includes('timeout'));
        if (isTimeout || err?.status === 0) {
          try {
            const resultado = this.mockAuth.registerClient({
              nombre: cliente.nombre,
              apellido: cliente.apellido,
              correo: cliente.correo,
              contrasena: cliente.contrasena
            });
            if (!resultado.success) {
              this.error = resultado.message || 'El correo ya está registrado. Intenta con otro.';
              return;
            }
            this.success = 'Registro local guardado (backend no disponible). Redirigiendo al login...';
            setTimeout(() => this.router.navigate(['/login']), 1500);
            return;
          } catch (e) {
            console.warn('Fallback local falló', e);
            // Continue to handle original error below
          }
        }

        // Handle standard backend responses (400, 409, etc.)
        if (err?.status === 400) {
          // older code path: backend returned a 400 with error message in body
          const mensajeBackend = typeof err.error === 'string' ? err.error : (err.error?.message || err.error?.mensaje);
          this.error = mensajeBackend || 'Datos inválidos. Verifica todos los campos.';
        } else if (err?.status === 409) {
          // conflict (duplicate) – standard RESTful approach
          const mensajeBackend = typeof err.error === 'string' ? err.error : (err.error?.message || err.error?.mensaje);
          this.error = mensajeBackend || 'El correo ya está registrado. Intenta con otro.';
        } else if (err?.status === 0) {
          // no connection
          this.error = 'No se puede conectar con el servidor';
        } else {
          // fallback general message
          this.error = err?.error?.message || err?.error || 'Error al registrar';
        }
      }
    });
  }

  goToLogin() {
    this.router.navigate(['/login']);
  }
}