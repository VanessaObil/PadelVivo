import { Component, EventEmitter, Output } from '@angular/core';
import { TemporadasService } from '../../../services/temporadas.service';
import { Router, RouterEvent, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-crear-temporada',
  templateUrl: './crear-temporada.component.html',
  imports: [CommonModule, FormsModule],
  styleUrls: ['./crear-temporada.component.css'],
})
export class CrearTemporadaComponent {
  nuevaTemporada = {
    name: '',
    startDate: '',
    endDate: '',
    year: null,
  };
  mensaje: string = '';
  mensajeError: string = '';

  @Output() temporadaCreada = new EventEmitter<void>();

  constructor(
    private temporadasService: TemporadasService,
    private router: Router
  ) {}
  crearTemporada(): void {
    console.log('Intentando crear temporada:', this.nuevaTemporada);

    this.temporadasService.crearTemporada(this.nuevaTemporada).subscribe({
      next: (data) => {
        console.log(
          'Temporada creada correctamente:',
          data ? data : 'Sin contenido'
        );
        alert('Temporada creada correctamente');

        this.nuevaTemporada = {
          name: '',
          startDate: '',
          endDate: '',
          year: null,
        };
      },
      error: (error) => {
        console.error('Error al crear temporada:', error);

        if (error.status === 400) {
          alert('Datos inválidos: Verifica los campos obligatorios.');
        } else if (error.status === 401) {
          alert('No tienes autorización para realizar esta acción.');
        } else {
          alert('Error desconocido al crear temporada.');
        }
      },
    });
    this.temporadaCreada.emit();
    this.router.navigate(['admin/temporadas']);
  }
}
