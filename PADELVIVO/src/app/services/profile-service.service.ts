import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { AuthService } from './auth.service'; // importa tu AuthService

export interface User {
  id: number;
  username: string;
  firstName: string;
  lastName: string;
  image?: string;
  enabled: boolean;
  createdDate?: string;
  lastModifiedDate?: string;
  lastPasswordChangeDate?: string;
}

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  private apiUrl = `${environment.apiUrl}/api/perfil`;

  constructor(private http: HttpClient, private authService: AuthService) {}

  getProfile() {
    return this.http.get<any>(`${environment.apiUrl}/api/users/me`);
  }

  updateProfile(user: Partial<User> & { password?: string | null }): Observable<User> {
    const token = localStorage.getItem('token');
    if (!token) {
      throw new Error('No token found');
    }
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.http.put<User>(this.apiUrl, user, { headers });
  }
}
