import { Component, OnInit } from '@angular/core';

import { CommonModule } from '@angular/common';
import { ActualizacionesService } from '../../../services/actualizaciones.service';


@Component({
  selector: 'app-lista-actualizaciones',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './lista-actualizaciones.component.html',
  styleUrls: ['./lista-actualizaciones.component.css']
})
export class ListaActualizacionesComponent implements OnInit {
  actualizaciones: any[] = [];
  errorMessage: string = '';

  constructor(private actualizacionesService: ActualizacionesService) { }

  ngOnInit(): void {
    this.actualizacionesService.getActualizaciones().subscribe({
      next: (data) => {
        this.actualizaciones = data;
      },
      error: (error) => {
        this.errorMessage = 'Error al cargar las actualizaciones.';
        console.error('Error al cargar las actualizaciones', error);
      }
    });
  }
}
