import { Component, EventEmitter, Output } from '@angular/core';
import { JugadoresService } from '../../../services/jugadores.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Jugador } from '../../../models/Jugador';
import { Router } from '@angular/router';

@Component({
  selector: 'app-crear-jugador',
  templateUrl: './crear-jugador.component.html',
  imports: [CommonModule, FormsModule],
  styleUrls: ['./crear-jugador.component.css'],
})
export class CrearJugadorComponent {
  nuevoJugador: Jugador = {
    name: '',
    url: '',
    ranking: 0,
    points: 0,
    height: 0,
    nationality: '',
    birthplace: '',
    birthdate: '',
    marketValue: '',
    hand: '',
    side: '',
  };
  mensaje: string = '';
  mensajeError: string = '';

  @Output() jugadorCreado = new EventEmitter<void>();

  constructor(
    private jugadoresService: JugadoresService,
    private router: Router
  ) {}

  crearJugador(): void {
    console.log('Intentando crear jugador:', this.nuevoJugador);

    this.jugadoresService.crearJugador(this.nuevoJugador).subscribe({
      next: (data) => {
        console.log(
          'Jugador creado correctamente:',
          data ? data : 'Sin contenido'
        );
        alert('Jugador creado correctamente');

        this.nuevoJugador = {
          name: '',
          url: '',
          ranking: 0,
          points: 0,
          height: 0,
          nationality: '',
          birthplace: '',
          birthdate: '',
          marketValue: '',
          hand: '',
          side: '',
        };

        this.jugadorCreado.emit();
        this.router.navigate(['admin/jugadores']);
      },
      error: (error) => {
        console.error('Error al crear jugador:', error);

        if (error.status === 400) {
          alert(
            'Datos inválidos: Verifica los campos obligatorios.' + error.status
          );
        } else if (error.status === 401) {
          alert(
            'No tienes autorización para realizar esta acción.' + error.status
          );
        } else {
          alert('Error desconocido al crear jugador.');
        }
      },
    });
  }
}
