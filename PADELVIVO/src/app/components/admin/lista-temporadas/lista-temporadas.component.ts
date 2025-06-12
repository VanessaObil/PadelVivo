import { Component, OnInit } from '@angular/core';
import { CommonModule, NgIf, NgFor } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TemporadasService } from '../../../services/temporadas.service';
import { RouterLink } from '@angular/router';

@Component({
    selector: 'app-lista-temporadas',
    standalone: true,
    imports: [CommonModule, FormsModule, RouterLink],
    templateUrl: './lista-temporadas.component.html',
    styleUrls: ['./lista-temporadas.component.css']
})
export class ListaTemporadasComponent implements OnInit {
    temporadas: any[] = [];
    errorMessage: string = '';
    anioFiltro: string = '';
    nombreFiltro: string = '';

    editandoId: number | null = null;


    constructor(private temporadasService: TemporadasService) { }

    ngOnInit(): void {
        this.obtenerTemporadas();
    }

    obtenerTemporadas(): void {
        this.temporadasService.getTemporadas().subscribe({
            next: (data) => {
                this.temporadas = data;
                console.log('Datos de temporadas recibidos:', this.temporadas);
            },
            error: (error) => {
                this.errorMessage = 'Error al cargar las temporadas.';
                console.error('Error al cargar las temporadas', error);
            }
        });
    }

    eliminarTemporada(temporadaId: number): void {
    const confirmacion = window.confirm("¿Estás seguro de que deseas eliminar esta temporada?");

    if (confirmacion) {
        console.log('Eliminando temporada con ID:', temporadaId);

        this.temporadasService.eliminarTemporada(temporadaId).subscribe({
            next: () => {
                console.log('Temporada eliminada correctamente');
                alert("La temporada se eliminó con éxito.");
                this.obtenerTemporadas();
            },
            error: (error) => {
                console.error('Error al eliminar la temporada:', error);
                alert("Hubo un error al eliminar la temporada.");
            }
        });
    } else {
        console.log('Eliminación cancelada');
    }
}




guardarEdicion(temporada: any): void {
  if (temporada.name && temporada.startDate && temporada.endDate && temporada.year) {
    this.temporadasService.actualizarTemporada(temporada.id, temporada).subscribe({
      next: () => {
        alert('Temporada actualizada correctamente');
        this.editandoId = null;
        this.obtenerTemporadas();
      },
      error: () => {
        alert('Error al actualizar la temporada');
      }
    });
  } else {
    alert('Todos los campos son obligatorios');
  }
}




    filtrarPorAnio(): void {
        console.log('Filtrar por año:', this.anioFiltro);
        if (this.anioFiltro) {
            this.temporadas = this.temporadas.filter(temporada => temporada.anio.toString().includes(this.anioFiltro));
        } else {
            this.obtenerTemporadas();
        }
    }

    filtrarPorNombre(): void {
        console.log('Filtrar por nombre de la temporada:', this.nombreFiltro);
        if (this.nombreFiltro) {
            this.temporadas = this.temporadas.filter(temporada => temporada.nombre.toLowerCase().includes(this.nombreFiltro.toLowerCase()));
        } else {
            this.obtenerTemporadas();
        }
    }
}
