import { Component, OnInit } from '@angular/core';
import { TorneosService } from '../../../services/torneos.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-lista-torneos',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './lista-torneos.component.html',
  styleUrls: ['./lista-torneos.component.css'],
})
export class ListaTorneosComponent implements OnInit {
  torneos: any[] = [];
  errorMessage: string = '';
  temporadaFiltro: string = '';
  nombreFiltro: string = '';

  modoEdicion: boolean = false;
  torneoEditado: any = {};

  constructor(private torneosService: TorneosService) {}

  ngOnInit(): void {
    this.obtenerTorneos();
  }

  obtenerTorneos(): void {
    this.torneosService.getTorneos().subscribe({
      next: (data) => {
        this.torneos = data;
        console.log('Datos de torneos recibidos:', this.torneos);
      },
      error: (error) => {
        this.errorMessage = 'Error al cargar los torneos.';
        console.error('Error al cargar los torneos', error);
      },
    });
  }

  verDetalles(torneoId: number): void {
    console.log(`Ver detalles del torneo con ID: ${torneoId}`);
  }



  filtrarPorTemporada(): void {
    console.log('Filtrar por temporada:', this.temporadaFiltro);
  }

  filtrarPorNombre(): void {
    console.log('Filtrar por nombre del torneo:', this.nombreFiltro);
  }

  eliminarTorneo(torneoId: number): void {
    const confirmacion = window.confirm(
      '¿Estás seguro de que deseas eliminar este torneo?'
    );

    if (confirmacion) {
      console.log('Eliminando temporada con ID:', torneoId);

      this.torneosService.eliminarTorneo(torneoId).subscribe({
        next: () => {
          console.log('Torneo eliminado correctamente');
          alert('El torneo se eliminó con éxito.');
          this.obtenerTorneos();
        },
        error: (error) => {
          console.error('Error al eliminar el torneo:', error);
          alert('Hubo un error al eliminar el torneo.');
        },
      });
    } else {
      console.log('Eliminación cancelada');
    }
  }
  editarTorneo(torneo: any): void {
  this.modoEdicion = true;
  this.torneoEditado = { ...torneo }; // Hacer copia para edición
}

guardarCambios(): void {
  console.log('Torneo a actualizar:', this.torneoEditado);
  this.torneosService.actualizarTorneo(this.torneoEditado).subscribe({
    next: () => {
      alert('Torneo actualizado correctamente');
      this.modoEdicion = false;
      this.obtenerTorneos();
    },
    error: (error) => {
      console.error('Error al actualizar el torneo:', error);
      alert('Error al actualizar el torneo');
    }
  });
}


cancelarEdicion(): void {
  this.modoEdicion = false;
  this.torneoEditado = {};
}
}
