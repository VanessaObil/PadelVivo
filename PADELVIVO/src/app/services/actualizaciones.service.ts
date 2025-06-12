import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ActualizacionesService {
private apiUrl = `${environment.apiUrl}/api/actualizaciones`;

  constructor(private http: HttpClient) { }

  getActualizaciones(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl, { withCredentials: true });
  }

}
