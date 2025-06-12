import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  imports: [FormsModule, CommonModule]
})
export class LoginComponent {
  username = '';
  password = '';
  errorMessage = '';
  rememberMe = false;

  constructor(private authService: AuthService, private router: Router) { }

  login(event: Event) {
    event.preventDefault();
    this.errorMessage = ''; // Limpiar mensajes de error previos

    this.authService.login({ username: this.username, password: this.password }).subscribe({
      next: (response) => {
        const role = this.authService.getRole();

        if (role === 'ROLE_ADMIN') {
          this.router.navigateByUrl('/admin/jugadores');
        } else if (role === 'ROLE_USER') {
          this.router.navigateByUrl('/user/favoritos');
        } else {
          this.errorMessage = 'Rol desconocido.';
        }
      },
      error: (err) => {
        this.errorMessage = 'Credenciales inválidas o error de conexión.';
      }
    });
  }
}
