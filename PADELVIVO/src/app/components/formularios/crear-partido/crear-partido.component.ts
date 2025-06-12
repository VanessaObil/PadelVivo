import { Component } from '@angular/core';
import { PartidosService } from '../../../services/partidos.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-crear-partido',
  templateUrl: './crear-partido.component.html',
  styleUrls: ['./crear-partido.component.css'],
  imports: [FormsModule, CommonModule],
})
export class CrearPartidoComponent {

  setsInput = '';
puntosInput = '';

  partido: any = {
    partidoId: '',
    marcadorActual: '',
    estadoPartido: '',
    setsActuales: '',
    puntosActuales: '',
    jugadoresActuales: [],
    nacionalidades: [],
    fechaPartido: ''
  };

  jugadoresInput = '';
  nacionalidadesInput = '';

  mensaje = '';
  mensajeError = '';

  constructor(private partidoService: PartidosService) {}

  crearPartido() {
  this.partido.jugadoresActuales = this.jugadoresInput.split(',').map(j => j.trim());
  this.partido.nacionalidades = this.nacionalidadesInput.split(',').map(n => n.trim());


  this.partido.setsActuales = this.setsInput
    .split(',')
    .map(s => parseInt(s.trim()))
    .filter(n => !isNaN(n));

  this.partido.puntosActuales = this.puntosInput
    .split(',')
    .map(p => parseInt(p.trim()))
    .filter(n => !isNaN(n));

  this.partidoService.crearPartido(this.partido).subscribe({
    next: () => {
      this.mensaje = 'Partido creado exitosamente';
      this.mensajeError = '';

    },
    error: (err) => {
      this.mensajeError = 'Error al crear el partido';
      console.error(err);
    }
  });
}



}
