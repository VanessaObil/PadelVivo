import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FavoritosService {

 private baseUrl = `${environment.apiUrl}/api/favoritos`; // Ajusta la URL base

  constructor(private http: HttpClient) { }

  getFavoritos() {
    return this.http.get<any[]>(this.baseUrl);
  }

  agregarFavorito(matchId: string) {
    return this.http.post(this.baseUrl + '?matchId=' + matchId, { responseType: 'text' });
  }

  eliminarFavorito(matchId: string) {
    return this.http.delete(this.baseUrl + '?matchId=' + matchId);
  }
  estaEnFavoritos(matchId: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.baseUrl}/existe?matchId=${matchId}`);
  }
}
