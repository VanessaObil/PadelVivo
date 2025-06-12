import { Component, OnInit } from '@angular/core';
import { TorneosService } from '../../../services/torneos.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-torneos',
  templateUrl: './cardstorneos.component.html',
  imports: [CommonModule, FormsModule],
  styleUrls: ['./cardstorneos.component.css']
})
export class CardstorneosComponent implements OnInit {
  torneos: any[] = [];
  torneosFiltrados: any[] = [];
  fechaSeleccionada: string = '';
  estadoSeleccionado: string = 'todos';
  errorMessage: string = '';

  constructor(private torneoService: TorneosService) {}

  ngOnInit(): void {

    this.torneoService.getTorneos().subscribe(
      (data) => {
        this.torneos = data;
        this.torneosFiltrados = data;
      },
      (error) => {
        this.errorMessage = 'Hubo un error al obtener los torneos';
        console.error(error);
      }
    );
  }


  aplicarFiltroFecha(): void {
    if (this.fechaSeleccionada) {
      this.torneosFiltrados = this.torneos.filter(torneo => {
        const torneoFecha = new Date(torneo.startDate);
        return torneoFecha.toISOString().split('T')[0] === this.fechaSeleccionada;
      });
    } else {
      this.torneosFiltrados = this.torneos;
    }
  }


  filtrarPorEstado(estado: string): void {
    if (estado === 'todos') {
      this.torneosFiltrados = this.torneos;
    } else {
      this.torneosFiltrados = this.torneos.filter(torneo => torneo.status === estado);
    }
  }
}
