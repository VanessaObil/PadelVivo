import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TemporadasService {
  private apiUrl = `${environment.apiUrl}/api/temporadas`;

  constructor(private http: HttpClient) { }

  getTemporadas(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  eliminarTemporada(id: number): Observable<any> {
    // responseType: 'text' evita el error de HttpErrorResponse con status 200
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' as 'json' });
  }

  crearTemporada(temporada: any): Observable<any> {
    return this.http.post(this.apiUrl, temporada, { responseType: 'text' as 'json' });
  }

  actualizarTemporada(id: number, temporada: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, temporada);
  }
}
