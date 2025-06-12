import { Component, OnInit } from '@angular/core';
import { FavoritosService } from '../../../services/favoritos.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-cardsfavoritos',
  templateUrl: './cardsfavoritos.component.html',
  styleUrls: ['./cardsfavoritos.component.css'],
  imports: [CommonModule]
})
export class CardsFavoritosComponent implements OnInit {
  favoritos: any[] = [];
  errorMessage: string = '';

  constructor(private favoritosService: FavoritosService) {}

  ngOnInit() {
    this.cargarFavoritos();
  }

  cargarFavoritos() {
    this.favoritosService.getFavoritos().subscribe({
      next: (data) => {
        this.favoritos = data;
      },
      error: (err) => {
        this.errorMessage = 'Error al cargar favoritos';
      }
    });
  }
  eliminarFavorito(matchId: string) {
    this.favoritosService.eliminarFavorito(matchId).subscribe({
      next: () => this.cargarFavoritos(),
      error: () => alert('Error al eliminar favorito')
    });
  }

}
