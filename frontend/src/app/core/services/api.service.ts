import { HttpClient, HttpParams } from '@angular/common/http';
import { computed, inject, Injectable, signal } from '@angular/core';
import { environment } from '../../../environments/environment';
import { catchError, map, Observable, retry, throwError } from 'rxjs';
import { ApiResponse, PagedResponse, PaginationParams } from '../../models/api-response.model';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiBaseUrl}`;

  private buildUrl(endpoint: string): string {
    return `${this.baseUrl}/${endpoint}`;
  }

  // get
  get<T>(
    endpoint: string,
    params?: Record<string, string | number | boolean>,
  ): Observable<T> {
    let httpParams = new HttpParams();
    if (params) {
      Object.entries(params).forEach(([k, v]) => {
        httpParams = httpParams.set(k, String(v));
      });
    }

    return this.http
      .get<ApiResponse<T>>(this.buildUrl(endpoint), {
        params: httpParams,
      })
      .pipe(
        // retry({ count: 2, delay: 1000 }),
        map(res => res.data),
        catchError(this._handleError),);
  }

  // get paged
  getPaged<T>(
    endpoint: string,
    pagination: PaginationParams,
    extraParams?: Record<string, string | number | boolean>,
  ): Observable<PagedResponse<T>> {
    const params: Record<string, string | number | boolean> = {
      page: pagination.page,
      size: pagination.pageSize,
      ...extraParams,
    };

    if (pagination.sortBy) {
      params['_sort'] = pagination.sortBy;
      params['_order'] = pagination.sortOrder ?? 'asc';
    }
    if (pagination.search) {
      params['q'] = pagination.search;
    }

    let httpParams = new HttpParams();
    Object.entries(params).forEach(([k, v]) => {
      httpParams = httpParams.set(k, String(v));
    });

    return this.http
      .get<PagedResponse<T>>(this.buildUrl(endpoint), {
        params: httpParams
      })
      .pipe(
        // retry({ count: 2, delay: 1000 }),
        catchError(this._handleError),
      );
  }

  // Post
  post<TResponse, TBody = unknown>(
    endpoint: string,
    body: TBody,
  ): Observable<TResponse> {
    return this.http
      .post<ApiResponse<TResponse>>(this.buildUrl(endpoint), body)
      .pipe(map(res => res.data),
        catchError(this._handleError)
      );
  }

  // put
  put<TResponse, TBody = unknown>(
    endpoint: string,
    body: TBody,
  ): Observable<TResponse> {
    return this.http
      .put<ApiResponse<TResponse>>(this.buildUrl(endpoint), body)
      .pipe(map(res => res.data),
        catchError(this._handleError)
      );
  }

  // patch
  patch<TResponse, TBody = unknown>(
    endpoint: string,
    body: Partial<TBody>,
  ): Observable<TResponse> {
    return this.http
      .patch<ApiResponse<TResponse>>(this.buildUrl(endpoint), body)
      .pipe(map(res => res.data),
        catchError(this._handleError)
      );
  }

  // delete
  delete<T>(endpoint: string): Observable<T> {
    return this.http
      .delete<ApiResponse<T>>(this.buildUrl(endpoint))
      .pipe(map(res => res.data),
        catchError(this._handleError)
      );
  }

  // The global error interceptor also catches & shows toasts, so services stay clean
  private _handleError = (error: unknown): Observable<never> => {
    // Re-throw so interceptors and feature services can react
    return throwError(() => error);
  };
}
