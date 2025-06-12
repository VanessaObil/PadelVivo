import { Component, OnInit } from '@angular/core';
import { JugadoresService } from '../../../services/jugadores.service';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-lista-jugadores',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './lista-jugadores.component.html',
  styleUrls: ['./lista-jugadores.component.css'],
})
export class ListaJugadoresComponent implements OnInit {
  jugadores: any[] = [];
  errorMessage: string = '';

  constructor(private jugadoresService: JugadoresService) {}

  ngOnInit(): void {
    this.obtenerJugadores();
  }

  obtenerJugadores(): void {
    this.jugadoresService.getJugadores().subscribe({
      next: (data: any[]) => {
        this.jugadores = data;
        console.log('Jugadores recibidos:', data);
      },
      error: (error) => {
        this.errorMessage = 'Error al cargar los jugadores.';
        console.error('Error al cargar los jugadores', error);
      },
    });
  }

  eliminarJugadores(jugadorId: number): void {
    const confirmacion = window.confirm(
      '¿Estás seguro de que deseas eliminar este jugador?'
    );

    if (confirmacion) {
      console.log('Eliminando jugador con ID:', jugadorId);

      this.jugadoresService.eliminarJugadores(jugadorId).subscribe({
        next: () => {
          console.log('Jugador eliminado correctamente');
          alert('El jugador se eliminó con éxito.');
          this.obtenerJugadores();
        },
        error: (error) => {
          console.error('Error al eliminar el jugador:', error);
          alert('Hubo un error al eliminar el jugador.');
        },
      });
    } else {
      console.log('Eliminación cancelada');
    }
  }

  editarJugador(jugador: any): void {
  jugador.editing = true;
}

guardarJugador(jugador: any): void {
  this.jugadoresService.actualizarJugador(jugador.id, jugador).subscribe({
    next: () => {
      jugador.editing = false;
      alert('Jugador actualizado correctamente.');
    },
    error: (error) => {
      console.error('Error al actualizar jugador', error);
      alert('Error al actualizar jugador.');
    }
  });
}

}
