import { Component, EventEmitter, Input, Output } from '@angular/core';
import { UserService } from '../../../services/user.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { User } from '../../../models/User';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-crear-user',
  templateUrl: './crear-user.component.html',
  styleUrls: ['./crear-user.component.css'],
  imports: [CommonModule,
    ReactiveFormsModule]
})
export class CrearUserComponent {
  @Input() user: User | null = null; // null = nuevo usuario
  @Output() saved = new EventEmitter<void>();
  @Output() cancelled = new EventEmitter<void>();

  userForm: FormGroup;
  mensaje: string = '';
  mensajeError: string = '';

  constructor(private fb: FormBuilder, private userService: UserService) {
    this.userForm = this.fb.group({
      username: ['', Validators.required],
      password: [''],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      image: [''],
      enabled: [true],
      roleName: ['', Validators.required],  // <-- rol obligatorio
    });
  }

  ngOnInit(): void {
    if (this.user) {
      this.userForm.patchValue({
        username: this.user.username,
        firstName: this.user.firstName,
        lastName: this.user.lastName,
        image: this.user.image,
        enabled: this.user.enabled,
        // Asumimos que user.roles es un array de strings con los nombres de roles
        roleName: this.user.roles && this.user.roles.length > 0 ? this.user.roles[0] : '',
      });

      // Opcional: si no quieres que el rol se edite al modificar usuario, deshabilita el control
      this.userForm.get('roleName')?.disable();
    }
  }

  submit(): void {
    const formValue = this.userForm.getRawValue(); // getRawValue para obtener valor aunque esté deshabilitado
    this.mensaje = '';
    this.mensajeError = '';

    if (this.user) {
      // Actualizar usuario (rol no cambia aquí porque está deshabilitado)
      this.userService.updateUser(this.user.id!, formValue).subscribe({
        next: () => {
          this.mensaje = 'Usuario actualizado correctamente.';
          setTimeout(() => {
            this.mensaje = '';
            this.saved.emit();
          }, 2000);
        },
        error: () => {
          this.mensajeError = 'Error al actualizar el usuario.';
        }
      });
    } else {
      // Crear usuario requiere contraseña
      if (!formValue.password) {
        alert('La contraseña es obligatoria para crear un usuario.');
        return;
      }
      this.userService.addUser(formValue).subscribe({
        next: () => {
          this.mensaje = 'Usuario creado correctamente.';
          setTimeout(() => {
            this.mensaje = '';
            this.saved.emit();
          }, 2000);
        },
        error: () => {
          this.mensajeError = 'Error al crear el usuario.';
        }
      });
    }
  }

  cancel(): void {
    this.cancelled.emit();
  }
}
