import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Jugador } from '../models/Jugador';

@Injectable({
  providedIn: 'root'
})
export class JugadoresService {
  private apiUrl = `${environment.apiUrl}/api/jugadores`;

  constructor(private http: HttpClient) {}

  getJugadores(): Observable<any[]> {
    return this.http.get<Jugador[]>(this.apiUrl);
  }

  eliminarJugadores(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  crearJugador(jugador: any): Observable<any> {
  return this.http.post(this.apiUrl, jugador, { responseType: 'text' as 'json' });
}


  actualizarJugador(id: number, jugador: Jugador): Observable<Jugador> {
    return this.http.put<Jugador>(`${this.apiUrl}/${id}`, jugador);
  }
}
