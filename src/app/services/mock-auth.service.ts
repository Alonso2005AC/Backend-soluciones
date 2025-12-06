import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class MockAuthService {
  private client = { email: 'cliente@ejemplo.com', password: 'cliente123', name: 'Cliente Demo' };
  private admin = { code: 'ADMIN1234', name: 'Administrador' };

  constructor(private router: Router) {}

  loginClient(email: string, password: string): boolean {
    // Intentar comprobar usuarios guardados localmente (registro sin backend)
    try {
      const usuariosRaw = localStorage.getItem('usuarios');
      const usuarios = usuariosRaw ? JSON.parse(usuariosRaw) : [];
      const match = usuarios.find((u: any) => ((u.correo || u.email) === email) && ((u.contrasena || u.password) === password));
      if (match) {
        localStorage.setItem('mock_user', JSON.stringify({ role: 'client', name: match.nombre || match.name || this.client.name, email }));
        return true;
      }
    } catch (e) {
      console.warn('mock-auth: error leyendo usuarios locales', e);
    }

    // Fallback a usuario demo incluido en el servicio
    if (email === this.client.email && password === this.client.password) {
      localStorage.setItem('mock_user', JSON.stringify({ role: 'client', name: this.client.name, email }));
      return true;
    }

    return false;
  }

  /**
   * Registrar un cliente en localStorage (solo para desarrollo sin backend)
   * Devuelve true si se registró correctamente, false si el correo ya existe
   */
  registerClient(usuario: any): { success: boolean; message?: string } {
    try {
      const usuariosRaw = localStorage.getItem('usuarios');
      const usuarios = usuariosRaw ? JSON.parse(usuariosRaw) : [];
      const existe = usuarios.some((u: any) => (u.correo || u.email) === (usuario.correo || usuario.email));
      if (existe) {
        return { success: false, message: 'El correo ya está registrado' };
      }
      usuarios.push(usuario);
      localStorage.setItem('usuarios', JSON.stringify(usuarios));
      return { success: true };
    } catch (e) {
      console.warn('mock-auth: error al registrar usuario', e);
      return { success: false, message: 'Error al guardar usuario localmente' };
    }
  }

  loginAdmin(code: string): boolean {
    if (code === this.admin.code) {
      localStorage.setItem('mock_user', JSON.stringify({ role: 'admin', name: this.admin.name, code }));
      return true;
    }
    return false;
  }

  logout() {
    localStorage.removeItem('mock_user');
    this.router.navigate(['/login']);
  }

  getCurrentUser() {
    const raw = localStorage.getItem('mock_user');
    return raw ? JSON.parse(raw) : null;
  }
}
