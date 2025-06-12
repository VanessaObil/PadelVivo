import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PartidosService {
  private apiUrl = `${environment.apiUrl}/api/partidos`;

  constructor(private http: HttpClient) { }

  getPartidos(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  eliminarPartido(id: string): Observable<any> {
  return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' });
}


  crearPartido(partido: any): Observable<any> {
    return this.http.post(this.apiUrl, partido);
  }

  actualizarPartido(id: string, partido: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, partido);
  }

}
