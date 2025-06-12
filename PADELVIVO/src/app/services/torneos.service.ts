import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Torneo } from '../models/Torneo';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TorneosService {
  private apiUrl = `${environment.apiUrl}/api/torneos`;

  constructor(private http: HttpClient) {}

  getTorneos(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  guardarTorneo(torneo: any): Observable<any> {
    return this.http.post(this.apiUrl, torneo);
  }

  eliminarTorneo(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  actualizarTorneo(torneo: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${torneo.id}`, torneo);
  }
}
