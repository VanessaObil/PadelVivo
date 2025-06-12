import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { UserService } from '../../../services/user.service';
import { User } from '../../../models/User';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-perfilusuario',
  templateUrl: './perfilusuario.component.html',
  styleUrls: ['./perfilusuario.component.css'],
   imports: [CommonModule, FormsModule],
})
export class PerfilusuarioComponent implements OnInit {
  usuario: User = {} as User;
  usuarioOriginal: User = {} as User; // Para cancelar edición
  editando: boolean = false;

  constructor(
    private router: Router,
    private authService: AuthService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login']);
      return;
    }

    const userData = this.authService.getUserFromToken();
    if (userData) {
      this.usuario = userData;
    } else {
      this.router.navigate(['/login']);
    }
  }

  guardarCambios() {
  // Aseguramos que enabled siempre sea 1
  this.usuario.enabled = true; // O false, según tu lógica

  this.userService.updateUser(this.usuario.id!, this.usuario).subscribe({
    next: (updatedUser) => {
      this.usuario = updatedUser;
      this.usuarioOriginal = { ...updatedUser };
      this.editando = false;
      alert('Perfil actualizado con éxito');
    },
    error: (err) => {
      console.error('Error al actualizar:', err);
      alert('Hubo un error al guardar los cambios');
    }
  });
}


  cancelarEdicion() {
    this.usuario = { ...this.usuarioOriginal };
    this.editando = false;
  }

  goBack() {
    this.router.navigate(['/']); // O cambia a donde quieras volver
  }
}
