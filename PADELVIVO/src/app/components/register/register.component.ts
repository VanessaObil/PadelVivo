// register.component.ts
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  imports: [CommonModule, ReactiveFormsModule]
})
export class RegisterComponent {
  registerForm: FormGroup;
  errorMessage: string = '';
  successMessage: string = '';


  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
    });
  }

  onSubmit(): void {
  if (this.registerForm.valid) {
    const formValue = this.registerForm.value;

    // Guardamos los datos en el localStorage
    const user = {
      nombre: `${formValue.firstName} ${formValue.lastName}`,
      email: formValue.username, // El correo es el username
      password: formValue.password
    };

    localStorage.setItem('usuario', JSON.stringify(user));  // Guardamos correctamente el usuario

    // Enviamos los datos al backend
    this.http.post(`${environment.apiUrl}/api/auth/register`, formValue)
      .subscribe({
          next: (res: any) => {
            this.successMessage = res.message || 'Usuario registrado con éxito';
            this.errorMessage = '';

            // Redirige tras 2 segundos
            setTimeout(() => {
              this.router.navigate(['/perfil']);
            }, 2000);
          },
          error: err => {
            this.errorMessage = err.error?.error || 'Error al registrar usuario';
            this.successMessage = '';
          }
        });
  }
}

}
