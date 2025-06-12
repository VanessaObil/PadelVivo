import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { jwtDecode } from 'jwt-decode';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly LOGIN_URL = `${environment.apiUrl}/api/v1/authenticate`;

  private isAuthenticatedSubject = new BehaviorSubject<boolean>(false);
  public isAuthenticated = this.isAuthenticatedSubject.asObservable();

  private roleSubject = new BehaviorSubject<string | null>(null);
  public role = this.roleSubject.asObservable();

  constructor(private http: HttpClient) {
    const token = localStorage.getItem('token');
    if (token) {
      try {
        const decoded: any = jwtDecode(token);
        const now = Math.floor(Date.now() / 1000);
        if (decoded.exp > now) {
          this.isAuthenticatedSubject.next(true);
          const roles = this.getRolesFromToken(token);
          if (roles.length > 0) {
            this.roleSubject.next(roles[0]);
          }
        } else {
          this.logout();
        }
      } catch (e) {
        this.logout();
      }
    }
  }

  login(credentials: { username: string; password: string; }) {
    return this.http.post<{ token: string, message: string }>(this.LOGIN_URL, credentials).pipe(
      tap(response => {
        if (response.token) {
          localStorage.setItem('token', response.token);
          this.isAuthenticatedSubject.next(true);
          const roles = this.getRolesFromToken(response.token);
          this.roleSubject.next(roles.length > 0 ? roles[0] : null);
        }
      })
    );
  }

  logout() {
    localStorage.removeItem('token');
    this.isAuthenticatedSubject.next(false);
    this.roleSubject.next(null);
  }

  isLoggedIn(): boolean {
    const token = localStorage.getItem('token');
    if (!token) return false;

    try {
      const decoded: any = jwtDecode(token);
      const now = Math.floor(Date.now() / 1000);
      return decoded.exp && decoded.exp > now;
    } catch (e) {
      return false;
    }
  }

  getRolesFromToken(token: string): string[] {
    try {
      const decoded: any = jwtDecode(token);
      return decoded.roles || [];
    } catch (e) {
      return [];
    }
  }

  getRole(): string | null {
    return this.roleSubject.value;
  }

  isAdmin(): boolean {
    return this.getRole() === 'ROLE_ADMIN';
  }

  isUser(): boolean {
    return this.getRole() === 'ROLE_USER';
  }

  getUserFromToken(): any {
    const token = localStorage.getItem('token');
    if (!token) return null;

    try {
      const decoded: any = jwtDecode(token);
      return {
        id: decoded.id,
        username: decoded.sub,
        firstName: decoded.nombre,
        lastName: decoded.apellido,
        roles: decoded.roles
      };
    } catch (error) {
      console.error('Error decodificando token:', error);
      return null;
    }
  }
  getCurrentUser() {
  return this.http.get<any>(`${environment.apiUrl}/api/users/me`);
}
}
