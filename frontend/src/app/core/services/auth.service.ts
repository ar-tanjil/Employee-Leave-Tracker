import { ApiResponse } from '../../models/api-response.model';
import { computed, effect, inject, Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from './api.service';
import { AuthState, UserLogin } from '../../models/auth.model';
import { JwtUtil } from '../../shared/utils/jwtUtil ';


export const AUTH_STORAGE_KEY = 'auth_state';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private api = inject(ApiService);

  private readonly _state = signal<AuthState>(this._loadFromStorage());

  readonly token = computed(() => this._state().token);
  readonly currentUser = computed(() => this._state().user);
  readonly isAuthenticated = computed(() => {
    const state = this._state();
    return state.token !== null && !JwtUtil.isTokenExpired(state.token);
  });

  constructor(private readonly router: Router) {
    effect(() => {
      const state = this._state();
      if (state.token !== null && !JwtUtil.isTokenExpired(state.token)) {
        localStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify(state));
      }
    });
  }

  login(user: UserLogin, expiresInMs = 3_600_000): void {
    this.api.post<{ token: string }>('v1/auth/login', {
      username: user.username,
      password: user.password,
    }).subscribe({
      next: (res) => {
        const authState = JwtUtil.buildAuthState(res.token);
        this._state.set(authState);
        this.router.navigate(['/']);
      },
      error: (err) => {
        console.error('Login failed', err);
      },
    });
  }

  logout(): void {
    this._state.set({
      token: null,
      user: null,
      expiresAt: null,
    });
    localStorage.clear();
    this.router.navigate(['/login']);
  }

  private _loadFromStorage(): AuthState {
    try {
      const raw = localStorage.getItem(AUTH_STORAGE_KEY);
      if (raw) {
        return JSON.parse(raw) as AuthState;
      }
    } catch {
      // corrupted storage - start fresh
    }
    return {
      token: null,
      user: null,
      expiresAt: null,
    };
  }


}
