import { Component, OnInit } from '@angular/core';
import { UserService } from '../../../services/user.service';
import { User } from '../../../models/User';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-lista-users',
  templateUrl: './lista-users.component.html',
  styleUrls: ['./lista-users.component.css'],
  imports: [FormsModule, CommonModule, RouterLink],
  standalone: true // si estás usando Angular standalone components
})
export class ListaUsersComponent implements OnInit {

  users: User[] = [];
  mensajeError: string = '';
  editingUser: User | null = null;

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.getUsers();
  }

 getUsers(): void {
  this.userService.getUsers().subscribe({
    next: (data) => {
      this.users = data;
    },
    error: () => {
      this.mensajeError = 'Error al cargar los usuarios.';
      alert('Error al cargar los usuarios.');
    }
  });
}

  onEditUser(user: User): void {
    this.editingUser = { ...user };
  }

  cancelarEdicion(): void {
    this.editingUser = null;
  }

  guardarCambios(): void {
  if (!this.editingUser || this.editingUser.id == null) return;

  this.userService.updateUser(this.editingUser.id, this.editingUser).subscribe({
    next: () => {
      alert('Usuario actualizado correctamente.');
      this.editingUser = null;
      this.getUsers();
    },
    error: () => {
      this.mensajeError = 'Error al guardar los cambios.';
      alert('Error al guardar los cambios.');
    }
  });
}

  onDeleteUser(user: User): void {
  if (!user.id) return;

  if (confirm(`¿Estás seguro de que deseas eliminar a ${user.username}?`)) {
    this.userService.deleteUser(user.id).subscribe({
      next: () => {
        alert('Usuario eliminado correctamente.');
        this.getUsers();
      },
      error: () => {
        this.mensajeError = 'Error al eliminar el usuario.';
        alert('Error al eliminar el usuario.');
      }
    });
  }
}

  onAddUser(): void {
    const nuevoUsuario: User = {
      id: 0,
      username: '',
      firstName: '',
      lastName: '',
      enabled: true,
      password: '',
      image: ''
    };
    this.editingUser = nuevoUsuario;
  }
}
