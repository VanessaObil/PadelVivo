import { Component, OnInit } from '@angular/core';
import { PartidosService } from '../../../services/partidos.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { environment } from '../../../../environments/environment';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-lista-partidos',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './lista-partidos.component.html',
  styleUrls: ['./lista-partidos.component.css'],
})
export class ListaPartidosComponent implements OnInit {
  partidos: any[] = [];
  errorMessage: string = '';
  fechaFiltro: string = '';
  estadoFiltro: string = '';

  editandoId: string | null = null;
partidoOriginal: any = {};

  constructor(private partidosService: PartidosService) {}

  ngOnInit(): void {
    this.obtenerPartidos();
  }

  obtenerPartidos(): void {
    this.partidosService.getPartidos().subscribe({
      next: (data) => {
        this.partidos = data;
        console.log('Datos de partidos recibidos:', this.partidos);
      },
      error: (error) => {
        this.errorMessage = 'Error al cargar los partidos.';
        console.error('Error al cargar los partidos', error);
      },
    });
  }

  verDetalles(partidoId: number): void {
    console.log(`Ver detalles del partido con ID: ${partidoId}`);
  }



  filtrarPorFecha(): void {
    console.log('Filtrar por fecha:', this.fechaFiltro);
  }

  filtrarPorEstado(): void {
    console.log('Filtrar por estado:', this.estadoFiltro);
  }

  eliminarPartido(partidoId: string) {
  if (!confirm("¿Estás seguro de que deseas eliminar este partido?")) {
    return;
  }

  this.partidosService.eliminarPartido(partidoId).subscribe({
  next: (mensaje) => {
    alert(mensaje); // Aquí mostrarás "Partido eliminado correctamente"
    this.obtenerPartidos();
  },
  error: (error) => {
    console.error('Error al eliminar partido:', error);
    alert('Error al eliminar el partido');
  }
});
  }

editarPartido(partido: any) {
  this.editandoId = partido.partidoId;

  // Backup de los datos originales por si se cancela
  this.partidoOriginal = { ...partido };

  // Convertimos el array de jugadores en string temporal para edición
  partido.jugadoresString = (partido.jugadores || []).join(', ');

}

cancelarEdicion() {
  const index = this.partidos.findIndex(p => p.partidoId === this.editandoId);
  if (index !== -1) {
    this.partidos[index] = { ...this.partidoOriginal };
  }
  this.editandoId = null;
  this.partidoOriginal = {};
}
guardarEdicion(partido: any) {
  const jugadoresStr = partido.jugadoresActualesString || partido.jugadoresString;
  if (typeof jugadoresStr === 'string') {
    partido.jugadoresActuales = jugadoresStr
      .split(',')
      .map((j: string) => j.trim())
      .filter((j: string) => j !== '');
  } else {
    partido.jugadoresActuales = [];
  }

  const nacionalidadesStr = partido.nacionalidadesString;
  if (typeof nacionalidadesStr === 'string') {
    partido.nacionalidades = nacionalidadesStr
      .split(',')
      .map((n: string) => n.trim())
      .filter((n: string) => n !== '');
  } else {
    partido.nacionalidades = [];
  }

  if (partido.fechaPartido) {
    const date = new Date(partido.fechaPartido);
    partido.fechaPartido = date.toISOString().substring(0, 10);
  }

  this.partidosService.actualizarPartido(partido.partidoId, partido).subscribe({
    next: () => {
      this.editandoId = null;
      this.partidoOriginal = {};
      alert('✅ Cambios guardados correctamente');
      this.obtenerPartidos(); // Recarga los datos actualizados
    },
    error: (err) => {
      console.error('Error al actualizar partido:', err);
      alert('❌ Error al guardar los cambios');
    }
  });
}










}
