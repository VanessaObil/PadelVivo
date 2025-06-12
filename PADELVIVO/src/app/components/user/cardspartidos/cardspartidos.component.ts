import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PartidosService } from '../../../services/partidos.service';
import { FormsModule } from '@angular/forms';
import { FavoritosService } from '../../../services/favoritos.service';
import { AuthService } from '../../../services/auth.service';  // importa AuthService

@Component({
  selector: 'app-cardspartidos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './cardspartidos.component.html',
  styleUrls: ['./cardspartidos.component.css']
})
export class CardspartidosComponent implements OnInit {
  partidos: any[] = [];
  partidosFiltrados: any[] = [];
  favoritosIds: Set<string> = new Set();

  fechaSeleccionada: string = '';
  estadoSeleccionado: string = 'todos';
  errorMessage: string = '';

  constructor(
    private partidoService: PartidosService,
    private favoritosService: FavoritosService,
    private authService: AuthService  // inyecta AuthService
  ) { }

  ngOnInit(): void {
    this.cargarDatos();
  }

  cargarDatos() {
    this.partidoService.getPartidos().subscribe({
      next: (partidos) => {
        this.partidos = partidos;
        this.partidosFiltrados = [...partidos];
        this.cargarFavoritos();
      },
      error: () => {
        this.errorMessage = 'Error al cargar los partidos.';
      }
    });
  }

  cargarFavoritos() {
    this.favoritosService.getFavoritos().subscribe({
      next: (favoritos) => {
        this.favoritosIds = new Set(
          favoritos
            .filter((f: any) => f && f.matchId !== undefined && f.matchId !== null)
            .map((f: any) => f.matchId.toString())
        );
      },
      error: () => console.warn('No se pudo cargar favoritos')
    });
  }

  esFavorito(partidoId: string | number): boolean {
    return this.favoritosIds.has(partidoId.toString());
  }

  toggleFavorito(partido: any) {
    if (!this.authService.isLoggedIn()) {
      alert('Debes iniciar sesión para guardar favoritos');
      return;
    }

    const partidoId = partido.partidoId.toString();

    if (this.esFavorito(partidoId)) {
      this.favoritosService.eliminarFavorito(partidoId).subscribe({
        next: () => {
          this.favoritosIds.delete(partidoId);
        },
        error: () => alert('Error al eliminar favorito')
      });
    } else {
      this.favoritosService.agregarFavorito(partidoId).subscribe({
        next: () => {
          this.favoritosIds.add(partidoId);
        },
        error: () => alert('Error al agregar favorito')
      });
    }
  }

  aplicarFiltroFecha() {
    if (!this.fechaSeleccionada) {
      this.partidosFiltrados = [...this.partidos];
      return;
    }

    const fechaSeleccionadaDate = new Date(this.fechaSeleccionada);
    this.partidosFiltrados = this.partidos.filter(p => {
      const fechaPartido = new Date(p.fechaPartido);
      return (
        fechaPartido.getFullYear() === fechaSeleccionadaDate.getFullYear() &&
        fechaPartido.getMonth() === fechaSeleccionadaDate.getMonth() &&
        fechaPartido.getDate() === fechaSeleccionadaDate.getDate()
      );
    });
  }

  filtrarPorTipo(tipo: string): void {
    if (tipo === 'finished') {
      this.partidosFiltrados = this.partidos.filter(p => p.estado === 'finished');
    } else if (tipo === 'bye') {
      this.partidosFiltrados = this.partidos.filter(p => p.estado === 'bye');
    } else {
      this.partidosFiltrados = [...this.partidos];
    }
  }
}
