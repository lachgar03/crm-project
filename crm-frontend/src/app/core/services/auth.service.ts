import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/v1/auth';
  private readonly TOKEN_KEY = 'auth_token';

  currentUserSignal = signal<any>(null);

  constructor(private http: HttpClient, private router: Router) {
    this.loadUserFromToken();
  }

  login(credentials: any) {
    return this.http.post<{ token: string }>(`${this.apiUrl}/login`, credentials);
  }

  setToken(token: string) {
    localStorage.setItem(this.TOKEN_KEY, token);
    this.loadUserFromToken();
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  logout() {
    localStorage.removeItem(this.TOKEN_KEY);
    this.currentUserSignal.set(null);
    this.router.navigate(['/login']);
  }

  private loadUserFromToken() {
    const token = this.getToken();
    if (token) {
      try {
        const decoded: any = jwtDecode(token);
        this.currentUserSignal.set(decoded);
      } catch (e) {
        console.error('Invalid token');
        this.logout();
      }
    }
  }

  getTenantName(): string {
    return this.currentUserSignal()?.tenantId ? `Tenant ${this.currentUserSignal().tenantId}` : 'CRM App';
  }

  getUserName(): string {
    return this.currentUserSignal()?.sub || 'User';
  }

  hasRole(role: string): boolean {
    const user = this.currentUserSignal();
    return user?.roles?.includes(role) || false;
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }
}
