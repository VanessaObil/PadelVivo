
import { Component, OnInit } from '@angular/core';
import { TemporadasService } from '../../../services/temporadas.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-cardstemporadas',
  templateUrl: './cardstemporadas.component.html',
  imports: [CommonModule, FormsModule],
  styleUrls: ['./cardstemporadas.component.css']
})
export class CardstemporadasComponent implements OnInit {

  temporadas: any[] = [];
  temporadasFiltradas: any[] = [];
  errorMessage: string = '';
  añoSeleccionado: string = '';

  constructor(private temporadasService: TemporadasService) {}

  ngOnInit(): void {
  this.obtenerTemporadas();
}

obtenerTemporadas(): void {
  this.temporadasService.getTemporadas().subscribe({
    next: (data) => {
      console.log("Datos recibidos de la API: ", data);
      this.temporadas = data;
      this.temporadasFiltradas = data;
    },
    error: (error) => {
      this.errorMessage = 'Error al cargar las temporadas';
      console.error(error);
    }
  });
}
}
