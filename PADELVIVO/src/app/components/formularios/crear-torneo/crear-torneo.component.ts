import { Component, EventEmitter, NgModule, OnInit, Output } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Torneo } from '../../../models/Torneo';
import { Temporada } from '../../../models/Temporada';
import { FormsModule, NgModel } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'app-crear-torneo',
  templateUrl: './crear-torneo.component.html',
  imports: [FormsModule, CommonModule],
  styleUrls: ['./crear-torneo.component.css'],
})
export class CrearTorneoComponent implements OnInit {
  temporadas: Temporada[] = [];
  mensaje: string = '';
  mensajeError: string = '';

  torneo: Torneo = {
    name: '',
    location: '',
    country: '',
    level: '',
    status: '',
    startDate: '',
    endDate: '',
    temporada: { id: 0, name: '', startDate: '', endDate: '', year: 0 },
  };

  @Output() toeneoCreado = new EventEmitter<void>();

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarTemporadas();
  }

  cargarTemporadas(): void {
    this.http
      .get<Temporada[]>(`${environment.apiUrl}/api/temporadas`)
      .subscribe({
        next: (data) => {
          this.temporadas = data;
        },
        error: (err) => {
          console.error('Error cargando temporadas', err);
        },
      });
  }

  crearTorneo(): void {
    this.http
      .post<Torneo>(`${environment.apiUrl}/api/torneos`, this.torneo)
      .subscribe({
        next: (data) => {
          console.log('Torneo creado:', data);
          this.mensaje = 'Torneo creado correctamente';
        },
        error: (error) => {
          console.error('Error al crear el torneo:', error);
          this.mensaje = 'Error al crear el torneo.';
        },
      });
      this.toeneoCreado.emit();
    this.router.navigate(['admin/torneos']);
  }


  resetForm(): void {
    this.torneo = {
      name: '',
      location: '',
      country: '',
      level: '',
      status: '',
      startDate: '',
      endDate: '',
      temporada: { id: 0, name: '', startDate: '', endDate: '', year: 0 },
    };
  }
}
